package com.capg.portal.controller; // Ensure this matches your package structure

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class HomeController {

    @GetMapping("/")
    public ModelAndView viewHomePage() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("index"); // Tells Spring to look for index.html
        return mav;
    }
}