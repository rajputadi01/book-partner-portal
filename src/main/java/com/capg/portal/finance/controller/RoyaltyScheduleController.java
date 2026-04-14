package com.capg.portal.finance.controller;

import com.capg.portal.finance.entity.RoyaltySchedule;
import com.capg.portal.catalog.entity.Title;
import com.capg.portal.finance.service.RoyaltyScheduleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/roysched")
public class RoyaltyScheduleController 
{
    private final RoyaltyScheduleService royaltyScheduleService;

    public RoyaltyScheduleController(RoyaltyScheduleService royaltyScheduleService) 
    {
        this.royaltyScheduleService = royaltyScheduleService;
    }

    @GetMapping
    public ResponseEntity<List<RoyaltySchedule>> getAllRoyaltySchedules() 
    {
        return new ResponseEntity<>(royaltyScheduleService.getAllRoyaltySchedules(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoyaltySchedule> getRoyaltyScheduleById(@PathVariable("id") Integer id) 
    {
        return new ResponseEntity<>(royaltyScheduleService.getRoyaltyScheduleById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<RoyaltySchedule> createRoyaltySchedule(@Valid @RequestBody RoyaltySchedule royaltySchedule) 
    {
        return new ResponseEntity<>(royaltyScheduleService.createRoyaltySchedule(royaltySchedule), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoyaltySchedule> updateRoyaltySchedule(@PathVariable("id") Integer id, @Valid @RequestBody RoyaltySchedule royaltySchedule) 
    {
        return new ResponseEntity<>(royaltyScheduleService.updateRoyaltySchedule(id, royaltySchedule), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<RoyaltySchedule> patchRoyaltySchedule(@PathVariable("id") Integer id, @RequestBody RoyaltySchedule updates) 
    {
        return new ResponseEntity<>(royaltyScheduleService.patchRoyaltySchedule(id, updates), HttpStatus.OK);
    }

    @GetMapping("/filter/range")
    public ResponseEntity<List<RoyaltySchedule>> filterRoyaltyByRange(
            @RequestParam("minLorange") Integer minLorange,
            @RequestParam("maxHirange") Integer maxHirange) 
    {
        return new ResponseEntity<>(royaltyScheduleService.getRoyaltySchedulesByRange(minLorange, maxHirange), HttpStatus.OK);
    }

    @GetMapping("/filter/title")
    public ResponseEntity<List<RoyaltySchedule>> filterRoyaltyByTitle(@RequestParam("titleId") String titleId) 
    {
        return new ResponseEntity<>(royaltyScheduleService.getRoyaltySchedulesByTitleId(titleId), HttpStatus.OK);
    }
    
    @GetMapping("/{id}/title")
    public ResponseEntity<Title> getTitleByRoyaltySchedule(@PathVariable("id") Integer id) 
    {
        RoyaltySchedule schedule = royaltyScheduleService.getRoyaltyScheduleById(id);
        return new ResponseEntity<>(schedule.getTitle(), HttpStatus.OK);
    }
}