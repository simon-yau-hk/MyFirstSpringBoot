package org.example.service;

import org.example.dto.BagDTO;
import org.example.dto.UserDTO;
import org.example.mapper.BagMapper;
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
public class BagDTOService {
    private final BagMapper bagMapper;
    private final BagRepository bagRepository;

    public BagDTOService( BagRepository bagRepository, BagMapper bagMapper) {
       
        this.bagRepository = bagRepository;
        this.bagMapper = bagMapper;
    }
  
    @Transactional(readOnly = true)
    public List<BagDTO> getBagsByUserId(Long userId) {
        var bags = bagRepository.findByUserId(userId);
      
        return bagMapper.toDTOList(bags);
    }


   
}
