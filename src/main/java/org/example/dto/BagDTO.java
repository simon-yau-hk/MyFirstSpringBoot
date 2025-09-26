package org.example.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Bag DTO - Data Transfer Object for API responses
 */
public class BagDTO {
    private Long id;
    private String name;
    private String description;
    private List<BagItemDTO> items;
    private int itemCount;
    private BigDecimal totalPrice;
    
    // Constructors
    public BagDTO() {}
    
    public BagDTO(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
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
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    

    
    public List<BagItemDTO> getItems() {
        return items;
    }
    
    public void setItems(List<BagItemDTO> items) {
        this.items = items;
    }
    
    public int getItemCount() {
        return itemCount;
    }
    
    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }
    
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }
    
    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
    
    @Override
    public String toString() {
        return "BagDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", itemCount=" + itemCount +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
