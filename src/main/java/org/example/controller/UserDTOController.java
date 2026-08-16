package org.example.controller;

import org.example.dto.UserDTO;
import org.example.entity.User;
import org.example.mapper.UserMapper;
import org.example.service.UserDTOService;
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

    private final UserDTOService userDTOService;
    private final UserMapper userMapper;  // MapStruct generated mapper

    public UserDTOController(UserDTOService userDTOService, UserMapper userMapper) {
        this.userDTOService = userDTOService;
        this.userMapper = userMapper;
    }
    
    /**
     * GET /api/v2/users - Get all users as DTOs
     * This avoids JSON recursion issues and provides cleaner responses
     */
    @GetMapping
    public List<UserDTO> getAllUsers() {
        List<UserDTO> users = userDTOService.getAllUsers();
        return users;
    }

  
}
