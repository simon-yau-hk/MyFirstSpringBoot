package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.example.config.ApplicationConfig.ApplicationInfo;

/**
 * Main Spring Boot Application Class
 * 
 * @SpringBootApplication is a convenience annotation that combines:
 * - @Configuration: Indicates this is a configuration class
 * - @EnableAutoConfiguration: Enables Spring Boot's auto-configuration
 * - @ComponentScan: Enables component scanning in the current package and sub-packages
 * 
 * CommandLineRunner interface allows us to run code after the application context is loaded
 */
@SpringBootApplication
public class MyFirstSpringBootApplication implements CommandLineRunner {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ApplicationInfo applicationInfo;

    /**
     * Main method - entry point of the Spring Boot application
     * @param args command line arguments
     */
    public static void main(String[] args) {
        System.out.println("🏁 Starting Spring Boot Application...");
        
        // This starts the Spring Boot application
        SpringApplication.run(MyFirstSpringBootApplication.class, args);
        
        System.out.println("🚀 Spring Boot Application Started Successfully!");
        System.out.println("📝 Main endpoints:");
        System.out.println("   • Root: http://localhost:8080/");
        System.out.println("   • API: http://localhost:8080/api/hello");
        System.out.println("   • Swagger: http://localhost:8080/swagger-ui.html");
        System.out.println("   • Actuator: http://localhost:8080/actuator");
    }

    /**
     * This method runs after the application context is fully loaded
     * It demonstrates dependency injection in action
     */
    @Override
    public void run(String... args) throws Exception {
        System.out.println("\n🔧 === DEPENDENCY INJECTION DEMONSTRATION ===");
        System.out.println("📦 Application Info (injected bean): " + applicationInfo);
        
        // Show all beans managed by Spring
        System.out.println("🏭 Total beans managed by Spring: " + applicationContext.getBeanDefinitionCount());
        
        // Show some key beans
        String[] beanNames = applicationContext.getBeanDefinitionNames();
        System.out.println("🔧 Key application beans:");
        for (String beanName : beanNames) {
            if (beanName.contains("Controller") || beanName.contains("Service") || beanName.contains("Config")) {
                System.out.println("   • " + beanName + " (" + applicationContext.getBean(beanName).getClass().getSimpleName() + ")");
            }
        }
        System.out.println("=== END DI DEMONSTRATION ===\n");
    }
}
