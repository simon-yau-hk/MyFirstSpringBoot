package org.example.controller;

import org.example.dto.BagDTO;
import org.example.service.BagDTOService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * UserDTO Controller - demonstrates MapStruct usage
 * This controller uses DTOs instead of entities for cleaner API responses
 */
@PreAuthorize("isAuthenticated()")
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
