package com.capg.portal.catalog.controller;

import com.capg.portal.catalog.entity.Title;
import com.capg.portal.catalog.entity.TitleAuthor;
import com.capg.portal.catalog.service.TitleService;
import com.capg.portal.creator.entity.Author;
import com.capg.portal.creator.entity.Publisher;
import com.capg.portal.finance.entity.RoyaltySchedule;
import com.capg.portal.retail.entity.Sales;
import com.capg.portal.retail.entity.Store;

import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/titles")
public class TitleController 
{
    private final TitleService titleService;
    
    public TitleController(TitleService titleService) 
    {
        this.titleService = titleService;
    }
    
    @GetMapping
    public ResponseEntity<List<Title>> getAllTitles() 
    {
        return new ResponseEntity<>(titleService.getAllTitles(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Title> getTitleById(@PathVariable("id") String id) 
    {
        Title title = titleService.getTitleById(id);
        return new ResponseEntity<>(title, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Title> createTitle(@Valid @RequestBody Title title) 
    {
        if (title.getType() == null || title.getType().trim().isEmpty()) 
        {
            title.setType("UNDECIDED");
        }
        
        if (title.getPubdate() == null) 
        {
            title.setPubdate(LocalDateTime.now());
        }

        Title savedTitle = titleService.createTitle(title);
        return new ResponseEntity<>(savedTitle, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Title> updateTitle(@PathVariable("id") String id, @Valid @RequestBody Title title) 
    {
        if (title.getType() == null || title.getType().trim().isEmpty()) 
        {
            title.setType("UNDECIDED");
        }
        
        if (title.getPubdate() == null) 
        {
            title.setPubdate(LocalDateTime.now());
        }

        Title updatedTitle = titleService.updateTitle(id, title);
        return new ResponseEntity<>(updatedTitle, HttpStatus.OK);
    }
    
    @PatchMapping("/{id}")
    public ResponseEntity<Title> patchTitle(@PathVariable("id") String id, @RequestBody Title updates) 
    {
        Title updatedTitle = titleService.patchTitle(id, updates);
        return new ResponseEntity<>(updatedTitle, HttpStatus.OK);
    }

    @GetMapping("/filter/price")
    public ResponseEntity<List<Title>> filterTitlesByPrice(@RequestParam("maxPrice") Double maxPrice) 
    {
        return new ResponseEntity<>(titleService.getTitlesByPriceLessThan(maxPrice), HttpStatus.OK);
    }

    @GetMapping("/filter/type")
    public ResponseEntity<List<Title>> filterTitlesByType(@RequestParam("type") String type) 
    {
        return new ResponseEntity<>(titleService.getTitlesByType(type), HttpStatus.OK);
    }

    @GetMapping("/filter/publisher")
    public ResponseEntity<List<Title>> filterTitlesByPublisher(@RequestParam("pubId") String pubId) 
    {
        return new ResponseEntity<>(titleService.getTitlesByPublisher(pubId), HttpStatus.OK);
    }

    @GetMapping("/filter/date")
    public ResponseEntity<List<Title>> filterTitlesByDateBefore(@RequestParam("beforeDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime beforeDate) 
    {
        return new ResponseEntity<>(titleService.getTitlesPublishedBefore(beforeDate), HttpStatus.OK);
    }

    @GetMapping("/{id}/publisher")
    public ResponseEntity<Publisher> getPublisherByTitle(@PathVariable("id") String id) 
    {
        return new ResponseEntity<>(titleService.getPublisherByTitleId(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/sales")
    public ResponseEntity<List<Sales>> getSalesByTitle(@PathVariable("id") String id) 
    {
        return new ResponseEntity<>(titleService.getSalesByTitleId(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/royalties")
    public ResponseEntity<List<RoyaltySchedule>> getRoyaltiesByTitle(@PathVariable("id") String id) 
    {
        return new ResponseEntity<>(titleService.getRoyaltiesByTitleId(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/title-authors")
    public ResponseEntity<List<TitleAuthor>> getTitleAuthorsByTitle(@PathVariable("id") String id) 
    {
        return new ResponseEntity<>(titleService.getTitleAuthorsByTitleId(id), HttpStatus.OK);
    }
    
    @GetMapping("/{id}/authors")
    public ResponseEntity<List<Author>> getAuthorsByTitle(@PathVariable("id") String id) 
    {
        return new ResponseEntity<>(titleService.getAuthorsByTitleId(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/stores")
    public ResponseEntity<List<Store>> getStoresByTitle(@PathVariable("id") String id) 
    {
        return new ResponseEntity<>(titleService.getStoresByTitleId(id), HttpStatus.OK);
    }
}