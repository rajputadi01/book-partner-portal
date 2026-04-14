package com.capg.portal.finance.controller;

import com.capg.portal.finance.entity.RoyaltySchedule;
import com.capg.portal.finance.service.RoyaltyScheduleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/roysched")
public class RoyaltyScheduleController {

    private final RoyaltyScheduleService royaltyScheduleService;

    // Constructor Injection
    public RoyaltyScheduleController(RoyaltyScheduleService royaltyScheduleService) {
        this.royaltyScheduleService = royaltyScheduleService;
    }

    // 1. GET Request: Get all schedules
    @GetMapping
    public ResponseEntity<List<RoyaltySchedule>> getAllRoyaltySchedules() {
        return new ResponseEntity<>(royaltyScheduleService.getAllRoyaltySchedules(), HttpStatus.OK); // 200 OK
    }

    // 2. GET Request: Get schedule by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getRoyaltyScheduleById(@PathVariable("id") Integer id) {
        try {
            RoyaltySchedule schedule = royaltyScheduleService.getRoyaltyScheduleById(id);
            return new ResponseEntity<>(schedule, HttpStatus.OK); // 200 OK
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND); // 404 Not Found
        }
    }

    // 3. POST Request: Create a new schedule
    @PostMapping
    public ResponseEntity<?> createRoyaltySchedule(@Valid @RequestBody RoyaltySchedule royaltySchedule, BindingResult result) {
        
        // 1. Validation Check
        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST); // 400 Bad Request
        }

        // 2. Save to Database
        try {
            RoyaltySchedule savedSchedule = royaltyScheduleService.createRoyaltySchedule(royaltySchedule);
            return new ResponseEntity<>(savedSchedule, HttpStatus.CREATED); // 201 Created
        } catch (Exception e) {
            return new ResponseEntity<>("Database Error: Could not save schedule.", HttpStatus.INTERNAL_SERVER_ERROR); // 500 Error
        }
    }

    // 4. PUT Request: Update an existing schedule
    @PutMapping("/{id}")
    public ResponseEntity<?> updateRoyaltySchedule(@PathVariable("id") Integer id, @Valid @RequestBody RoyaltySchedule royaltySchedule, BindingResult result) {
        
        // 1. Validation Check
        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST); // 400 Bad Request
        }

        // 2. Update Database
        try {
            RoyaltySchedule updatedSchedule = royaltyScheduleService.updateRoyaltySchedule(id, royaltySchedule);
            return new ResponseEntity<>(updatedSchedule, HttpStatus.OK); // 200 OK
        } catch (Exception e) {
            return new ResponseEntity<>("Database Error: Could not update schedule.", HttpStatus.INTERNAL_SERVER_ERROR); // 500 Error
        }
    }

    // 5. GET Request: Filter by Range
    @GetMapping("/filter/range")
    public ResponseEntity<List<RoyaltySchedule>> filterRoyaltyByRange(
            @RequestParam("minLorange") Integer minLorange,
            @RequestParam("maxHirange") Integer maxHirange) {
        return new ResponseEntity<>(royaltyScheduleService.getRoyaltySchedulesByRange(minLorange, maxHirange), HttpStatus.OK); // 200 OK
    }

    // 6. GET Request: Filter by Title ID
    @GetMapping("/filter/title")
    public ResponseEntity<List<RoyaltySchedule>> filterRoyaltyByTitle(@RequestParam("titleId") String titleId) {
        return new ResponseEntity<>(royaltyScheduleService.getRoyaltySchedulesByTitleId(titleId), HttpStatus.OK); // 200 OK
    }
    
    @PatchMapping("/{id}")
    public ResponseEntity<?> patchRoyaltySchedule(
            @PathVariable("id") Integer id,
            @RequestBody RoyaltySchedule updates) {

        try {
            RoyaltySchedule updatedSchedule = royaltyScheduleService.patchRoyaltySchedule(id, updates);
            return new ResponseEntity<>(updatedSchedule, HttpStatus.OK); // 200 OK
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST); 
        } catch (Exception e) {
            return new ResponseEntity<>("Database Error: Could not partially update schedule.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}