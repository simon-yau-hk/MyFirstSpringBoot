package org.example.repository;

import org.example.entity.BagItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface BagItemRepository extends JpaRepository<BagItem, Long> {
    
    // Find all items in a specific bag
    List<BagItem> findByBagId(Long bagId);
    
    // Find items by name (case-insensitive)
    List<BagItem> findByNameContainingIgnoreCase(String name);
    
    // Find items by price range
    List<BagItem> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    
    // Find items for a specific user (across all their bags)
    @Query("SELECT i FROM BagItem i WHERE i.bag.user.id = :userId")
    List<BagItem> findByUserId(@Param("userId") Long userId);
    
    // Count items in a bag
    long countByBagId(Long bagId);
    
    // Calculate total value of items in a bag
    @Query("SELECT SUM(i.price * i.quantity) FROM BagItem i WHERE i.bag.id = :bagId")
    BigDecimal calculateTotalValueByBagId(@Param("bagId") Long bagId);
    
    // Find expensive items (price above threshold)
    List<BagItem> findByPriceGreaterThan(BigDecimal minPrice);
    
    // Find items by quantity
    List<BagItem> findByQuantityGreaterThan(Integer minQuantity);
    
    // Search items by name or description
    @Query("SELECT i FROM BagItem i WHERE i.name LIKE %:keyword% OR i.description LIKE %:keyword%")
    List<BagItem> searchItems(@Param("keyword") String keyword);
}
