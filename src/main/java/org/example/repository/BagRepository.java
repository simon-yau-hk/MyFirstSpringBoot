package org.example.repository;

import org.example.entity.Bag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BagRepository extends JpaRepository<Bag, Long> {
    
    // Find all bags for a specific user
    List<Bag> findByUserId(Long userId);
    
    // Find bags by name (case-insensitive)
    List<Bag> findByNameContainingIgnoreCase(String name);
    
    // Find bag with all items loaded (to avoid N+1 problem)
    @Query("SELECT b FROM Bag b LEFT JOIN FETCH b.items WHERE b.id = :bagId")
    Optional<Bag> findByIdWithItems(@Param("bagId") Long bagId);
    
    // Find all bags for a user with items loaded
    @Query("SELECT DISTINCT b FROM Bag b LEFT JOIN FETCH b.items WHERE b.user.id = :userId")
    List<Bag> findByUserIdWithItems(@Param("userId") Long userId);
    
    // Count bags for a user
    long countByUserId(Long userId);
    
    // Find bags by user email
    @Query("SELECT b FROM Bag b WHERE b.user.email = :email")
    List<Bag> findByUserEmail(@Param("email") String email);
}
