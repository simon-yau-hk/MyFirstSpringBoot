package org.example.controller;

import org.example.common.HelloService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * REST Controller - handles HTTP requests
 * 
 * @RestController combines @Controller and @ResponseBody
 * This means methods return data directly (not view names)
 */
@RestController
@RequestMapping("/api") // Base URL path for all endpoints in this controller
public class HelloController {

    private final Environment springEnv;
    private final HelloService helloService;

    @Value("${environment:unknown}")
    private String environment;

    public HelloController(Environment springEnv, HelloService helloService) {
        this.springEnv = springEnv;
        this.helloService = helloService;
    }
    /**
     * Simple GET endpoint
     * Accessible at: GET http://localhost:8080/api/hello
     */
    @GetMapping("/hello")
    public String hello() {
        return helloService.greet() + " 🎉";
    }

    /**
     * GET endpoint with path variable
     * Accessible at: GET http://localhost:8080/api/hello/John
     */
    @GetMapping("/hello/{name}")
    public String helloWithName(@PathVariable String name) {
        return "Hello, " + name + "! Welcome to Spring Boot! 👋";
    }

    /**
     * GET endpoint with query parameter
     * Accessible at: GET http://localhost:8080/api/greet?name=John&age=25
     */
    @GetMapping("/greet")
    public String greetWithParams(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int age) {
        return String.format("Greetings, %s! You are %d years old. 🎂", name, age);
    }

    /**
     * POST endpoint that accepts JSON data
     * Accessible at: POST http://localhost:8080/api/user
     * Send JSON: {"name": "John", "email": "john@example.com"}
     */
    @PostMapping("/user")
    public String createUser(@RequestBody UserRequest userRequest) {
        return "User created: " + userRequest.getName() + 
               " with email: " + userRequest.getEmail() + " ✅";
    }

    @GetMapping("/hello/env")
    public Map<String, String> showEnv() {
    Map<String, String> info = new LinkedHashMap<>();
    info.put("environment", environment);
    info.put("activeProfiles", String.join(",", springEnv.getActiveProfiles()));
    info.put("defaultProfiles", String.join(",", springEnv.getDefaultProfiles()));
    info.put("applicationName", springEnv.getProperty("spring.application.name"));
    return info;
}

    /**
     * Simple DTO (Data Transfer Object) class
     * Used to receive JSON data in POST requests
     */
    public static class UserRequest {
        private String name;
        private String email;

        // Default constructor (required for JSON deserialization)
        public UserRequest() {}

        // Constructor with parameters
        public UserRequest(String name, String email) {
            this.name = name;
            this.email = email;
        }

        // Getters and setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
