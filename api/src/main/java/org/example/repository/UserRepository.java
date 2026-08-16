package org.example.repository;

import org.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Spring Data JPA automatically implements basic CRUD operations
    // Additional custom methods:
    @EntityGraph(attributePaths = {"bags"})
    @Override
    @NonNull
    List<User> findAll();

    @EntityGraph(attributePaths = {"bags"})
    @Override
    @NonNull
    Optional<User> findById(@NonNull Long id);
    
    
    @EntityGraph(attributePaths = {"bags"})
    Optional<User> findByEmail(String email);
    
    @Query("""
    SELECT DISTINCT u FROM User u
    LEFT JOIN FETCH u.bags b
    LEFT JOIN FETCH b.items
    WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%'))
    """
    )
    List<User> findByNameContainingIgnoreCase(String name);
    
    @Query("""
    SELECT DISTINCT u FROM User u
    LEFT JOIN FETCH u.bags b
    LEFT JOIN FETCH b.items
    WHERE u.name LIKE %:keyword% OR u.email LIKE %:keyword%
    """
    )
    List<User> searchUsers(String keyword);
    
    long countByNameContaining(String name);
}