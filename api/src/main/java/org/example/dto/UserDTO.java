package org.example.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * User DTO - Data Transfer Object for API responses
 * This excludes sensitive data and controls what gets serialized
 */
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private List<BagDTO> bags;
    private int bagCount;
    
    // Constructors
    public UserDTO() {}
    
    public UserDTO(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
    
    // Getters and setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
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
    

    
    public List<BagDTO> getBags() {
        return bags;
    }
    
    public void setBags(List<BagDTO> bags) {
        this.bags = bags;
    }
    
    public int getBagCount() {
        return bagCount;
    }
    
    public void setBagCount(int bagCount) {
        this.bagCount = bagCount;
    }
    
    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", bagCount=" + bagCount +
                '}';
    }
}
