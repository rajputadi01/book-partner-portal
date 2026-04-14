package com.capg.portal.finance.controller;

import com.capg.portal.finance.entity.Discount;
import com.capg.portal.retail.entity.Store;
import com.capg.portal.finance.service.DiscountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/discounts")
public class DiscountController 
{
    private final DiscountService discountService;

    public DiscountController(DiscountService discountService) 
    {
        this.discountService = discountService;
    }

    @GetMapping
    public ResponseEntity<List<Discount>> getAllDiscounts() 
    {
        return new ResponseEntity<>(discountService.getAllDiscounts(), HttpStatus.OK);
    }

    @GetMapping("/{type}")
    public ResponseEntity<Discount> getDiscountByType(@PathVariable("type") String type) 
    {
        Discount discount = discountService.getDiscountByType(type);
        return new ResponseEntity<>(discount, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Discount> createDiscount(@Valid @RequestBody Discount discount) 
    {
        Discount savedDiscount = discountService.createDiscount(discount);
        return new ResponseEntity<>(savedDiscount, HttpStatus.CREATED);
    }

    @PutMapping("/{type}")
    public ResponseEntity<Discount> updateDiscount(@PathVariable("type") String type, @Valid @RequestBody Discount discount) 
    {
        Discount updatedDiscount = discountService.updateDiscount(type, discount);
        return new ResponseEntity<>(updatedDiscount, HttpStatus.OK);
    }

    @PatchMapping("/{type}")
    public ResponseEntity<Discount> patchDiscount(@PathVariable("type") String type, @RequestBody Discount updates) 
    {
        Discount patchedDiscount = discountService.patchDiscount(type, updates);
        return new ResponseEntity<>(patchedDiscount, HttpStatus.OK);
    }

    @GetMapping("/filter/qty")
    public ResponseEntity<List<Discount>> filterDiscountsByQty(@RequestParam("minQty") Integer minQty, @RequestParam("maxQty") Integer maxQty) 
    {
        return new ResponseEntity<>(discountService.getDiscountsByQuantityRange(minQty, maxQty), HttpStatus.OK);
    }

    @GetMapping("/filter/amount")
    public ResponseEntity<List<Discount>> filterDiscountsByAmount(@RequestParam("maxAmount") BigDecimal maxAmount) 
    {
        return new ResponseEntity<>(discountService.getDiscountsLessThanAmount(maxAmount), HttpStatus.OK);
    }

    @GetMapping("/filter/store")
    public ResponseEntity<List<Discount>> filterDiscountsByStore(@RequestParam("storId") String storId) 
    {
        return new ResponseEntity<>(discountService.getDiscountsByStoreId(storId), HttpStatus.OK);
    }

    @GetMapping("/{type}/store")
    public ResponseEntity<Store> getStoreByDiscountType(@PathVariable("type") String type) 
    {
        Discount discount = discountService.getDiscountByType(type);
        Store store = discount.getStore();
        
        if (store == null) 
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        
        return new ResponseEntity<>(store, HttpStatus.OK);
    }
}