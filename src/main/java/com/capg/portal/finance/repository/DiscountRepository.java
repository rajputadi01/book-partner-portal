package com.capg.portal.finance.repository;

import com.capg.portal.finance.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, String> 
{
    List<Discount> findByLowQtyGreaterThanEqualAndHighQtyLessThanEqual(Integer minQty, Integer maxQty);
    
    List<Discount> findByDiscountAmountLessThan(BigDecimal maxDiscount);
    
    List<Discount> findByStoreStorId(String storId);
}