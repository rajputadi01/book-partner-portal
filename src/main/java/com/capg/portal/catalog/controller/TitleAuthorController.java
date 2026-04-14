package com.capg.portal.catalog.controller;

import com.capg.portal.catalog.entity.TitleAuthor;
import com.capg.portal.catalog.entity.TitleAuthorId;
import com.capg.portal.catalog.service.TitleAuthorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/titleauthors")
public class TitleAuthorController 
{
    private final TitleAuthorService titleAuthorService;

    public TitleAuthorController(TitleAuthorService titleAuthorService) 
    {
        this.titleAuthorService = titleAuthorService;
    }

    @GetMapping
    public ResponseEntity<List<TitleAuthor>> getAllTitleAuthors() 
    {
        return new ResponseEntity<>(titleAuthorService.getAllTitleAuthors(), HttpStatus.OK);
    }

    @GetMapping("/{auId}/{titleId}")
    public ResponseEntity<TitleAuthor> getTitleAuthorById(@PathVariable("auId") String auId, @PathVariable("titleId") String titleId) 
    {
        TitleAuthorId id = new TitleAuthorId(auId, titleId);
        return new ResponseEntity<>(titleAuthorService.getTitleAuthorById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TitleAuthor> createTitleAuthor(@Valid @RequestBody TitleAuthor titleAuthor) 
    {
        TitleAuthor savedTitleAuthor = titleAuthorService.createTitleAuthor(titleAuthor);
        return new ResponseEntity<>(savedTitleAuthor, HttpStatus.CREATED);
    }

    @PutMapping("/{auId}/{titleId}")
    public ResponseEntity<TitleAuthor> updateTitleAuthor(@PathVariable("auId") String auId, @PathVariable("titleId") String titleId, @Valid @RequestBody TitleAuthor titleAuthor) 
    {
        TitleAuthorId id = new TitleAuthorId(auId, titleId);
        TitleAuthor updatedTitleAuthor = titleAuthorService.updateTitleAuthor(id, titleAuthor);
        return new ResponseEntity<>(updatedTitleAuthor, HttpStatus.OK);
    }

    @PatchMapping("/{auId}/{titleId}")
    public ResponseEntity<TitleAuthor> patchTitleAuthor(@PathVariable("auId") String auId, @PathVariable("titleId") String titleId, @RequestBody TitleAuthor updates) 
    {
        TitleAuthorId id = new TitleAuthorId(auId, titleId);
        TitleAuthor patchedTitleAuthor = titleAuthorService.patchTitleAuthor(id, updates);
        return new ResponseEntity<>(patchedTitleAuthor, HttpStatus.OK);
    }

    @GetMapping("/filter/royalty")
    public ResponseEntity<List<TitleAuthor>> filterTitleAuthorsByRoyalty(@RequestParam("maxRoyalty") Integer maxRoyalty) 
    {
        return new ResponseEntity<>(titleAuthorService.getTitleAuthorsByRoyaltyLessThan(maxRoyalty), HttpStatus.OK);
    }

    @GetMapping("/filter/author")
    public ResponseEntity<List<TitleAuthor>> filterTitleAuthorsByAuthorId(@RequestParam("auId") String auId) 
    {
        return new ResponseEntity<>(titleAuthorService.getTitleAuthorsByAuthorId(auId), HttpStatus.OK);
    }

    @GetMapping("/filter/title")
    public ResponseEntity<List<TitleAuthor>> filterTitleAuthorsByTitleId(@RequestParam("titleId") String titleId) 
    {
        return new ResponseEntity<>(titleAuthorService.getTitleAuthorsByTitleId(titleId), HttpStatus.OK);
    }
    
    @GetMapping("/filter/lead")
    public ResponseEntity<List<TitleAuthor>> getLeadAuthors() 
    {
        return new ResponseEntity<>(titleAuthorService.getLeadAuthors(), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<TitleAuthor>> search(
            @RequestParam(required = false) String auId,
            @RequestParam(required = false) String titleId,
            @RequestParam(required = false) Integer maxRoyalty,
            @RequestParam(required = false) Integer minRoyalty) 
    {
        return new ResponseEntity<>(titleAuthorService.search(auId, titleId, maxRoyalty, minRoyalty), HttpStatus.OK);
    }
}