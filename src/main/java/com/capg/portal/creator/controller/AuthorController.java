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
    
    // 3. GET Request: Load the Edit Page for a specific Author
    @GetMapping("/edit/{id}")
    public ModelAndView viewEditAuthorPage(@PathVariable("id") String id) {
        ModelAndView mav = new ModelAndView("edit-author"); // Points to the new HTML file
        mav.addObject("author", authorService.getAuthorById(id));
        return mav;
    }

    // 4. POST Request: Save the updated data
    @PostMapping("/update/{id}")
    public ModelAndView saveUpdatedAuthor(@PathVariable("id") String id, @Valid @ModelAttribute("author") Author author, BindingResult result) {
        
        // 1. Spring Validation Failure
        if (result.hasErrors()) {
            ModelAndView mav = new ModelAndView("edit-author");
            mav.addObject("author", author);
            mav.addObject("org.springframework.validation.BindingResult.author", result);
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
            authorService.updateAuthor(id, author);
        } catch (Exception e) {
            result.rejectValue("auLname", "error.author", "Database Error: Could not update author.");
            ModelAndView mav = new ModelAndView("edit-author");
            mav.addObject("author", author);
            mav.addObject("org.springframework.validation.BindingResult.author", result);
            return mav;
        }

        return new ModelAndView("redirect:/authors");
    }

    // 5. GET Request: Filter the table by Contract Status
    @GetMapping("/filter")
    public ModelAndView filterAuthorsByContract(@RequestParam(value = "contract", required = false) Integer contract) 
    {
        ModelAndView mav = new ModelAndView("authors");
        
        // If the user submits a blank search, return all authors. Otherwise, filter them.
        if (contract == null) {
            mav.addObject("listAuthors", authorService.getAllAuthors());
        } else {
            mav.addObject("listAuthors", authorService.getAuthorsByContractStatus(contract));
        }
        
        mav.addObject("author", new Author()); // Keeps the "Add New" form working
        return mav;
    }
}