package com.capg.portal.catalog.controller;

import com.capg.portal.catalog.entity.Title;
import com.capg.portal.catalog.service.TitleService;
import com.capg.portal.creator.service.PublisherService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/titles")
@RequiredArgsConstructor
public class TitleController {

    private final TitleService titleService;
    private final PublisherService publisherService;

    // 1. GET Request: Load the HTML page
    @GetMapping
    public ModelAndView viewTitlesPage() 
    {
        ModelAndView mav = new ModelAndView("titles");
        
        mav.addObject("listTitles", titleService.getAllTitles());
        mav.addObject("listPublishers", publisherService.getAllPublishers()); 
        
        // Default the publish date to right now for new entries
        Title title = new Title();
        title.setPubdate(LocalDateTime.now());
        mav.addObject("title", title); 
        
        return mav;
    }

    // 2. POST Request: Save data from the HTML form
    @PostMapping
    public ModelAndView saveTitle(@Valid @ModelAttribute("title") Title title, BindingResult result) 
    {
        // 1. Spring Validation Failure (Catches blank fields)
        if (result.hasErrors()) 
        {
            ModelAndView mav = new ModelAndView("titles");
            mav.addObject("title", title);
            mav.addObject("org.springframework.validation.BindingResult.title", result);
            
            // Re-populate lists so the dropdowns don't crash
            mav.addObject("listTitles", titleService.getAllTitles());
            mav.addObject("listPublishers", publisherService.getAllPublishers());
            return mav;
        }
        
        // Cleanup empty strings & Failsafe date
        if (title.getType() == null || title.getType().trim().isEmpty()) 
        {
            title.setType("UNDECIDED");
        }
        if (title.getPubdate() == null) {
            title.setPubdate(LocalDateTime.now());
        }

        // 2. Database Validation Failure (Catches duplicates)
        try 
        {
            titleService.createTitle(title);
        } 
        catch (Exception e) 
        {
            result.rejectValue("titleId", "error.title", "Database Error: Title ID may already exist or violates a rule.");
            
            ModelAndView mav = new ModelAndView("titles");
            mav.addObject("title", title);
            mav.addObject("org.springframework.validation.BindingResult.title", result);
            
            mav.addObject("listTitles", titleService.getAllTitles());
            mav.addObject("listPublishers", publisherService.getAllPublishers());
            return mav;
        }

        return new ModelAndView("redirect:/titles");
    }
}