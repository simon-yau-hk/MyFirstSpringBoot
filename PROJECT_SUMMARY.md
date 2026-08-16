# MyFirstSpringBoot — 專案摘要

給 C# 背景。Spring Boot ≈ ASP.NET Core + DI + EF Core 一次打包。

---

## 1. Architecture

經典 **3-layer REST API**。embedded Tomcat `:8080`，MySQL `:3307`（`localDocker/docker-compose.yml`）。

```
HTTP
  → Controller
  → Service (@Transactional)
  → Repository (JPA / Hibernate)
  → MySQL
       ↘ 出錯
         GlobalExceptionHandler → ProblemDetail JSON（RFC 7807）
```

對外 v2：**Controller 只碰 DTO**；Entity ↔ DTO 由 MapStruct 喺 Service 做。

| Layer | Package | C# 對照 |
|---|---|---|
| Controller | `controller/` | `[ApiController]` |
| Service | `service/` | application service |
| Repository | `repository/` | `DbSet` + repository |
| Entity | `entity/` | EF entity |
| DTO | `dto/` | API contract |
| Mapper | `mapper/` | AutoMapper（compile-time） |
| Exception | `exception/` | exception middleware / `ProblemDetails` |
| Config | `config/` | `IServiceCollection` |

### Domain

```
User 1 ──< Bag 1 ──< BagItem
```

- `User.bags` 用 `Set`（避免 Hibernate multiple-bag fetch）
- `cascade + orphanRemoval`：刪 User 連 Bag / Item
- `LAZY`：預設唔載入關聯
- 查詢用 `@EntityGraph` / `JOIN FETCH` 一次撈 nested data，避 N+1

`UserDTOService.getAllUsers()` 分兩步（唔可以一次 FETCH 兩層 collection）：

1. `UserRepository.findAll()` → 連 `bags`（`@EntityGraph`）
2. `BagRepository.findWithItemsByUsers(users)` → 同一個 persistence context 再 fetch `items`

### API

| Path | 回傳 | 註 |
|---|---|---|
| `/api/v2/users` | `UserDTO` | 正確做法：DTO + mapper |
| `/api/v2/users/TestThrowException` | 404 ProblemDetail | 試 `GlobalExceptionHandler` |
| `/api/v2/bags?Id=` | `BagDTO` | query param `Id` |
| `/api/users` | Entity | 學習用，唔好當正式 API |
| `/api/sample-data` | seed / clear | 測試資料 |
| `/api/hello` | string | 最簡 endpoint |

### 而家跟到嘅 practice

- **Constructor injection**（`final` field，唔再用 field `@Autowired`）
- v2 **唔 return Entity**
- **`@RestControllerAdvice`** + `ProblemDetail`（≈ ASP.NET `ProblemDetails`）
- DTO service 方法加 **`@Transactional(readOnly = true)`**
- N+1：`@EntityGraph` / `JOIN FETCH`
- `spring-boot-starter-validation` 已加；handler 已接 `MethodArgumentNotValidException`（DTO 未掛 `@NotBlank` 等）

### Stack

Java 21 / Spring Boot 3.1.4 / Maven / JPA+Hibernate / MySQL / MapStruct / springdoc / Actuator / Validation

---

## 2. Technical — 點樣跑

`main()` → `SpringApplication.run(...)` ≈ `WebApplication.CreateBuilder().Build().Run()`

`@SpringBootApplication` = `@Configuration` + `@EnableAutoConfiguration` + `@ComponentScan`（掃 `org.example.**`，標咗 stereotype 就變 bean）。

單次 request：

```
GET /api/v2/users
  → DispatcherServlet
  → UserDTOController（constructor 注入 UserDTOService）
  → UserDTOService.getAllUsers()   // @Transactional
  → UserRepository + BagRepository
  → UserMapper.toDTOList()         // MapStruct 編譯期產生
  → Jackson JSON
```

例外：controller/service `throw ResourceNotFoundException` → `@RestControllerAdvice` 攔截 → `404 ProblemDetail`。唔使每個 action 寫 `try/catch`（`SampleDataController` 仲有舊式 catch，可以之後清）。

---

## 3. 本專案用緊嘅 Annotation（逐個點解）

C# attribute ≈ Java annotation。Spring **靠呢啲喺 runtime 組裝**，你唔使 `new` Controller/Service。

### 啟動 / DI / Config

