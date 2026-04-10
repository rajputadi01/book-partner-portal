package com.capg.portal.creator.controller;

import com.capg.portal.creator.entity.Author;
import com.capg.portal.creator.service.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    // 1. GET Request: Load the HTML page
    @GetMapping
    public ModelAndView viewAuthorsPage() {
        ModelAndView mav = new ModelAndView("authors");
        mav.addObject("listAuthors", authorService.getAllAuthors());
        mav.addObject("author", new Author()); 
        return mav;
    }

    // 2. POST Request: Save data from the HTML form
    @PostMapping
    public ModelAndView saveAuthor(@Valid @ModelAttribute("author") Author author, BindingResult result) {
        
        // 1. Spring Validation Failure
        if (result.hasErrors()) {
            ModelAndView mav = new ModelAndView("authors");
            mav.addObject("author", author);
            mav.addObject("org.springframework.validation.BindingResult.author", result);
            mav.addObject("listAuthors", authorService.getAllAuthors());
            return mav;
        }

        // Clean up empty strings before sending to Database
        if (author.getPhone() == null || author.getPhone().trim().isEmpty()) {
            author.setPhone("UNKNOWN");
        }
        if (author.getZip() != null && author.getZip().trim().isEmpty()) {
            author.setZip(null);
        }
        if (author.getState() != null && author.getState().trim().isEmpty()) {
            author.setState(null);
        }

        // 2. Database Validation Failure
        try {
            authorService.createAuthor(author);
        } catch (Exception e) {
            result.rejectValue("auId", "error.author", "Database Error: ID exists or format violates DB constraints.");
            
            ModelAndView mav = new ModelAndView("authors");
            mav.addObject("author", author);
            mav.addObject("org.springframework.validation.BindingResult.author", result);
            mav.addObject("listAuthors", authorService.getAllAuthors());
            return mav;
        }

        return new ModelAndView("redirect:/authors");
    }
}