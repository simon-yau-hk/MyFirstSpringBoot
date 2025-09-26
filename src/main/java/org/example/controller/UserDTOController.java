package org.example.controller;

import org.example.dto.UserDTO;
import org.example.entity.User;
import org.example.mapper.UserMapper;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * UserDTO Controller - demonstrates MapStruct usage
 * This controller uses DTOs instead of entities for cleaner API responses
 */
@RestController
@RequestMapping("/api/v2/users")  // v2 to differentiate from original controller
public class UserDTOController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private UserMapper userMapper;  // MapStruct generated mapper

    /**
     * GET /api/v2/users - Get all users as DTOs
     * This avoids JSON recursion issues and provides cleaner responses
     */
    @GetMapping
    public List<UserDTO> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return userMapper.toDTOList(users);  // MapStruct magic! 🎉
    }

    /**
     * GET /api/v2/users/{id} - Get user by ID as DTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            UserDTO userDTO = userMapper.toDTO(user.get());  // Entity → DTO
            return ResponseEntity.ok(userDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * POST /api/v2/users - Create a new user from DTO
     */
    @PostMapping
    public UserDTO createUser(@RequestBody UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);  // DTO → Entity
        User savedUser = userService.createUser(user.getName(), user.getEmail());
        return userMapper.toDTO(savedUser);  // Entity → DTO
    }

    /**
     * PUT /api/v2/users/{id} - Update user from DTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable Long id, 
            @RequestBody UserDTO userDTO) {
        
        Optional<User> existingUser = userService.getUserById(id);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            
            // MapStruct updates existing entity from DTO
            userMapper.updateUserFromDTO(userDTO, user);
            
            // Save and return updated DTO
            User updatedUser = userService.updateUser(id, user.getName(), user.getEmail()).get();
            return ResponseEntity.ok(userMapper.toDTO(updatedUser));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * DELETE /api/v2/users/{id} - Delete a user
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
     * GET /api/v2/users/count - Get total user count
     */
    @GetMapping("/count")
    public long getUserCount() {
        return userService.getUserCount();
    }
}
