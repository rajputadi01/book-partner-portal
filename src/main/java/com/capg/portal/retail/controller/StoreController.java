package com.capg.portal.retail.controller;

import com.capg.portal.retail.entity.Store;
import com.capg.portal.retail.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    // 1. GET Request: Load the HTML page
    @GetMapping
    public ModelAndView viewStoresPage() {
        ModelAndView mav = new ModelAndView("stores");
        mav.addObject("listStores", storeService.getAllStores());
        mav.addObject("store", new Store());
        return mav;
    }

    // 2. POST Request: Save data from the HTML form
    @PostMapping
    public ModelAndView saveStore(@Valid @ModelAttribute("store") Store store, BindingResult result) {
        
        // 1. Spring Validation Failure
        if (result.hasErrors()) {
            ModelAndView mav = new ModelAndView("stores");
            mav.addObject("store", store);
            mav.addObject("org.springframework.validation.BindingResult.store", result);
            mav.addObject("listStores", storeService.getAllStores());
            return mav;
        }

        // Clean up empty strings before sending to Database
        if (store.getState() != null && store.getState().trim().isEmpty()) {
            store.setState(null);
        }
        if (store.getZip() != null && store.getZip().trim().isEmpty()) {
            store.setZip(null);
        }

        // 2. Database Validation Failure (Catches Duplicate Store IDs)
        try {
            storeService.createStore(store);
        } catch (Exception e) {
            result.rejectValue("storId", "error.store", "Database Error: Store ID already exists.");
            
            ModelAndView mav = new ModelAndView("stores");
            mav.addObject("store", store);
            mav.addObject("org.springframework.validation.BindingResult.store", result);
            mav.addObject("listStores", storeService.getAllStores());
            return mav;
        }

        return new ModelAndView("redirect:/stores");
    }
}