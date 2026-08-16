package org.example.service;

import org.example.dto.UserDTO;
import org.example.mapper.UserMapper;
import org.example.repository.BagRepository;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import org.example.entity.User;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Layer - contains business logic
 * 
 * @Service marks this class as a service component
 * Spring will automatically create an instance (bean) of this class
 */
@Service
public class UserDTOService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BagRepository bagRepository;

    public UserDTOService(UserRepository userRepository, BagRepository bagRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.bagRepository = bagRepository;
        this.userMapper = userMapper;
    }
  
    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        if (!users.isEmpty()) {
            bagRepository.findWithItemsByUsers(users);
        }
        return userMapper.toDTOList(users);
    }


   
}
