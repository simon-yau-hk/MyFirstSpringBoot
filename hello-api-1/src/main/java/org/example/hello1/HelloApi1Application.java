package org.example.hello1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"org.example.hello1", "org.example.common"})
public class HelloApi1Application {

    public static void main(String[] args) {
        SpringApplication.run(HelloApi1Application.class, args);
    }
}
