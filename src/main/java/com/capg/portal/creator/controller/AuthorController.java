package com.capg.portal.creator.controller;

import com.capg.portal.creator.entity.Author;
import com.capg.portal.creator.service.AuthorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;

    // Constructor Injection (Replaces Lombok's @RequiredArgsConstructor)
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    // 1. GET Request: Get all authors
    @GetMapping
    public ResponseEntity<List<Author>> getAllAuthors() {
        return new ResponseEntity<>(authorService.getAllAuthors(), HttpStatus.OK); // 200 OK
    }

    // 2. GET Request: Get author by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getAuthorById(@PathVariable("id") String id) {
        try {
            Author author = authorService.getAuthorById(id);
            return new ResponseEntity<>(author, HttpStatus.OK); // 200 OK
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND); // 404 Not Found
        }
    }

    // 3. POST Request: Create a new author
    @PostMapping
    public ResponseEntity<?> createAuthor(@Valid @RequestBody Author author, BindingResult result) {
        
        // 1. Validation Check
        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST); // 400 Bad Request
        }

        // Clean up empty strings before saving
        if (author.getPhone() == null || author.getPhone().trim().isEmpty()) {
            author.setPhone("UNKNOWN");
        }
        if (author.getZip() != null && author.getZip().trim().isEmpty()) {
            author.setZip(null);
        }
        if (author.getState() != null && author.getState().trim().isEmpty()) {
            author.setState(null);
        }

        // 2. Save to Database
        try {
            Author savedAuthor = authorService.createAuthor(author);
            return new ResponseEntity<>(savedAuthor, HttpStatus.CREATED); // 201 Created
        } catch (Exception e) {
            return new ResponseEntity<>("Database Error: ID exists or format violates DB constraints.", HttpStatus.CONFLICT); // 409 Conflict
        }
    }

    // 4. PUT Request: Update an existing author
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAuthor(@PathVariable("id") String id, @Valid @RequestBody Author author, BindingResult result) {
        
        // 1. Validation Check
        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST); // 400 Bad Request
        }

        // Clean up empty strings before saving
        if (author.getPhone() == null || author.getPhone().trim().isEmpty()) {
            author.setPhone("UNKNOWN");
        }
        if (author.getZip() != null && author.getZip().trim().isEmpty()) {
            author.setZip(null);
        }
        if (author.getState() != null && author.getState().trim().isEmpty()) {
            author.setState(null);
        }

        // 2. Update Database
        try {
            Author updatedAuthor = authorService.updateAuthor(id, author);
            return new ResponseEntity<>(updatedAuthor, HttpStatus.OK); // 200 OK
        } catch (Exception e) {
            return new ResponseEntity<>("Database Error: Could not update author.", HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error
        }
    }

    // 5. GET Request: Filter by Contract
    @GetMapping("/filter/contract")
    public ResponseEntity<List<Author>> filterAuthorsByContract(@RequestParam("status") Integer contract) {
        return new ResponseEntity<>(authorService.getAuthorsByContractStatus(contract), HttpStatus.OK); // 200 OK
    }

    // 6. GET Request: Filter by City
    @GetMapping("/filter/city")
    public ResponseEntity<List<Author>> filterAuthorsByCity(@RequestParam("name") String city) {
        return new ResponseEntity<>(authorService.getAuthorsByCity(city), HttpStatus.OK); // 200 OK
    }

    // 7. GET Request: Filter by State
    @GetMapping("/filter/state")
    public ResponseEntity<List<Author>> filterAuthorsByState(@RequestParam("code") String state) {
        return new ResponseEntity<>(authorService.getAuthorsByState(state), HttpStatus.OK); // 200 OK
    }
}