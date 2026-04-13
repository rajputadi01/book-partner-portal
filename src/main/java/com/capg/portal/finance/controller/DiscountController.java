package com.capg.portal.finance.controller;

import com.capg.portal.finance.entity.Discount;
import com.capg.portal.finance.service.DiscountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/discounts")
public class DiscountController {

    private final DiscountService discountService;

    // Constructor Injection
    public DiscountController(DiscountService discountService) {
        this.discountService = discountService;
    }

    // 1. GET Request: Get all discounts
    @GetMapping
    public ResponseEntity<List<Discount>> getAllDiscounts() {
        return new ResponseEntity<>(discountService.getAllDiscounts(), HttpStatus.OK); // 200 OK
    }

    // 2. GET Request: Get discount by type
    @GetMapping("/{type}")
    public ResponseEntity<?> getDiscountByType(@PathVariable("type") String type) {
        try {
            Discount discount = discountService.getDiscountByType(type);
            return new ResponseEntity<>(discount, HttpStatus.OK); // 200 OK
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND); // 404 Not Found
        }
    }

    // 3. POST Request: Create a new discount
    @PostMapping
    public ResponseEntity<?> createDiscount(@Valid @RequestBody Discount discount, BindingResult result) {
        
        // 1. Validation Check
        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST); // 400 Bad Request
        }

        // 2. Save to Database
        try {
            Discount savedDiscount = discountService.createDiscount(discount);
            return new ResponseEntity<>(savedDiscount, HttpStatus.CREATED); // 201 Created
        } catch (Exception e) {
            return new ResponseEntity<>("Database Error: Discount Type already exists.", HttpStatus.CONFLICT); // 409 Conflict
        }
    }

    // 4. PUT Request: Update an existing discount
    @PutMapping("/{type}")
    public ResponseEntity<?> updateDiscount(@PathVariable("type") String type, @Valid @RequestBody Discount discount, BindingResult result) {
        
        // 1. Validation Check
        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST); // 400 Bad Request
        }

        // 2. Update Database
        try {
            Discount updatedDiscount = discountService.updateDiscount(type, discount);
            return new ResponseEntity<>(updatedDiscount, HttpStatus.OK); // 200 OK
        } catch (Exception e) {
            return new ResponseEntity<>("Database Error: Could not update discount.", HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error
        }
    }

    // 5. GET Request: Filter by Quantity Range
    @GetMapping("/filter/qty")
    public ResponseEntity<List<Discount>> filterDiscountsByQty(
            @RequestParam("minQty") Integer minQty,
            @RequestParam("maxQty") Integer maxQty) {
        return new ResponseEntity<>(discountService.getDiscountsByQuantityRange(minQty, maxQty), HttpStatus.OK); // 200 OK
    }

    // 6. GET Request: Filter by Maximum Discount Amount
    @GetMapping("/filter/amount")
    public ResponseEntity<List<Discount>> filterDiscountsByAmount(@RequestParam("maxAmount") BigDecimal maxAmount) {
        return new ResponseEntity<>(discountService.getDiscountsLessThanAmount(maxAmount), HttpStatus.OK); // 200 OK
    }
    
    // 7. GET Request: Filter by Store ID
    @GetMapping("/filter/store")
    public ResponseEntity<List<Discount>> filterDiscountsByStore(@RequestParam("storId") String storId) {
        return new ResponseEntity<>(discountService.getDiscountsByStoreId(storId), HttpStatus.OK); // 200 OK
    }
}