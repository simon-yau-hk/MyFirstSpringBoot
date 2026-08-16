# MyFirstSpringBoot — 專案摘要

給 C# 背景的快速對照。Spring Boot ≈ ASP.NET Core + DI + EF Core 一次打包。

---

## 1. Architecture

經典 **3-layer REST API**，embedded Tomcat 跑在 `:8080`，MySQL 持久化。

```
HTTP → Controller → Service → Repository → MySQL
                      ↓
                   Mapper (Entity ↔ DTO)
```

| Layer | Package | C# 對照 |
|---|---|---|
| Controller | `controller/` | `[ApiController]` |
| Service | `service/` | Application / Business service |
| Repository | `repository/` | `DbContext` + repository |
| Entity | `entity/` | EF entity |
| DTO | `dto/` | API contract |
| Mapper | `mapper/` | AutoMapper |
| Config | `config/` | `IServiceCollection` setup |

### Domain

```
User 1 ──< Bag 1 ──< BagItem
```

- cascade + `orphanRemoval`：刪 User 會連 Bag / Item 一起刪（類似 EF cascade）
- `LAZY` fetch：預設不載入關聯（類似 EF lazy loading）
- `@JsonManagedReference` / `@JsonBackReference`：避免 JSON 循環

### API 兩套

| Path | 回傳 | 用途 |
|---|---|---|
| `/api/users` | Entity | 學習用，可能 JSON 循環 |
| `/api/v2/users` | DTO via MapStruct | 正確做法 |
| `/api/sample-data` | seed / clear | 測試資料 |
| `/api/hello` | string | 最簡 endpoint |

### Stack

- Java 21 / Spring Boot 3.1.4 / Maven
- Spring Web + Data JPA (Hibernate) + MySQL
- MapStruct / springdoc (Swagger) / Actuator
- Docker Compose（目前只包 app，DB 仍連 `localhost:3306`）

---

## 2. Technical — Spring Boot 怎麼跑（C# 對照）

### 啟動

`main()` → `SpringApplication.run(...)`  
≈ `WebApplication.CreateBuilder().Build().Run()`

`@SpringBootApplication` 一次做三件事：

1. `@Configuration` — 這是 config class
2. `@EnableAutoConfiguration` — 看 classpath 自動配 Tomcat / JPA / Jackson（≈ ASP.NET generic host + convention）
3. `@ComponentScan` — 掃 `org.example` 及子 package，把標了 annotation 的 class 註冊成 bean（≈ `AddControllers` + assembly scan）

`CommandLineRunner.run()` 在 context 建好後執行（≈ `IHostedService.StartAsync`）。

### DI（IoC Container）

Spring 管所有 **bean**（≈ singleton service）。

| Java | C# |
|---|---|
| `@Component` / `@Service` / `@Repository` / `@RestController` | `[ApiController]` + `AddScoped/Singleton` |
| `@Autowired` | constructor injection |
| `@Bean` in `@Configuration` | `services.AddSingleton(...)` |
| `@PostConstruct` | 物件建好後 hook |

這個專案用 **field injection**（`@Autowired` 欄位）。C# 習慣 constructor injection；Spring 也建議後者。

Stereotype 差別只是語意：Spring 掃到就註冊。`@RestController` = Controller + 回 JSON（≈ `[ApiController]`）。

### Request 生命週期

```
GET /api/v2/users/1
  → DispatcherServlet（≈ ASP.NET middleware pipeline）
  → UserDTOController.getUserById(@PathVariable Long id)
  → UserService.getUserById(id)
  → UserRepository.findById(id)   // Hibernate SQL
  → UserMapper.toDTO(user)        // MapStruct 編譯期產生實作
  → Jackson 序列化 JSON
```

| Annotation | C# |
|---|---|
| `@GetMapping` / `@PostMapping` | `[HttpGet]` / `[HttpPost]` |
| `@RequestMapping("/api/users")` | `[Route("api/users")]` |
| `@PathVariable` | `[FromRoute]` |
| `@RequestParam` | `[FromQuery]` |
| `@RequestBody` | `[FromBody]` |
| `ResponseEntity` | `ActionResult<T>` / `NotFound()` |

### Persistence（JPA ≈ EF Core）

`UserRepository extends JpaRepository<User, Long>` — **只要 interface，Spring 自動產生實作**（≈ EF `DbSet<User>` + 慣例 query）。

```java
Optional<User> findByEmail(String email);           // 依方法名產生 SQL
List<Bag> findByUserId(Long userId);

@Query("SELECT u FROM User u WHERE ...")            // JPQL，不是 SQL
List<User> searchUsers(String keyword);
```

Entity：

| JPA | EF |
|---|---|
| `@Entity` `@Table` | `[Table]` |
| `@Id` `@GeneratedValue` | `[Key]` + identity |
| `@OneToMany` / `@ManyToOne` | navigation + FK |
| `@JoinColumn(name = "user_id")` | `[ForeignKey]` |
| `@PreUpdate` | `SaveChanges` interceptor |
| `ddl-auto=update` | `EnsureCreated` / migrate（開發用） |

`@Transactional` ≈ `using var tx = db.Database.BeginTransaction()`，掛在 Service 方法上。

`Optional<T>` ≈ `T?`：沒資料不丟 NRE。

### MapStruct ≈ AutoMapper

interface + annotation，**編譯期產生實作 class**（比 AutoMapper 反射快）。

```java
@Mapper(componentModel = "spring", uses = {BagMapper.class})
public interface UserMapper {
    UserDTO toDTO(User user);
    User toEntity(UserDTO dto);
}
```

`componentModel = "spring"` → 產生的 mapper 本身也是 bean，可 `@Autowired`。

### Config

`application.properties` ≈ `appsettings.json`：

- `server.port=8080`
- MySQL + HikariCP connection pool
- `spring.jpa.show-sql=true`
- Actuator 全開、Swagger `/swagger-ui.html`

`ApplicationConfig` 手動註冊 CORS、OpenAPI、`ApplicationInfo` bean。

### 常用指令

```bash
mvn spring-boot:run          # ≈ dotnet run
mvn compile                  # MapStruct 在這步產生 mapper
```

Swagger: `http://localhost:8080/swagger-ui.html`  
Health: `http://localhost:8080/actuator/health`

---

## 心智模型（一句話）

**Controller 收 HTTP → Service 做業務 → Repository 講話給 Hibernate → Mapper 把 Entity 轉成 DTO 再回 JSON。**  
Spring 用 annotation + classpath 自動組裝，不必自己 `new` 這些物件。
