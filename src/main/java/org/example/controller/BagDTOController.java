package org.example.controller;

import org.example.dto.BagDTO;
import org.example.dto.UserDTO;
import org.example.mapper.UserMapper;
import org.example.service.BagDTOService;
import org.example.service.UserDTOService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * UserDTO Controller - demonstrates MapStruct usage
 * This controller uses DTOs instead of entities for cleaner API responses
 */
@RestController
@RequestMapping("/api/v2/bags")  // v2 to differentiate from original controller
public class BagDTOController {

    private final BagDTOService bagDTOService;
    public BagDTOController(BagDTOService bagDTOService) {
        this.bagDTOService = bagDTOService;
    }
    
    /**
     * GET /api/v2/users - Get all users as DTOs
     * This avoids JSON recursion issues and provides cleaner responses
     */
    @GetMapping
    public List<BagDTO> getBagsByUserId(long Id) {
        return bagDTOService.getBagsByUserId(Id);
    }

  
}
