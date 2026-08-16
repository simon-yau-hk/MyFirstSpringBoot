package org.example.hello2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"org.example.hello2", "org.example.common"})
public class HelloApi2Application {

    public static void main(String[] args) {
        SpringApplication.run(HelloApi2Application.class, args);
    }
}
