package com.capg.portal.finance.service;

import com.capg.portal.finance.entity.Discount;
import com.capg.portal.finance.repository.DiscountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DiscountService {

    private final DiscountRepository discountRepository;

    // Constructor Injection
    public DiscountService(DiscountRepository discountRepository) {
        this.discountRepository = discountRepository;
    }

    public Discount createDiscount(Discount discount) {
        return discountRepository.save(discount);
    }

    public List<Discount> getAllDiscounts() {
        return discountRepository.findAll();
    }

    public Discount getDiscountByType(String type) {
        return discountRepository.findById(type)
                .orElseThrow(() -> new RuntimeException("Discount not found with type: " + type));
    }

    public Discount updateDiscount(String type, Discount discountDetails) {
        Discount existingDiscount = getDiscountByType(type);
        existingDiscount.setStore(discountDetails.getStore());
        existingDiscount.setLowQty(discountDetails.getLowQty());
        existingDiscount.setHighQty(discountDetails.getHighQty());
        existingDiscount.setDiscountAmount(discountDetails.getDiscountAmount());
        return discountRepository.save(existingDiscount);
    }
    
    public List<Discount> getDiscountsByQuantityRange(Integer minQty, Integer maxQty) {
        return discountRepository.findByLowQtyGreaterThanEqualAndHighQtyLessThanEqual(minQty, maxQty);
    }

    // New Method: Get discounts less than a specific amount
    public List<Discount> getDiscountsLessThanAmount(BigDecimal maxDiscount) {
        return discountRepository.findByDiscountAmountLessThan(maxDiscount);
    }
    
    // New Method: Get discounts by Store ID
    public List<Discount> getDiscountsByStoreId(String storId) {
        return discountRepository.findByStoreStorId(storId);
    }
}