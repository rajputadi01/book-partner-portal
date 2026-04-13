package com.capg.portal.retail.controller;

import com.capg.portal.retail.entity.Sales;
import com.capg.portal.retail.entity.SalesId;
import com.capg.portal.retail.service.SalesService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sales")
public class SalesController {

    private final SalesService salesService;

    public SalesController(SalesService salesService) {
        this.salesService = salesService;
    }

    @GetMapping
    public ResponseEntity<List<Sales>> getAllSales() {
        return new ResponseEntity<>(salesService.getAllSales(), HttpStatus.OK); 
    }

    @GetMapping("/{storId}/{ordNum}/{titleId}")
    public ResponseEntity<?> getSaleById(
            @PathVariable("storId") String storId, 
            @PathVariable("ordNum") String ordNum, 
            @PathVariable("titleId") String titleId) {
        try {
            SalesId id = new SalesId(storId, ordNum, titleId);
            Sales sale = salesService.getSaleById(id);
            return new ResponseEntity<>(sale, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND); 
        }
    }

    @PostMapping
    public ResponseEntity<?> createSale(@Valid @RequestBody Sales sale, BindingResult result) {
        
        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST); 
        }

        if (sale.getOrdDate() == null) {
            sale.setOrdDate(LocalDateTime.now());
        }

        try {
            Sales savedSale = salesService.createSale(sale);
            return new ResponseEntity<>(savedSale, HttpStatus.CREATED); 
        } catch (Exception e) {
            return new ResponseEntity<>("Database Error: This Order Number already exists for this Store and Title.", HttpStatus.CONFLICT); 
        }
    }

    @PutMapping("/{storId}/{ordNum}/{titleId}")
    public ResponseEntity<?> updateSale(
            @PathVariable("storId") String storId, 
            @PathVariable("ordNum") String ordNum, 
            @PathVariable("titleId") String titleId, 
            @Valid @RequestBody Sales sale, 
            BindingResult result) {
        
        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST); 
        }

        if (sale.getOrdDate() == null) {
            sale.setOrdDate(LocalDateTime.now());
        }

        try {
            SalesId id = new SalesId(storId, ordNum, titleId);
            Sales updatedSale = salesService.updateSale(id, sale);
            return new ResponseEntity<>(updatedSale, HttpStatus.OK); 
        } catch (Exception e) {
            return new ResponseEntity<>("Database Error: Could not update the transaction.", HttpStatus.INTERNAL_SERVER_ERROR); 
        }
    }

    @GetMapping("/filter/store")
    public ResponseEntity<List<Sales>> filterSalesByStore(@RequestParam("storId") String storId) {
        return new ResponseEntity<>(salesService.getSalesByStoreId(storId), HttpStatus.OK); 
    }

    @GetMapping("/filter/title")
    public ResponseEntity<List<Sales>> filterSalesByTitle(@RequestParam("titleId") String titleId) {
        return new ResponseEntity<>(salesService.getSalesByTitleId(titleId), HttpStatus.OK); 
    }
}