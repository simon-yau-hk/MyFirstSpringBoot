package org.example.controller;

import org.example.entity.User;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * User Controller - demonstrates dependency injection and CRUD operations
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    // Dependency Injection - Spring automatically injects the UserService
    @Autowired
    private UserService userService;

    /**
     * GET /api/users - Get all users
     */
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * GET /api/users/{id} - Get user by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * POST /api/users - Create a new user
     * Request body: {"name": "John Doe", "email": "john@example.com"}
     */
    @PostMapping
    public User createUser(@RequestBody CreateUserRequest request) {
        return userService.createUser(request.getName(), request.getEmail());
    }

    /**
     * PUT /api/users/{id} - Update an existing user
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable Long id, 
            @RequestBody CreateUserRequest request) {
        
        Optional<User> updatedUser = userService.updateUser(
            id, request.getName(), request.getEmail());
        
        if (updatedUser.isPresent()) {
            return ResponseEntity.ok(updatedUser.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * DELETE /api/users/{id} - Delete a user
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        boolean deleted = userService.deleteUser(id);
        if (deleted) {
            return ResponseEntity.ok("User deleted successfully ✅");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * GET /api/users/count - Get total user count
     */
    @GetMapping("/count")
    public long getUserCount() {
        return userService.getUserCount();
    }

    /**
     * Request DTO for creating/updating users
     */
    public static class CreateUserRequest {
        private String name;
        private String email;

        public CreateUserRequest() {}

        public CreateUserRequest(String name, String email) {
            this.name = name;
            this.email = email;
        }

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
