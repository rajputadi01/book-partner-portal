package com.capg.portal.retail.controller;

import com.capg.portal.retail.entity.Store;
import com.capg.portal.retail.entity.Sales;
import com.capg.portal.catalog.entity.Title;
import com.capg.portal.creator.entity.Publisher;
import com.capg.portal.finance.entity.Discount;
import com.capg.portal.creator.entity.Author;
import com.capg.portal.retail.service.StoreService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/stores")
public class StoreController 
{
    private final StoreService storeService;

    public StoreController(StoreService storeService) 
    {
        this.storeService = storeService;
    }

    @GetMapping
    public ResponseEntity<List<Store>> getAllStores() 
    {
        return new ResponseEntity<>(storeService.getAllStores(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Store> getStoreById(@PathVariable("id") String id) 
    {
        Store store = storeService.getStoreById(id);
        return new ResponseEntity<>(store, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Store> createStore(@Valid @RequestBody Store store) 
    {
        Store savedStore = storeService.createStore(store);
        return new ResponseEntity<>(savedStore, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Store> updateStore(@PathVariable("id") String id, @Valid @RequestBody Store store) 
    {
        Store updatedStore = storeService.updateStore(id, store);
        return new ResponseEntity<>(updatedStore, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Store> patchStore(@PathVariable("id") String id, @RequestBody Store updates) 
    {
        Store patchedStore = storeService.patchStore(id, updates);
        return new ResponseEntity<>(patchedStore, HttpStatus.OK);
    }

    @GetMapping("/filter/city")
    public ResponseEntity<List<Store>> filterStoresByCity(@RequestParam("city") String city) 
    {
        return new ResponseEntity<>(storeService.getStoresByCity(city), HttpStatus.OK);
    }

    @GetMapping("/filter/state")
    public ResponseEntity<List<Store>> filterStoresByState(@RequestParam("state") String state) 
    {
        return new ResponseEntity<>(storeService.getStoresByState(state), HttpStatus.OK);
    }

    @GetMapping("/{id}/sales")
    public ResponseEntity<List<Sales>> getSalesByStore(@PathVariable("id") String id) 
    {
        return new ResponseEntity<>(storeService.getSalesByStoreId(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/titles")
    public ResponseEntity<List<Title>> getTitlesByStore(@PathVariable("id") String id) 
    {
        return new ResponseEntity<>(storeService.getTitlesByStoreId(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/publishers")
    public ResponseEntity<List<Publisher>> getPublishersByStore(@PathVariable("id") String id) 
    {
        return new ResponseEntity<>(storeService.getPublishersByStoreId(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/authors")
    public ResponseEntity<List<Author>> getAuthorsByStore(@PathVariable("id") String id) 
    {
        return new ResponseEntity<>(storeService.getAuthorsByStoreId(id), HttpStatus.OK);
    }
    
    @GetMapping("/{id}/discounts")
    public ResponseEntity<List<Discount>> getDiscountsByStore(@PathVariable("id") String id) 
    {
        return new ResponseEntity<>(storeService.getDiscountsByStoreId(id), HttpStatus.OK);
    }
}