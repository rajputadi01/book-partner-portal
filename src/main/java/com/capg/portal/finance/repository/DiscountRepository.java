package com.capg.portal.finance.repository;

import com.capg.portal.finance.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, String> {
    
    List<Discount> findByLowQtyGreaterThanEqualAndHighQtyLessThanEqual(Integer minQty, Integer maxQty);
    
    // New Method: Filter by discount amount less than a specified value
    List<Discount> findByDiscountAmountLessThan(BigDecimal maxDiscount);
    
 // New Method: Get all discounts for a specific store ID
    List<Discount> findByStoreStorId(String storId);
}