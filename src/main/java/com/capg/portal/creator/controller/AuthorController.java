package com.capg.portal.creator.controller;

import com.capg.portal.creator.entity.Author;
import com.capg.portal.creator.entity.Publisher;
import com.capg.portal.catalog.entity.Title;
import com.capg.portal.catalog.entity.TitleAuthor;
import com.capg.portal.retail.entity.Store;
import com.capg.portal.creator.service.AuthorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/authors")
public class AuthorController 
{
    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) 
    {
        this.authorService = authorService;
    }

    @GetMapping
    public ResponseEntity<List<Author>> getAllAuthors() 
    {
        return new ResponseEntity<>(authorService.getAllAuthors(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Author> getAuthorById(@PathVariable("id") String id) 
    {
        Author author = authorService.getAuthorById(id);
        return new ResponseEntity<>(author, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Author> createAuthor(@Valid @RequestBody Author author) 
    {
        Author savedAuthor = authorService.createAuthor(author);
        return new ResponseEntity<>(savedAuthor, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Author> updateAuthor(@PathVariable("id") String id, @Valid @RequestBody Author author) 
    {
        Author updatedAuthor = authorService.updateAuthor(id, author);
        return new ResponseEntity<>(updatedAuthor, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Author> patchAuthor(@PathVariable("id") String id, @RequestBody Author updates) 
    {
        Author patchedAuthor = authorService.patchAuthor(id, updates);
        return new ResponseEntity<>(patchedAuthor, HttpStatus.OK);
    }

    @GetMapping("/filter/contract")
    public ResponseEntity<List<Author>> filterAuthorsByContract(@RequestParam("status") Integer contract) 
    {
        return new ResponseEntity<>(authorService.getAuthorsByContractStatus(contract), HttpStatus.OK);
    }

    @GetMapping("/filter/city")
    public ResponseEntity<List<Author>> filterAuthorsByCity(@RequestParam("city") String city) 
    {
        return new ResponseEntity<>(authorService.getAuthorsByCity(city), HttpStatus.OK);
    }

    @GetMapping("/filter/state")
    public ResponseEntity<List<Author>> filterAuthorsByState(@RequestParam("state") String state) 
    {
        return new ResponseEntity<>(authorService.getAuthorsByState(state), HttpStatus.OK);
    }

    @GetMapping("/{id}/title-authors")
    public ResponseEntity<List<TitleAuthor>> getTitleAuthorsByAuthor(@PathVariable("id") String id) 
    {
        return new ResponseEntity<>(authorService.getTitleAuthorsByAuthorId(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/titles")
    public ResponseEntity<List<Title>> getTitlesByAuthor(@PathVariable("id") String id) 
    {
        return new ResponseEntity<>(authorService.getTitlesByAuthorId(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/publishers")
    public ResponseEntity<List<Publisher>> getPublishersByAuthor(@PathVariable("id") String id) 
    {
        return new ResponseEntity<>(authorService.getPublishersByAuthorId(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/stores")
    public ResponseEntity<List<Store>> getStoresByAuthor(@PathVariable("id") String id) 
    {
        return new ResponseEntity<>(authorService.getStoresByAuthorId(id), HttpStatus.OK);
    }
}