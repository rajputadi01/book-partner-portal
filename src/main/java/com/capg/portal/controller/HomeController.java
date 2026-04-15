package com.capg.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // Use @Controller instead of @RestController
public class HomeController {

    @GetMapping("/")
    public String viewHomePage() {
        return "index"; // Spring automatically looks for src/main/resources/templates/index.html
    }
}