| Annotation | 用喺邊 | 點解要 |
|---|---|---|
| `@SpringBootApplication` | `MyFirstSpringBootApplication` | 入口。開 auto-config + 掃 package 註冊 bean。≈ `WebApplication` 預設 pipeline。 |
| `@Configuration` | `ApplicationConfig` | 話俾 Spring 知：呢個 class 入面嘅 `@Bean` 方法係手動註冊。≈ `IServiceCollection` 擴展。 |
| `@Bean` | `customOpenAPI()` / `corsConfigurer()` / `applicationInfo()` | 方法回傳值放進 container。用嚟配 **第三方 / 自己 new 嘅物件**（OpenAPI、CORS），唔係業務 class。 |
| `@PostConstruct` | `ApplicationConfig.init()` | bean 建好、注入完之後跑一次。≈ constructor 之後嘅 init hook。呢度淨係 print URL。 |
| `@Override` | `run()` / `findAll()` | Java 編譯器檢查：真係 override 父類/interface。同 C# `override` 一樣，唔係 Spring 特有。 |

**點解而家 constructor 冇 `@Autowired`？**  
Spring 4.3+：**單一 constructor 會自動注入**。寫 `public UserDTOController(UserDTOService s, UserMapper m)` 就夠。`final` 保證必填、易測。Field `@Autowired` 係舊 tutorial 寫法。

Stereotype（掃到就註冊；語意唔同，技術上差唔多）：

| Annotation | 用喺邊 | 點解要 |
|---|---|---|
| `@RestController` | 所有 controller | = `@Controller` + `@ResponseBody`。方法 return 值直接變 JSON，唔係 view 名。≈ `[ApiController]`。 |
| `@Service` | `UserDTOService` 等 | 標業務層。AOP（`@Transactional`）預設打喺呢層。 |
| `@Repository` | `*Repository` | 標資料層；順便把 DB exception 轉成 Spring `DataAccessException`。 |

### Web / HTTP

| Annotation | 用喺邊 | 點解要 |
|---|---|---|
| `@RequestMapping("/api/v2/users")` | class | 呢個 controller 嘅 URL 前綴。≈ `[Route("api/v2/users")]`。 |
| `@GetMapping` / `@PostMapping` / `@PutMapping` / `@DeleteMapping` | method | HTTP verb + path。`@GetMapping("/{id}")` ≈ `[HttpGet("{id}")]`。 |
| `@PathVariable` | `getUserById(@PathVariable Long id)` | 從 URL path 攞。`/api/users/1` → `id=1`。≈ `[FromRoute]`。 |
| `@RequestParam` | `greet?name=&age=` | 從 query string 攞。`defaultValue` = 可選。≈ `[FromQuery]`。冇標嘅簡單參數（如 `getBagsByUserId(long Id)`）Spring 都會當 query param。 |
| `@RequestBody` | `createUser(@RequestBody CreateUserRequest)` | JSON body → 物件。≈ `[FromBody]`。 |

`ResponseEntity<T>` 唔係 annotation，係回傳類型：自己控 status（200/404）。≈ `ActionResult<T>`。

### Exception（新層）

| Annotation | 用喺邊 | 點解要 |
|---|---|---|
| `@RestControllerAdvice` | `GlobalExceptionHandler` | 全局攔截例外，回 JSON。≈ ASP.NET exception middleware + `ProblemDetails`。一個 class 服務全部 controller。 |
| `@ExceptionHandler(Xxx.class)` | 每個 handler 方法 | 「呢種 exception 用呢個方法」。對應 404 / 409 / 400 validation / DB unique。 |

本專案例外：

- `ResourceNotFoundException` → 404
- `DuplicateResourceException` → 409
- `MethodArgumentNotValidException` → 400（`@Valid` 失敗時先會觸發）
- `DataIntegrityViolationException` → 409（例如 email unique）

### Persistence（JPA ≈ EF）

