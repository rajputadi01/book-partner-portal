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

   
    public TitleAuthorController(TitleAuthorService titleAuthorService) {
        this.titleAuthorService = titleAuthorService;
    }

 
    @GetMapping
    public ResponseEntity<List<TitleAuthor>> getAllTitleAuthors() {
        return new ResponseEntity<>(titleAuthorService.getAllTitleAuthors(), HttpStatus.OK); // 200 OK
    }

   
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


    @PostMapping
    public ResponseEntity<?> createTitleAuthor(@Valid @RequestBody TitleAuthor titleAuthor, BindingResult result) {
        
  
        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST); // 400 Bad Request
        }


        try {
            TitleAuthor savedTitleAuthor = titleAuthorService.createTitleAuthor(titleAuthor);
            return new ResponseEntity<>(savedTitleAuthor, HttpStatus.CREATED); // 201 Created
        } catch (Exception e) {
            return new ResponseEntity<>("Database Error: This Author is already assigned to this Title.", HttpStatus.CONFLICT); // 409 Conflict
        }
    }


    @PutMapping("/{auId}/{titleId}")
    public ResponseEntity<?> updateTitleAuthor(
            @PathVariable("auId") String auId, 
            @PathVariable("titleId") String titleId, 
            @Valid @RequestBody TitleAuthor titleAuthor, 
            BindingResult result) {
        
        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST); // 400 Bad Request
        }

        try {
            TitleAuthorId id = new TitleAuthorId(auId, titleId);
            TitleAuthor updatedTitleAuthor = titleAuthorService.updateTitleAuthor(id, titleAuthor);
            return new ResponseEntity<>(updatedTitleAuthor, HttpStatus.OK); // 200 OK
        } catch (Exception e) {
            return new ResponseEntity<>("Database Error: Could not update contract.", HttpStatus.INTERNAL_SERVER_ERROR); // 500 Server Error
        }
    }


    @GetMapping("/filter/royalty")
    public ResponseEntity<List<TitleAuthor>> filterTitleAuthorsByRoyalty(@RequestParam("maxRoyalty") Integer maxRoyalty) {
        return new ResponseEntity<>(titleAuthorService.getTitleAuthorsByRoyaltyLessThan(maxRoyalty), HttpStatus.OK); // 200 OK
    }


    @GetMapping("/filter/author")
    public ResponseEntity<List<TitleAuthor>> filterTitleAuthorsByAuthorId(@RequestParam("auId") String auId) {
        return new ResponseEntity<>(titleAuthorService.getTitleAuthorsByAuthorId(auId), HttpStatus.OK); // 200 OK
    }

 
    @GetMapping("/filter/title")
    public ResponseEntity<List<TitleAuthor>> filterTitleAuthorsByTitleId(@RequestParam("titleId") String titleId) {
        return new ResponseEntity<>(titleAuthorService.getTitleAuthorsByTitleId(titleId), HttpStatus.OK); // 200 OK
    }
    
    
    @GetMapping("/search")
    public ResponseEntity<List<TitleAuthor>> search(
            @RequestParam(required = false) String auId,
            @RequestParam(required = false) String titleId,
            @RequestParam(required = false) Integer maxRoyalty,
            @RequestParam(required = false) Integer minRoyalty) {

        List<TitleAuthor> result = titleAuthorService.search(auId, titleId, maxRoyalty, minRoyalty);
        return ResponseEntity.ok(result);
    }
}