package org.example.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * BagItem DTO - Data Transfer Object for API responses
 */
public class BagItemDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal totalPrice; // price * quantity
    
    // Constructors
    public BagItemDTO() {}
    
    public BagItemDTO(Long id, String name, BigDecimal price, Integer quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
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
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
 
    
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }
    
    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
    
    @Override
    public String toString() {
        return "BagItemDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
