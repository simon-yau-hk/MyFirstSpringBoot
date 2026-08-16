package org.example.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Root Controller - handles requests to the base URL
 */

@RestController
public class RootController {

    /**
     * Root endpoint - accessible at the base URL
     * Accessible at: GET http://localhost:8080/
     */
    @GetMapping("/")
    public String root() {
        return "🚀 Welcome to My First Spring Boot Application! 🎉\n\n" +
               "Available endpoints:\n" +
               "• GET /api/hello - Simple greeting\n" +
               "• GET /api/hello/{name} - Personal greeting\n" +
               "• GET /api/greet?name=John&age=25 - Greeting with parameters\n" +
               "• GET /api/users - Get all users\n" +
               "• GET /api/users/count - Get user count\n" +
               "• POST /api/users - Create a new user\n" +
               "• POST /api/auth/register - Register with password\n" +
               "• POST /api/auth/login - Login and get JWT\n" +
               "• GET /api/auth/me - Current user (Bearer token)\n\n" +
               "Protected endpoints require Authorization: Bearer <token>\n\n" +
               "Happy coding! 👨‍💻";
    }
}
