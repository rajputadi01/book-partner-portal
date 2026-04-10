package com.capg.portal.catalog.controller;

import com.capg.portal.catalog.entity.TitleAuthor;
import com.capg.portal.catalog.service.TitleAuthorService;
import com.capg.portal.catalog.service.TitleService; // Aditya's Service
import com.capg.portal.creator.service.AuthorService; // Sakshi's Service
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/titleauthors")
@RequiredArgsConstructor
public class TitleAuthorController {

    private final TitleAuthorService titleAuthorService;
    private final AuthorService authorService; 
    private final TitleService titleService;  

    @GetMapping
    public ModelAndView viewTitleAuthorsPage() {
        ModelAndView mav = new ModelAndView("titleauthors");
        mav.addObject("listTitleAuthors", titleAuthorService.getAllTitleAuthors());
        
        
        mav.addObject("listAuthors", authorService.getAllAuthors());
        mav.addObject("listTitles", titleService.getAllTitles());
        
        mav.addObject("titleAuthor", new TitleAuthor());
        return mav;
    }

 
    @PostMapping
    public ModelAndView saveTitleAuthor(@Valid @ModelAttribute("titleAuthor") TitleAuthor titleAuthor, BindingResult result) {
        
     
        if (result.hasErrors()) {
            ModelAndView mav = new ModelAndView("titleauthors");
            mav.addObject("titleAuthor", titleAuthor);
            mav.addObject("org.springframework.validation.BindingResult.titleAuthor", result);
            
            mav.addObject("listTitleAuthors", titleAuthorService.getAllTitleAuthors());
            mav.addObject("listAuthors", authorService.getAllAuthors());
            mav.addObject("listTitles", titleService.getAllTitles());
            return mav;
        }


        try {
            titleAuthorService.createTitleAuthor(titleAuthor);
        } catch (Exception e) {
            // Because it's a composite key, we reject the 'author' field to trigger the error display
            result.rejectValue("author", "error.titleAuthor", "Database Error: This Author is already assigned to this Title.");
            
            ModelAndView mav = new ModelAndView("titleauthors");
            mav.addObject("titleAuthor", titleAuthor);
            mav.addObject("org.springframework.validation.BindingResult.titleAuthor", result);
            
            mav.addObject("listTitleAuthors", titleAuthorService.getAllTitleAuthors());
            mav.addObject("listAuthors", authorService.getAllAuthors());
            mav.addObject("listTitles", titleService.getAllTitles());
            return mav;
        }

        return new ModelAndView("redirect:/titleauthors");
    }
}