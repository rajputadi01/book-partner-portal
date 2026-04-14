package com.capg.portal.retail.controller;

import com.capg.portal.retail.entity.Sales;
import com.capg.portal.retail.entity.SalesId;
import com.capg.portal.retail.service.SalesService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/sales")
public class SalesController 
{
    private final SalesService salesService;

    public SalesController(SalesService salesService) 
    {
        this.salesService = salesService;
    }

    @GetMapping
    public ResponseEntity<List<Sales>> getAllSales() 
    {
        return new ResponseEntity<>(salesService.getAllSales(), HttpStatus.OK); 
    }

    @GetMapping("/{storId}/{ordNum}/{titleId}")
    public ResponseEntity<Sales> getSaleById(
            @PathVariable("storId") String storId, 
            @PathVariable("ordNum") String ordNum, 
            @PathVariable("titleId") String titleId) 
    {
        SalesId id = new SalesId(storId, ordNum, titleId);
        return new ResponseEntity<>(salesService.getSaleById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Sales> createSale(@Valid @RequestBody Sales sale) 
    {
        return new ResponseEntity<>(salesService.createSale(sale), HttpStatus.CREATED); 
    }

    @PutMapping("/{storId}/{ordNum}/{titleId}")
    public ResponseEntity<Sales> updateSale(
            @PathVariable("storId") String storId, 
            @PathVariable("ordNum") String ordNum, 
            @PathVariable("titleId") String titleId, 
            @Valid @RequestBody Sales sale) 
    {
        SalesId id = new SalesId(storId, ordNum, titleId);
        return new ResponseEntity<>(salesService.updateSale(id, sale), HttpStatus.OK); 
    }

    @PatchMapping("/{storId}/{ordNum}/{titleId}")
    public ResponseEntity<Sales> patchSale(
            @PathVariable("storId") String storId, 
            @PathVariable("ordNum") String ordNum, 
            @PathVariable("titleId") String titleId, 
            @RequestBody Sales updates) 
    {
        SalesId id = new SalesId(storId, ordNum, titleId);
        return new ResponseEntity<>(salesService.patchSale(id, updates), HttpStatus.OK);
    }

    @GetMapping("/filter/store")
    public ResponseEntity<List<Sales>> filterSalesByStore(@RequestParam("storId") String storId) 
    {
        return new ResponseEntity<>(salesService.getSalesByStoreId(storId), HttpStatus.OK); 
    }

    @GetMapping("/filter/title")
    public ResponseEntity<List<Sales>> filterSalesByTitle(@RequestParam("titleId") String titleId) 
    {
        return new ResponseEntity<>(salesService.getSalesByTitleId(titleId), HttpStatus.OK); 
    }
    
    @GetMapping("/store/{storId}/totalqty")
    public ResponseEntity<Integer> getTotalQtyByStore(@PathVariable String storId) {
        return new ResponseEntity<>(
                salesService.getTotalQtyByStore(storId),
                HttpStatus.OK
        );
    }
    
    @GetMapping("/store/{storId}/count")
    public ResponseEntity<Long> getTransactionCountByStore(@PathVariable("storId") String storId) 
    {
        return new ResponseEntity<>(salesService.getTransactionCountByStore(storId), HttpStatus.OK);
    }
    
    
    @GetMapping("/title/{titleId}/totalqty")
    public ResponseEntity<Integer> getTotalQtyByTitle(@PathVariable String titleId) {
        return new ResponseEntity<>(
                salesService.getTotalQtyByTitle(titleId),
                HttpStatus.OK
        );
    }
    
    @PatchMapping("/{storId}/{ordNum}/{titleId}")
    public ResponseEntity<?> patchSale(
            @PathVariable String storId,
            @PathVariable String ordNum,
            @PathVariable String titleId,
            @RequestBody Sales updates) {

        try {
            SalesId id = new SalesId(storId, ordNum, titleId);
            Sales updated = salesService.patchSale(id, updates);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/filter/payterms")
    public ResponseEntity<List<Sales>> filterByPayterms(@RequestParam("terms") String terms) {
        return new ResponseEntity<>(
                salesService.getSalesByPayterms(terms),
                HttpStatus.OK
        );
    }
}