| Annotation | 用喺邊 | 點解要 |
|---|---|---|
| `@Entity` | `User` / `Bag` / `BagItem` | 呢個 class 對應一張表。≈ EF entity。 |
| `@Table(name = "users")` | entity class | 表名。`users` 因為 `user` 喺 MySQL 係保留字。 |
| `@Id` | `id` 欄 | PK。≈ `[Key]`。 |
| `@GeneratedValue(IDENTITY)` | `id` | DB auto-increment。≈ identity column。 |
| `@Column` | 一般欄 | `nullable` / `unique` / `length` / `name` / `precision`。≈ `[Column]` + `[Required]`（DB 層，唔係 API validation）。 |
| `@OneToMany(mappedBy="user", cascade=ALL, fetch=LAZY, orphanRemoval=true)` | `User.bags` | 一對多。`mappedBy` = FK 喺對方。`cascade` 存/刪跟住走。`orphanRemoval` 從 collection 拎走就 DELETE。`LAZY` = 用到先查。 |
| `@ManyToOne(fetch=LAZY)` | `Bag.user` | 多對一。FK 喺呢邊。 |
| `@JoinColumn(name="user_id")` | 同上 | FK 欄名。≈ `[ForeignKey]`。 |
| `@PreUpdate` | `Bag.preUpdate()` | flush/update 前自動跑。呢度 stamp `updatedAt`。≈ EF interceptor / `SaveChanges` hook。 |
| `@Transactional` | Service 寫操作 | 方法包一個 DB transaction。例外就 rollback。≈ `IDbContextTransaction`。 |
| `@Transactional(readOnly=true)` | query service | 提示係讀；某啲 driver/Hibernate 可 skip dirty check。**Lazy load 一定要喺 transaction 內**，否則 `LazyInitializationException`。 |

### Repository query

| Annotation | 用喺邊 | 點解要 |
|---|---|---|
| `@Query("SELECT …")` | custom 方法 | 手寫 **JPQL**（entity 名，唔係 table 名）。方法名表達唔到嘅查詢先用。 |
| `@Param("users")` | 方法參數 | 同 JPQL `:users` 綁埋。唔綁有時仍然 work，明確較穩。 |
| `@EntityGraph(attributePaths={"bags"})` | `findAll` / `findById` | 呢次 query **順便 fetch** 指定關聯，唔改 entity 預設 LAZY。避 N+1。≈ EF `.Include(u => u.Bags)`。 |
| `@NonNull` | `findAll` / `findById` | 滿足 `JpaRepository` 契約 + 靜態分析；同業務關係唔大。 |

`findByEmail` / `findByUserId` **冇 annotation**：Spring Data 睇方法名產生 SQL（derived query）。

### JSON（Jackson）

| Annotation | 用喺邊 | 點解要 |
|---|---|---|
| `@JsonManagedReference("user-bags")` | 父（`User.bags`） | 序列化呢邊。 |
| `@JsonBackReference("user-bags")` | 子（`Bag.user`） | **唔**序列化返上去。否則 User→Bag→User 無限循環。v2 用 DTO 其實較乾淨，v1 return Entity 先靠呢對。 |

同名 `"user-bags"` / `"bag-items"` 配對，一個 class 可以有多段關係。

### MapStruct（≈ AutoMapper profile）

| Annotation | 用喺邊 | 點解要 |
|---|---|---|
| `@Mapper(componentModel="spring", uses=BagMapper.class, unmappedTargetPolicy=IGNORE)` | mapper interface | 編譯期產生實作 class，並註冊成 Spring bean。`uses` = nested 用另一個 mapper。`IGNORE` = 冇對應欄唔 fail build。 |
| `@Mapping(target="bagCount", expression="java(…)")` | `toDTO` | 欄位名唔對、或者要計（size / totalPrice）時手寫 mapping。 |
| `@Mapping(target="bags", ignore=true)` | `toEntity` | DTO→Entity 唔改關聯；關聯由 `addBag()` 管。 |
| `@MappingTarget` | `updateUserFromDTO(dto, @MappingTarget User user)` | 唔 new，**改現有 entity**（PATCH）。≈ AutoMapper `Map(src, dest)`。 |

---

## 4. Spring Boot 常用、本專案未用（值得識）

學完而家呢套，下一層最常碰到：

### Validation（pom 已有 `starter-validation`，DTO 未掛）

| Annotation | 用途 | C# |
|---|---|---|
| `@Valid` / `@Validated` | 觸發對 `@RequestBody` 嘅檢查；失敗 → `MethodArgumentNotValidException`（你 handler 已接） | `[ApiController]` 自動 400 |
| `@NotNull` `@NotBlank` `@NotEmpty` | 必填 | `[Required]` |
| `@Email` `@Size(min,max)` `@Min` `@Max` `@Positive` | 格式 / 範圍 | `[EmailAddress]` `[Range]` |
| `@Pattern(regexp=)` | regex | `[RegularExpression]` |

### HTTP / API 進階

| Annotation | 用途 |
|---|---|
| `@PatchMapping` | 部分更新 |
| `@ResponseStatus(HttpStatus.NO_CONTENT)` | 方法直接定 status（例如 DELETE 204） |
| `@CrossOrigin` | 單個 controller CORS（你而家喺 `ApplicationConfig` 全局配） |
| `@RequestHeader` `@CookieValue` | header / cookie |
| `@ModelAttribute` | form / query bind 成物件 |
| `@InitBinder` | 自訂 binding |

