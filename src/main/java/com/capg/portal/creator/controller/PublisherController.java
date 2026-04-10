package com.capg.portal.creator.controller;

import com.capg.portal.creator.entity.Publisher;
import com.capg.portal.creator.service.PublisherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/publishers")
@RequiredArgsConstructor
public class PublisherController {
    private final PublisherService publisherService;
    
    //get all publishers
    @GetMapping
    public ModelAndView viewPublishersPage() {
        ModelAndView mav = new ModelAndView("publishers");
        mav.addObject("listPublishers", publisherService.getAllPublishers());
        mav.addObject("publisher", new Publisher());
        return mav;
    }

    //create publisher using form
    @PostMapping
    public ModelAndView savePublisher(@Valid @ModelAttribute("publisher") Publisher publisher, BindingResult result) {
    	//validation of input failed
    	if(result.hasErrors()) {
    		ModelAndView mav=new ModelAndView("publishers");
    		mav.addObject("publisher", publisher);
    		mav.addObject("org.springframework.validation.BindingResult.publisher", result);
    		mav.addObject("listPublishers",publisherService.getAllPublishers());
    		return mav;
    	}
    	//force empty country string to default value USA
    	if (publisher.getCountry() == null || publisher.getCountry().trim().isEmpty()) {
            publisher.setCountry("USA");
        }
    	
    	try {
            publisherService.createPublisher(publisher);
        } catch (Exception e) {
            result.rejectValue("pubId", "error.publisher", "Database Error: Publisher ID already exists or violates a rule.");
            
            ModelAndView mav = new ModelAndView("publishers");
            mav.addObject("publisher", publisher);
            mav.addObject("org.springframework.validation.BindingResult.publisher", result);
            mav.addObject("listPublishers", publisherService.getAllPublishers());
            return mav;
        }
    	return new ModelAndView("redirect:/publishers");
    }
}