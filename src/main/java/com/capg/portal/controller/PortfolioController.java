package com.capg.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PortfolioController 
{
    @GetMapping("/portfolio/{memberName}")
    public String viewPortfolio(@PathVariable String memberName, Model model) 
    {
        // Pass the name (e.g., 'aditya') to the HTML so Thymeleaf knows which links to show
        model.addAttribute("memberName", memberName.toLowerCase());
        
        return "member-portfolio";
    }
}