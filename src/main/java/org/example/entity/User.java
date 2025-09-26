package org.example.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String name;
    
    @Column(nullable = false, unique = true, length = 100)
    private String email;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    // One user can have many bags
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonManagedReference("user-bags")
    private List<Bag> bags = new ArrayList<>();
    
    // Default constructor
    public User() {
        this.createdAt = LocalDateTime.now();
    }
    
    // Constructor with parameters
    public User(String name, String email) {
        this();
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
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public List<Bag> getBags() {
        return bags;
    }
    
    public void setBags(List<Bag> bags) {
        this.bags = bags;
    }
    
    // Helper methods for managing bidirectional relationships
    public void addBag(Bag bag) {
        bags.add(bag);
        bag.setUser(this);
    }
    
    public void removeBag(Bag bag) {
        bags.remove(bag);
        bag.setUser(null);
    }
    
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}