### DI 進階

| Annotation | 用途 | C# |
|---|---|---|
| `@Autowired` | 多 constructor / field / setter 先要顯式標 | constructor injection |
| `@Qualifier("name")` | 同一類型多個 bean 揀邊個 | named registration |
| `@Primary` | 預設用呢個實作 |  |
| `@Value("${server.port}")` | 注入 config 值 | `IOptions<T>` / `[FromServices]` 唔同，呢個係讀 properties |
| `@ConfigurationProperties(prefix="app")` | 一整組 config bind 去 POJO | `IOptions<AppOptions>` |
| `@Profile("docker")` | 呢個 bean 只喺某 profile 生效 |  |
| `@Scope("prototype")` | 非 singleton（預設 singleton ≈ `AddSingleton`；request scope ≈ `AddScoped` 用 `@RequestScope`） |  |
| `@Lazy` | 第一次用先建 bean |  |
| `@Order` | bean / filter 次序 |  |

### 生命週期 / AOP

| Annotation | 用途 |
|---|---|
| `@PreDestroy` | bean 銷毀前（對 `@PostConstruct`） |
| `@Async` | 方法丟去 thread pool（要 `@EnableAsync`） |
| `@Scheduled` / `@EnableScheduling` | cron / fixedRate |
| `@Cacheable` `@CacheEvict` | 方法級 cache |
| `@Retryable` | 失敗重試（要 spring-retry） |
| `@EventListener` | 聽 `ApplicationEvent` |

### JPA 更多

| Annotation | 用途 |
|---|---|
| `@Embedded` `@Embeddable` | value object 嵌進同一表 |
| `@Enumerated(STRING)` | enum 存字串 |
| `@CreatedDate` `@LastModifiedDate` | 要 `@EnableJpaAuditing`，代替手動 `@PreUpdate` |
| `@Version` | optimistic lock |
| `@Modifying` | `@Query` 做 UPDATE/DELETE（唔係 SELECT） |
| `@Lock(PESSIMISTIC_WRITE)` | SELECT FOR UPDATE |
| `@NamedQuery` | 預先命名 JPQL |
| `@SqlResultSetMapping` | native SQL 映射 |
| `@Transient` | 唔 persist 嘅欄 |

### Security（未加 `spring-boot-starter-security`）

| Annotation | 用途 | C# |
|---|---|---|
| `@EnableWebSecurity` | 開 security filter chain | `AddAuthentication` |
| `@PreAuthorize("hasRole('ADMIN')")` | 方法級授權 | `[Authorize(Roles="Admin")]` |
| `@Secured` / `@RolesAllowed` | 較舊嘅角色檢查 |  |

### Test

| Annotation | 用途 |
|---|---|
| `@SpringBootTest` | 起成個 context（慢，integration） |
| `@WebMvcTest(UserDTOController.class)` | 只測 web 層 |
| `@DataJpaTest` | 只測 repository（多數用 H2） |
| `@MockBean` / `@MockitoBean` | 換掉某個 bean |
| `@Test` `@BeforeEach` | JUnit 5 ≈ `[Fact]` / constructor setup |
| `@DisplayName` `@ParameterizedTest` | 可讀 / 多組 input |

### 其他常見

| Annotation | 用途 |
|---|---|
| `@ConditionalOnProperty` / `@ConditionalOnMissingBean` | auto-config 條件（寫 library / starter 先多用） |
| `@EnableConfigurationProperties` | 開啟 `@ConfigurationProperties` class |
| `@Controller` | 回 **view 名**（Thymeleaf）；REST 用 `@RestController` |
| `@ResponseBody` | 單個方法回 JSON（已被 `@RestController` 包埋） |
| `@Slf4j`（Lombok） | 生成 `log` 欄；唔係 Spring，好常用 |
| `@Data` `@RequiredArgsConstructor`（Lombok） | 生成 getter / constructor；可大幅減 boilerplate |

---

## 心智模型

**Annotation = 俾 Spring 嘅聲明：邊個係 bean、邊個 URL、邊個要開 transaction、例外點轉 HTTP。**  
Request：Controller → Service（transaction）→ Repository（Hibernate）→ Mapper → JSON；例外由 `@RestControllerAdvice` 統一變 `ProblemDetail`。
