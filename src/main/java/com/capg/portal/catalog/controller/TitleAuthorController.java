package com.capg.portal.catalog.controller;

import com.capg.portal.catalog.entity.TitleAuthor;
import com.capg.portal.catalog.entity.TitleAuthorId;
import com.capg.portal.catalog.service.TitleAuthorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/titleauthors")
public class TitleAuthorController {

    private final TitleAuthorService titleAuthorService;

    // Constructor Injection
    public TitleAuthorController(TitleAuthorService titleAuthorService) {
        this.titleAuthorService = titleAuthorService;
    }

    // 1. GET Request: Get all title-author contracts
    @GetMapping
    public ResponseEntity<List<TitleAuthor>> getAllTitleAuthors() {
        return new ResponseEntity<>(titleAuthorService.getAllTitleAuthors(), HttpStatus.OK); // 200 OK
    }

    // 2. GET Request: Get specific title-author contract by Composite Key (auId and titleId)
    @GetMapping("/{auId}/{titleId}")
    public ResponseEntity<?> getTitleAuthorById(@PathVariable("auId") String auId, @PathVariable("titleId") String titleId) {
        try {
            TitleAuthorId id = new TitleAuthorId(auId, titleId);
            TitleAuthor titleAuthor = titleAuthorService.getTitleAuthorById(id);
            return new ResponseEntity<>(titleAuthor, HttpStatus.OK); // 200 OK
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND); // 404 Not Found
        }
    }

    // 3. POST Request: Create a new title-author contract
    @PostMapping
    public ResponseEntity<?> createTitleAuthor(@Valid @RequestBody TitleAuthor titleAuthor, BindingResult result) {
        
        // 1. Validation Check
        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST); // 400 Bad Request
        }

        // 2. Save to Database
        try {
            TitleAuthor savedTitleAuthor = titleAuthorService.createTitleAuthor(titleAuthor);
            return new ResponseEntity<>(savedTitleAuthor, HttpStatus.CREATED); // 201 Created
        } catch (Exception e) {
            return new ResponseEntity<>("Database Error: This Author is already assigned to this Title.", HttpStatus.CONFLICT); // 409 Conflict
        }
    }

    // 4. PUT Request: Update an existing contract
    @PutMapping("/{auId}/{titleId}")
    public ResponseEntity<?> updateTitleAuthor(
            @PathVariable("auId") String auId, 
            @PathVariable("titleId") String titleId, 
            @Valid @RequestBody TitleAuthor titleAuthor, 
            BindingResult result) {
        
        // 1. Validation Check
        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST); // 400 Bad Request
        }

        // 2. Update Database
        try {
            TitleAuthorId id = new TitleAuthorId(auId, titleId);
            TitleAuthor updatedTitleAuthor = titleAuthorService.updateTitleAuthor(id, titleAuthor);
            return new ResponseEntity<>(updatedTitleAuthor, HttpStatus.OK); // 200 OK
        } catch (Exception e) {
            return new ResponseEntity<>("Database Error: Could not update contract.", HttpStatus.INTERNAL_SERVER_ERROR); // 500 Server Error
        }
    }

    // 5. GET Request: Filter by Max Royalty Percentage
    @GetMapping("/filter/royalty")
    public ResponseEntity<List<TitleAuthor>> filterTitleAuthorsByRoyalty(@RequestParam("maxRoyalty") Integer maxRoyalty) {
        return new ResponseEntity<>(titleAuthorService.getTitleAuthorsByRoyaltyLessThan(maxRoyalty), HttpStatus.OK); // 200 OK
    }

    // 6. GET Request: Filter by Author ID
    @GetMapping("/filter/author")
    public ResponseEntity<List<TitleAuthor>> filterTitleAuthorsByAuthorId(@RequestParam("auId") String auId) {
        return new ResponseEntity<>(titleAuthorService.getTitleAuthorsByAuthorId(auId), HttpStatus.OK); // 200 OK
    }

    // 7. GET Request: Filter by Title ID
    @GetMapping("/filter/title")
    public ResponseEntity<List<TitleAuthor>> filterTitleAuthorsByTitleId(@RequestParam("titleId") String titleId) {
        return new ResponseEntity<>(titleAuthorService.getTitleAuthorsByTitleId(titleId), HttpStatus.OK); // 200 OK
    }
}