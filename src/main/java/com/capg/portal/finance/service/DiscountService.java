package com.capg.portal.finance.service;

import com.capg.portal.finance.entity.Discount;
import com.capg.portal.retail.entity.Store;
import com.capg.portal.finance.repository.DiscountRepository;
import com.capg.portal.exception.ResourceAlreadyExistsException;
import com.capg.portal.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
public class DiscountService 
{
    private final DiscountRepository discountRepository;

    public DiscountService(DiscountRepository discountRepository) 
    {
        this.discountRepository = discountRepository;
    }

    public Discount createDiscount(Discount discount) 
    {
        if (discountRepository.existsById(discount.getDiscountType())) 
        {
            throw new ResourceAlreadyExistsException("Discount with type '" + discount.getDiscountType() + "' already exists.");
        }
        return discountRepository.save(discount);
    }

    public List<Discount> getAllDiscounts() 
    {
        return discountRepository.findAll();
    }

    public Discount getDiscountByType(String type) 
    {
        return discountRepository.findById(type)
                .orElseThrow(() -> new ResourceNotFoundException("Discount not found with type: " + type));
    }

    public Discount updateDiscount(String type, Discount discountDetails) 
    {
        Discount existingDiscount = getDiscountByType(type);
        
        // This safely handles nulls if the admin wants to change a Store-Specific discount to a Global one
        existingDiscount.setStore(discountDetails.getStore());
        existingDiscount.setLowQty(discountDetails.getLowQty());
        existingDiscount.setHighQty(discountDetails.getHighQty());
        existingDiscount.setDiscountAmount(discountDetails.getDiscountAmount());
        
        return discountRepository.save(existingDiscount);
    }

    public Discount patchDiscount(String type, Discount updates) 
    {
        Discount existingDiscount = getDiscountByType(type);

        if (updates.getStore() != null) existingDiscount.setStore(updates.getStore());
        if (updates.getLowQty() != null) existingDiscount.setLowQty(updates.getLowQty());
        if (updates.getHighQty() != null) existingDiscount.setHighQty(updates.getHighQty());
        if (updates.getDiscountAmount() != null) existingDiscount.setDiscountAmount(updates.getDiscountAmount());

        return discountRepository.save(existingDiscount);
    }

    public List<Discount> getDiscountsByQuantityRange(Integer minQty, Integer maxQty) 
    {
        return discountRepository.findByLowQtyGreaterThanEqualAndHighQtyLessThanEqual(minQty, maxQty);
    }

    public List<Discount> getDiscountsLessThanAmount(BigDecimal maxDiscount) 
    {
        return discountRepository.findByDiscountAmountLessThan(maxDiscount);
    }

    public List<Discount> getDiscountsByStoreId(String storId) 
    {
        return discountRepository.findByStoreStorId(storId);
    }
}