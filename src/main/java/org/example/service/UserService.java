package org.example.service;

import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import org.example.entity.User;


/**
 * Service Layer - contains business logic
 * 
 * @Service marks this class as a service component
 * Spring will automatically create an instance (bean) of this class
 */
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

  
   /**
     * Get all users from database
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Get user by ID from database
     */
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Create a new user in database
     */
    public User createUser(String name, String email) {
        User user = new User(name, email);
        return userRepository.save(user);
    }

    /**
     * Update an existing user in database
     */
    public Optional<User> updateUser(Long id, String name, String email) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setName(name);
            user.setEmail(email);
            return Optional.of(userRepository.save(user));
        }
        return Optional.empty();
    }

    /**
     * Delete a user from database
     */
    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Get user count from database
     */
    public long getUserCount() {
        return userRepository.count();
    }

    /**
     * Find user by email
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Search users by keyword
     */
    public List<User> searchUsers(String keyword) {
        return userRepository.searchUsers(keyword);
    }

   
}
