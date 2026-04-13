package com.capg.portal.catalog.controller;

import com.capg.portal.catalog.entity.Title;
import com.capg.portal.catalog.entity.TitleAuthor;
import com.capg.portal.catalog.service.TitleService;
import com.capg.portal.finance.entity.RoyaltySchedule;
import com.capg.portal.retail.entity.Sales;

import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/titles")
public class TitleController {

    private final TitleService titleService;

    public TitleController(TitleService titleService) 
    {
        this.titleService = titleService;
    }

    // 1. GET Request: Get all titles
    @GetMapping
    public ResponseEntity<List<Title>> getAllTitles() 
    {
        return new ResponseEntity<>(titleService.getAllTitles(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTitleById(@PathVariable("id") String id) {
        try {
            Title title = titleService.getTitleById(id);
            return new ResponseEntity<>(title, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // 3. POST Request: Create a new title
    @PostMapping
    public ResponseEntity<?> createTitle(@Valid @RequestBody Title title, BindingResult result) {
        
        // 1. Validation Check
        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
        
        // Cleanup empty strings & Failsafe date
        if (title.getType() == null || title.getType().trim().isEmpty()) {
            title.setType("UNDECIDED");
        }
        if (title.getPubdate() == null) {
            title.setPubdate(LocalDateTime.now());
        }

        // 2. Save to Database
        try {
            Title savedTitle = titleService.createTitle(title);
            return new ResponseEntity<>(savedTitle, HttpStatus.CREATED); // 201 Created
        } catch (Exception e) {
            return new ResponseEntity<>("Database Error: Title ID may already exist or violates constraints.", HttpStatus.CONFLICT); // 409 Conflict
        }
    }

    // 4. PUT Request: Update an existing title
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTitle(@PathVariable("id") String id, @Valid @RequestBody Title title, BindingResult result) {
        
        // 1. Validation Check
        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST); // 400 Bad Request
        }

        // Cleanup empty strings & Failsafe date
        if (title.getType() == null || title.getType().trim().isEmpty()) {
            title.setType("UNDECIDED");
        }
        if (title.getPubdate() == null) {
            title.setPubdate(LocalDateTime.now());
        }

        // 2. Update Database
        try {
            Title updatedTitle = titleService.updateTitle(id, title);
            return new ResponseEntity<>(updatedTitle, HttpStatus.OK); // 200 OK
        } catch (Exception e) {
            return new ResponseEntity<>("Database Error: Could not update title.", HttpStatus.INTERNAL_SERVER_ERROR); // 500 Server Error
        }
    }

    // 5. GET Request: Filter by Max Price
    @GetMapping("/filter/price")
    public ResponseEntity<List<Title>> filterTitlesByPrice(@RequestParam("maxPrice") Double maxPrice) {
        return new ResponseEntity<>(titleService.getTitlesByPriceLessThan(maxPrice), HttpStatus.OK); // 200 OK
    }

    // 6. GET Request: Filter by Type
    @GetMapping("/filter/type")
    public ResponseEntity<List<Title>> filterTitlesByType(@RequestParam("type") String type) {
        return new ResponseEntity<>(titleService.getTitlesByType(type), HttpStatus.OK); // 200 OK
    }

    // 7. GET Request: Filter by Publisher ID
    @GetMapping("/filter/publisher")
    public ResponseEntity<List<Title>> filterTitlesByPublisher(@RequestParam("pubId") String pubId) {
        return new ResponseEntity<>(titleService.getTitlesByPublisher(pubId), HttpStatus.OK); // 200 OK
    }

    // 8. GET Request: Filter by Publish Date Before
    // Example Postman URL: /filter/date?beforeDate=2024-01-01T00:00:00
    @GetMapping("/filter/date")
    public ResponseEntity<List<Title>> filterTitlesByDateBefore(
            @RequestParam("beforeDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime beforeDate) {
        return new ResponseEntity<>(titleService.getTitlesPublishedBefore(beforeDate), HttpStatus.OK); // 200 OK
    }
    
 // 1. Get all sales for a specific title
    @GetMapping("/{id}/sales")
    public ResponseEntity<List<Sales>> getTitleSales(@PathVariable String id) {
        return ResponseEntity.ok(titleService.getSalesByTitle(id));
    }

    // 2. Get all royalty schedules for a specific title
    @GetMapping("/{id}/royalties")
    public ResponseEntity<List<RoyaltySchedule>> getTitleRoyalties(@PathVariable String id) {
        return ResponseEntity.ok(titleService.getRoyaltiesByTitle(id));
    }

    // 3. Get all authors mapped to this title
    @GetMapping("/{id}/authors")
    public ResponseEntity<List<TitleAuthor>> getTitleAuthors(@PathVariable String id) {
        return ResponseEntity.ok(titleService.getAuthorsByTitle(id));
    }
}