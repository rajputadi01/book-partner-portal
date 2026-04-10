package com.capg.portal.hr.controller;

import com.capg.portal.hr.entity.Job;
import com.capg.portal.hr.service.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    
    @GetMapping
    public ModelAndView viewJobsPage() {
        ModelAndView mav = new ModelAndView("jobs");
        mav.addObject("listJobs", jobService.getAllJobs());
        mav.addObject("job", new Job());
        return mav;
    }

    
    @PostMapping
    public ModelAndView saveJob(@Valid @ModelAttribute("job") Job job, BindingResult result) {
        
        
        if (result.hasErrors()) {
            ModelAndView mav = new ModelAndView("jobs");
            mav.addObject("job", job);
            mav.addObject("org.springframework.validation.BindingResult.job", result);
            mav.addObject("listJobs", jobService.getAllJobs());
            return mav;
        }

        try {
            jobService.createJob(job);
        } catch (Exception e) {
            result.rejectValue("jobDesc", "error.job", "Database Error: Could not save job.");
            
            ModelAndView mav = new ModelAndView("jobs");
            mav.addObject("job", job);
            mav.addObject("org.springframework.validation.BindingResult.job", result);
            mav.addObject("listJobs", jobService.getAllJobs());
            return mav;
        }

        return new ModelAndView("redirect:/jobs");
    }
}