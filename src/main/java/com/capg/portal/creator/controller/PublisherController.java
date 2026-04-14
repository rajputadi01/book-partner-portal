package com.capg.portal.creator.controller;

import com.capg.portal.creator.entity.Publisher;
import com.capg.portal.creator.entity.Author;
import com.capg.portal.catalog.entity.Title;
import com.capg.portal.hr.entity.Employee;
import com.capg.portal.retail.entity.Store;
import com.capg.portal.creator.service.PublisherService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/publishers")
public class PublisherController 
{
    private final PublisherService publisherService;

    public PublisherController(PublisherService publisherService) 
    {
        this.publisherService = publisherService;
    }

    @GetMapping
    public ResponseEntity<List<Publisher>> getAllPublishers() 
    {
        return new ResponseEntity<>(publisherService.getAllPublishers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Publisher> getPublisherById(@PathVariable("id") String id) 
    {
        Publisher publisher = publisherService.getPublisherById(id);
        return new ResponseEntity<>(publisher, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Publisher> createPublisher(@Valid @RequestBody Publisher publisher) 
    {
        Publisher savedPublisher = publisherService.createPublisher(publisher);
        return new ResponseEntity<>(savedPublisher, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Publisher> updatePublisher(@PathVariable("id") String id, @Valid @RequestBody Publisher publisher) 
    {
        Publisher updatedPublisher = publisherService.updatePublisher(id, publisher);
        return new ResponseEntity<>(updatedPublisher, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Publisher> patchPublisher(@PathVariable("id") String id, @RequestBody Publisher updates) 
    {
        Publisher patchedPublisher = publisherService.patchPublisher(id, updates);
        return new ResponseEntity<>(patchedPublisher, HttpStatus.OK);
    }

    @GetMapping("/filter/city")
    public ResponseEntity<List<Publisher>> filterPublishersByCity(@RequestParam("city") String city) 
    {
        return new ResponseEntity<>(publisherService.getPublishersByCity(city), HttpStatus.OK);
    }

    @GetMapping("/filter/state")
    public ResponseEntity<List<Publisher>> filterPublishersByState(@RequestParam("state") String state) 
    {
        return new ResponseEntity<>(publisherService.getPublishersByState(state), HttpStatus.OK);
    }

    @GetMapping("/filter/country")
    public ResponseEntity<List<Publisher>> filterPublishersByCountry(@RequestParam("country") String country) 
    {
        return new ResponseEntity<>(publisherService.getPublishersByCountry(country), HttpStatus.OK);
    }

    @GetMapping("/{id}/employees")
    public ResponseEntity<List<Employee>> getEmployeesByPublisher(@PathVariable("id") String id) 
    {
        return new ResponseEntity<>(publisherService.getEmployeesByPublisherId(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/titles")
    public ResponseEntity<List<Title>> getTitlesByPublisher(@PathVariable("id") String id) 
    {
        return new ResponseEntity<>(publisherService.getTitlesByPublisherId(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/authors")
    public ResponseEntity<List<Author>> getAuthorsByPublisher(@PathVariable("id") String id) 
    {
        return new ResponseEntity<>(publisherService.getAuthorsByPublisherId(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/stores")
    public ResponseEntity<List<Store>> getStoresByPublisher(@PathVariable("id") String id) 
    {
        return new ResponseEntity<>(publisherService.getStoresByPublisherId(id), HttpStatus.OK);
    }
}