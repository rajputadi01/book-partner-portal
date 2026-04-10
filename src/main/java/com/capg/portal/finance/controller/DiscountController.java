package com.capg.portal.finance.controller;

import com.capg.portal.finance.entity.Discount;
import com.capg.portal.finance.service.DiscountService;
import com.capg.portal.retail.service.StoreService; 
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/discounts")
@RequiredArgsConstructor
public class DiscountController {

    private final DiscountService discountService;
    private final StoreService storeService; 


    @GetMapping
    public ModelAndView viewDiscountsPage() {
        ModelAndView mav = new ModelAndView("discounts");
        mav.addObject("listDiscounts", discountService.getAllDiscounts());
        
 
        mav.addObject("listStores", storeService.getAllStores()); 
        
        mav.addObject("discount", new Discount());
        return mav;
    }

    @PostMapping
    public ModelAndView saveDiscount(@Valid @ModelAttribute("discount") Discount discount, BindingResult result) {
        
       
        if (result.hasErrors()) {
            ModelAndView mav = new ModelAndView("discounts");
            mav.addObject("discount", discount);
            mav.addObject("org.springframework.validation.BindingResult.discount", result);
            mav.addObject("listDiscounts", discountService.getAllDiscounts());
            mav.addObject("listStores", storeService.getAllStores()); 
            return mav;
        }

      
        try {
            discountService.createDiscount(discount);
        } catch (Exception e) {
            result.rejectValue("discountType", "error.discount", "Database Error: Discount Type already exists.");
            
            ModelAndView mav = new ModelAndView("discounts");
            mav.addObject("discount", discount);
            mav.addObject("org.springframework.validation.BindingResult.discount", result);
            mav.addObject("listDiscounts", discountService.getAllDiscounts());
            mav.addObject("listStores", storeService.getAllStores());
            return mav;
        }

        return new ModelAndView("redirect:/discounts");
    }
}