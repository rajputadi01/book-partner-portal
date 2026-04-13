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

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public ResponseEntity<List<Author>> getAllAuthors() {
        return new ResponseEntity<>(authorService.getAllAuthors(), HttpStatus.OK); 
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAuthorById(@PathVariable("id") String id) {
        try {
            Author author = authorService.getAuthorById(id);
            return new ResponseEntity<>(author, HttpStatus.OK); 
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND); 
        }
    }

    @PostMapping
    public ResponseEntity<?> createAuthor(@Valid @RequestBody Author author, BindingResult result) {
        
        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST); 
        }

        if (author.getPhone() == null || author.getPhone().trim().isEmpty()) {
            author.setPhone("UNKNOWN");
        }
        if (author.getZip() != null && author.getZip().trim().isEmpty()) {
            author.setZip(null);
        }
        if (author.getState() != null && author.getState().trim().isEmpty()) {
            author.setState(null);
        }

        try {
            Author savedAuthor = authorService.createAuthor(author);
            return new ResponseEntity<>(savedAuthor, HttpStatus.CREATED); 
        } catch (Exception e) {
            return new ResponseEntity<>("Database Error: ID exists or format violates DB constraints.", HttpStatus.CONFLICT); 
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAuthor(@PathVariable("id") String id, @Valid @RequestBody Author author, BindingResult result) {
        
        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST); 
        }

        if (author.getPhone() == null || author.getPhone().trim().isEmpty()) {
            author.setPhone("UNKNOWN");
        }
        if (author.getZip() != null && author.getZip().trim().isEmpty()) {
            author.setZip(null);
        }
        if (author.getState() != null && author.getState().trim().isEmpty()) {
            author.setState(null);
        }

        try {
            Author updatedAuthor = authorService.updateAuthor(id, author);
            return new ResponseEntity<>(updatedAuthor, HttpStatus.OK); 
        } catch (Exception e) {
            return new ResponseEntity<>("Database Error: Could not update author.", HttpStatus.INTERNAL_SERVER_ERROR); 
        }
    }

    @GetMapping("/filter/contract")
    public ResponseEntity<List<Author>> filterAuthorsByContract(@RequestParam("status") Integer contract) {
        return new ResponseEntity<>(authorService.getAuthorsByContractStatus(contract), HttpStatus.OK); 
    }

    @GetMapping("/filter/city")
    public ResponseEntity<List<Author>> filterAuthorsByCity(@RequestParam("name") String city) {
        return new ResponseEntity<>(authorService.getAuthorsByCity(city), HttpStatus.OK); 
    }

    @GetMapping("/filter/state")
    public ResponseEntity<List<Author>> filterAuthorsByState(@RequestParam("code") String state) {
        return new ResponseEntity<>(authorService.getAuthorsByState(state), HttpStatus.OK); 
    }
}