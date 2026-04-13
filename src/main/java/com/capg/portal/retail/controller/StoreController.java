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

    // Constructor Injection (Replaces Lombok's @RequiredArgsConstructor)
    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    // 1. GET Request: Get all stores
    @GetMapping
    public ResponseEntity<List<Store>> getAllStores() {
        return new ResponseEntity<>(storeService.getAllStores(), HttpStatus.OK); // 200 OK
    }

    // 2. GET Request: Get store by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getStoreById(@PathVariable("id") String id) {
        try {
            Store store = storeService.getStoreById(id);
            return new ResponseEntity<>(store, HttpStatus.OK); // 200 OK
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND); // 404 Not Found
        }
    }

    // 3. POST Request: Create a new store
    @PostMapping
    public ResponseEntity<?> createStore(@Valid @RequestBody Store store, BindingResult result) {
        
        // 1. Validation Check
        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST); // 400 Bad Request
        }

        // Clean up empty strings before saving
        if (store.getState() != null && store.getState().trim().isEmpty()) {
            store.setState(null);
        }
        if (store.getZip() != null && store.getZip().trim().isEmpty()) {
            store.setZip(null);
        }

        // 2. Save to Database
        try {
            Store savedStore = storeService.createStore(store);
            return new ResponseEntity<>(savedStore, HttpStatus.CREATED); // 201 Created
        } catch (Exception e) {
            return new ResponseEntity<>("Database Error: Store ID already exists or violates constraints.", HttpStatus.CONFLICT); // 409 Conflict
        }
    }

    // 4. PUT Request: Update an existing store
    @PutMapping("/{id}")
    public ResponseEntity<?> updateStore(@PathVariable("id") String id, @Valid @RequestBody Store store, BindingResult result) {
        
        // 1. Validation Check
        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST); // 400 Bad Request
        }

        // Clean up empty strings before saving
        if (store.getState() != null && store.getState().trim().isEmpty()) {
            store.setState(null);
        }
        if (store.getZip() != null && store.getZip().trim().isEmpty()) {
            store.setZip(null);
        }

        // 2. Update Database
        try {
            Store updatedStore = storeService.updateStore(id, store);
            return new ResponseEntity<>(updatedStore, HttpStatus.OK); // 200 OK
        } catch (Exception e) {
            return new ResponseEntity<>("Database Error: Could not update store.", HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error
        }
    }

    // 5. GET Request: Filter by City
    @GetMapping("/filter/city")
    public ResponseEntity<List<Store>> filterStoresByCity(@RequestParam("name") String city) {
        return new ResponseEntity<>(storeService.getStoresByCity(city), HttpStatus.OK); // 200 OK
    }

    // 6. GET Request: Filter by State
    @GetMapping("/filter/state")
    public ResponseEntity<List<Store>> filterStoresByState(@RequestParam("code") String state) {
        return new ResponseEntity<>(storeService.getStoresByState(state), HttpStatus.OK); // 200 OK
    }
}