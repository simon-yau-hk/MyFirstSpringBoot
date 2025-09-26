package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;

import jakarta.annotation.PostConstruct;

/**
 * Application Configuration Class
 * 
 * This class demonstrates:
 * - Dependency Injection setup
 * - Bean configuration
 * - Swagger/OpenAPI configuration
 * - CORS configuration
 */
@Configuration
public class ApplicationConfig {

    /**
     * This method runs after the bean is created and dependencies are injected
     * It's part of the Spring lifecycle
     */
    @PostConstruct
    public void init() {
        System.out.println("🔧 ApplicationConfig initialized - DI setup complete!");
        System.out.println("📚 Swagger UI available at: http://localhost:8080/swagger-ui.html");
        System.out.println("📊 Actuator endpoints at: http://localhost:8080/actuator");
    }

    /**
     * Configure Swagger/OpenAPI documentation
     * This bean will be automatically injected where needed
     */
    @Bean
    public OpenAPI customOpenAPI() {
        System.out.println("🔧 Creating OpenAPI bean for Swagger documentation");
        return new OpenAPI()
                .info(new Info()
                        .title("My First Spring Boot API")
                        .version("1.0.0")
                        .description("A sample Spring Boot REST API with Swagger documentation")
                        .contact(new Contact()
                                .name("Your Name")
                                .email("your.email@example.com")));
    }

    /**
     * Configure CORS (Cross-Origin Resource Sharing)
     * This bean demonstrates dependency injection in configuration
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        System.out.println("🔧 Creating CORS configuration bean");
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("http://localhost:3000", "http://localhost:4200")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }

    /**
     * Example of a custom bean that will be managed by Spring's DI container
     */
    @Bean
    public ApplicationInfo applicationInfo() {
        System.out.println("🔧 Creating ApplicationInfo bean");
        return new ApplicationInfo("My First Spring Boot", "1.0.0", "Learning Spring Boot DI");
    }

    /**
     * Simple POJO class to demonstrate bean creation
     */
    public static class ApplicationInfo {
        private final String name;
        private final String version;
        private final String description;

        public ApplicationInfo(String name, String version, String description) {
            this.name = name;
            this.version = version;
            this.description = description;
        }

        public String getName() { return name; }
        public String getVersion() { return version; }
        public String getDescription() { return description; }

        @Override
        public String toString() {
            return String.format("ApplicationInfo{name='%s', version='%s', description='%s'}", 
                               name, version, description);
        }
    }
}
