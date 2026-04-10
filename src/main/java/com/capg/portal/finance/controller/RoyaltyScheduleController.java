package com.capg.portal.finance.controller;

import com.capg.portal.finance.entity.RoyaltySchedule;
import com.capg.portal.finance.service.RoyaltyScheduleService;
import com.capg.portal.catalog.service.TitleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/roysched")
@RequiredArgsConstructor
public class RoyaltyScheduleController {

    private final RoyaltyScheduleService royaltyScheduleService;    
    private final TitleService titleService;

    @GetMapping
    public ModelAndView viewRoyaltyPage() {
        ModelAndView mav = new ModelAndView("royched");
        
        mav.addObject("listRoyched", royaltyScheduleService.getAllRoyaltySchedules());
        mav.addObject("listTitles", titleService.getAllTitles()); 
        
        mav.addObject("royaltySchedule", new RoyaltySchedule());
        return mav;
    }

    @PostMapping
    public ModelAndView saveRoyalty(@Valid @ModelAttribute("royaltySchedule") RoyaltySchedule royaltySchedule, BindingResult result) {
        
        if (result.hasErrors()) {
            ModelAndView mav = new ModelAndView("roysched");
            mav.addObject("royaltySchedule", royaltySchedule);
            mav.addObject("org.springframework.validation.BindingResult.royaltySchedule", result);
            
            mav.addObject("listRoyched", royaltyScheduleService.getAllRoyaltySchedules());
            mav.addObject("listTitles", titleService.getAllTitles());
            return mav;
        }

        try {
            royaltyScheduleService.createRoyaltySchedule(royaltySchedule);
        } catch (Exception e) {
            result.rejectValue("title", "error.royaltySchedule", "Database Error: Could not save schedule.");
            
            ModelAndView mav = new ModelAndView("roysched");
            mav.addObject("royaltySchedule", royaltySchedule);
            mav.addObject("org.springframework.validation.B"
            		+ "indingResult.royaltySchedule", result); 
            
            mav.addObject("listRoyched", royaltyScheduleService.getAllRoyaltySchedules());
            mav.addObject("listTitles", titleService.getAllTitles());
            return mav;
        }

        return new ModelAndView("redirect:/roysched");
    }
}