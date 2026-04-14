package com.capg.portal.retail.controller;

import com.capg.portal.retail.entity.Store;
import com.capg.portal.retail.service.StoreService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/stores")
public class StoreController {

    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }
    
    @GetMapping
    public ResponseEntity<List<Store>> getAllStores() {
        return new ResponseEntity<>(storeService.getAllStores(), HttpStatus.OK); 
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStoreById(@PathVariable("id") String id) {
        try {
            Store store = storeService.getStoreById(id);
            return new ResponseEntity<>(store, HttpStatus.OK); 
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND); 
        }
    }

    @PostMapping
    public ResponseEntity<?> createStore(@Valid @RequestBody Store store, BindingResult result) {
        
        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST); 
        }

        if (store.getState() != null && store.getState().trim().isEmpty()) {
            store.setState(null);
        }
        if (store.getZip() != null && store.getZip().trim().isEmpty()) {
            store.setZip(null);
        }

        try {
            Store savedStore = storeService.createStore(store);
            return new ResponseEntity<>(savedStore, HttpStatus.CREATED); 
        } catch (Exception e) {
            return new ResponseEntity<>("Database Error: Store ID already exists or violates constraints.", HttpStatus.CONFLICT); 
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStore(@PathVariable("id") String id, @Valid @RequestBody Store store, BindingResult result) {
        
        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST); 
        }

        if (store.getState() != null && store.getState().trim().isEmpty()) {
            store.setState(null);
        }
        if (store.getZip() != null && store.getZip().trim().isEmpty()) {
            store.setZip(null);
        }

        try {
            Store updatedStore = storeService.updateStore(id, store);
            return new ResponseEntity<>(updatedStore, HttpStatus.OK); 
        } catch (Exception e) {
            return new ResponseEntity<>("Database Error: Could not update store.", HttpStatus.INTERNAL_SERVER_ERROR); 
        }
    }

    @GetMapping("/filter/city")
    public ResponseEntity<List<Store>> filterStoresByCity(@RequestParam("name") String city) {
        return new ResponseEntity<>(storeService.getStoresByCity(city), HttpStatus.OK); 
    }

    @GetMapping("/filter/state")
    public ResponseEntity<List<Store>> filterStoresByState(@RequestParam("code") String state) {
        return new ResponseEntity<>(storeService.getStoresByState(state), HttpStatus.OK); 
    }
}