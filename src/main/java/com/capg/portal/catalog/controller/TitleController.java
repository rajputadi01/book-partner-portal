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
    public ModelAndView viewTitlesPage() {
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
    public ModelAndView saveTitle(@Valid @ModelAttribute("title") Title title, BindingResult result) {
        
        // 1. Spring Validation Failure (Catches blank fields)
        if (result.hasErrors()) {
            ModelAndView mav = new ModelAndView("titles");
            mav.addObject("title", title);
            mav.addObject("org.springframework.validation.BindingResult.title", result);
            
            // Re-populate lists so the dropdowns don't crash
            mav.addObject("listTitles", titleService.getAllTitles());
            mav.addObject("listPublishers", publisherService.getAllPublishers());
            return mav;
        }
        
        // Cleanup empty strings & Failsafe date
        if (title.getType() == null || title.getType().trim().isEmpty()) {
            title.setType("UNDECIDED");
        }
        if (title.getPubdate() == null) {
            title.setPubdate(LocalDateTime.now());
        }

        // 2. Database Validation Failure (Catches duplicates)
        try {
            titleService.createTitle(title);
        } catch (Exception e) {
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
    
 // 3. GET Request: Load the Edit Page for a specific Title
    @GetMapping("/edit/{id}")
    public ModelAndView viewEditTitlePage(@PathVariable("id") String id) {
        ModelAndView mav = new ModelAndView("edit-title"); // Points to the new HTML file
        
        mav.addObject("title", titleService.getTitleById(id));
        mav.addObject("listPublishers", publisherService.getAllPublishers()); // Required for Publisher dropdown
        
        return mav;
    }

    // 4. POST Request: Save the updated data
    @PostMapping("/update/{id}")
    public ModelAndView saveUpdatedTitle(@PathVariable("id") String id, @Valid @ModelAttribute("title") Title title, BindingResult result) {
        
        // 1. Spring Validation Failure
        if (result.hasErrors()) {
            ModelAndView mav = new ModelAndView("edit-title");
            mav.addObject("title", title);
            mav.addObject("org.springframework.validation.BindingResult.title", result);
            mav.addObject("listPublishers", publisherService.getAllPublishers()); // Reload dropdown data!
            return mav;
        }

        // Cleanup empty strings & Failsafe date
        if (title.getType() == null || title.getType().trim().isEmpty()) {
            title.setType("UNDECIDED");
        }
        if (title.getPubdate() == null) {
            title.setPubdate(LocalDateTime.now());
        }

        // 2. Database Validation Failure
        try {
            titleService.updateTitle(id, title);
        } catch (Exception e) {
            result.rejectValue("titleName", "error.title", "Database Error: Could not update title.");
            
            ModelAndView mav = new ModelAndView("edit-title");
            mav.addObject("title", title);
            mav.addObject("org.springframework.validation.BindingResult.title", result);
            mav.addObject("listPublishers", publisherService.getAllPublishers());
            return mav;
        }

        return new ModelAndView("redirect:/titles");
    }

    // 5. GET Request: Filter the table by Maximum Price
    @GetMapping("/filter")
    public ModelAndView filterTitlesByPrice(@RequestParam(value = "maxPrice", required = false) Double maxPrice) {
        ModelAndView mav = new ModelAndView("titles");
        
        // If the user submits a blank search, return all titles. Otherwise, filter them.
        if (maxPrice == null) {
            mav.addObject("listTitles", titleService.getAllTitles());
        } else {
            mav.addObject("listTitles", titleService.getTitlesByPriceLessThan(maxPrice));
        }
        
        mav.addObject("listPublishers", publisherService.getAllPublishers()); // Reload dropdown data
        
        // Keeps the "Add New" form working with a default date
        Title title = new Title();
        title.setPubdate(LocalDateTime.now());
        mav.addObject("title", title); 
        
        return mav;
    }
}