package com.capg.portal.retail.controller;

import com.capg.portal.retail.entity.Sales;
import com.capg.portal.retail.service.SalesService;
import com.capg.portal.retail.service.StoreService; // Sakshi's Service
import com.capg.portal.catalog.service.TitleService; // Aditya's Service
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/sales")
@RequiredArgsConstructor
public class SalesController {

    private final SalesService salesService;
    private final StoreService storeService;
    private final TitleService titleService;
    

    @GetMapping
    public ModelAndView viewSalesPage() {
        ModelAndView mav = new ModelAndView("sales");
        
        mav.addObject("listSales", salesService.getAllSales());
        mav.addObject("listStores", storeService.getAllStores());
        mav.addObject("listTitles", titleService.getAllTitles());
        
        Sales sale = new Sales();
        sale.setOrdDate(LocalDateTime.now());
        mav.addObject("sale", sale);
        
        return mav;
    }

    @PostMapping
    public ModelAndView saveSale(@Valid @ModelAttribute("sale") Sales sale, BindingResult result) {
        
        if (result.hasErrors()) {
            ModelAndView mav = new ModelAndView("sales");
            mav.addObject("sale", sale);
            mav.addObject("org.springframework.validation.BindingResult.sale", result);
            
            mav.addObject("listSales", salesService.getAllSales());
            mav.addObject("listStores", storeService.getAllStores());
            mav.addObject("listTitles", titleService.getAllTitles());
            return mav;
        }

        if (sale.getOrdDate() == null) {
            sale.setOrdDate(LocalDateTime.now());
        }

        try {
            salesService.createSales(sale);
        } catch (Exception e) {
            result.rejectValue("ordNum", "error.sale", "Database Error: This Order Number already exists for this Store and Title.");
            
            ModelAndView mav = new ModelAndView("sales");
            mav.addObject("sale", sale);
            mav.addObject("org.springframework.validation.BindingResult.sale", result);
            
            mav.addObject("listSales", salesService.getAllSales());
            mav.addObject("listStores", storeService.getAllStores());
            mav.addObject("listTitles", titleService.getAllTitles());
            return mav;
        }

        return new ModelAndView("redirect:/sales");
    }
}