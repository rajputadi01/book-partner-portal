package com.capg.portal.hr.controller;

import com.capg.portal.hr.entity.Job;
import com.capg.portal.hr.service.JobService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/jobs")
public class JobController {

    private final JobService jobService;

    // Constructor Injection (Replaces Lombok's @RequiredArgsConstructor)
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    // 1. GET Request: Get all jobs
    @GetMapping
    public ResponseEntity<List<Job>> getAllJobs() {
        return new ResponseEntity<>(jobService.getAllJobs(), HttpStatus.OK); // 200 OK
    }

    // 2. GET Request: Get job by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getJobById(@PathVariable("id") Short id) {
        try {
            Job job = jobService.getJobById(id);
            return new ResponseEntity<>(job, HttpStatus.OK); // 200 OK
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND); // 404 Not Found
        }
    }

    // 3. POST Request: Create a new job
    @PostMapping
    public ResponseEntity<?> createJob(@Valid @RequestBody Job job, BindingResult result) {
        
        // 1. Spring Validation Failure
        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST); // 400 Bad Request
        }

        // 2. Database Save
        try {
            Job savedJob = jobService.createJob(job);
            return new ResponseEntity<>(savedJob, HttpStatus.CREATED); // 201 Created
        } catch (Exception e) {
            return new ResponseEntity<>("Database Error: Could not save job.", HttpStatus.INTERNAL_SERVER_ERROR); // 500 Error
        }
    }

    // 4. PUT Request: Update an existing job
    @PutMapping("/{id}")
    public ResponseEntity<?> updateJob(@PathVariable("id") Short id, @Valid @RequestBody Job job, BindingResult result) {
        
        // 1. Spring Validation Failure
        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST); // 400 Bad Request
        }

        // 2. Database Update
        try {
            Job updatedJob = jobService.updateJob(id, job);
            return new ResponseEntity<>(updatedJob, HttpStatus.OK); // 200 OK
        } catch (Exception e) {
            return new ResponseEntity<>("Database Error: Could not update job.", HttpStatus.INTERNAL_SERVER_ERROR); // 500 Error
        }
    }

    // 5. GET Request: Filter jobs by level range
    @GetMapping("/filter")
    public ResponseEntity<List<Job>> filterJobsByLevels(
            @RequestParam(value = "minLvl", defaultValue = "10") Integer minLvl, 
            @RequestParam(value = "maxLvl", defaultValue = "250") Integer maxLvl) {
        
        return new ResponseEntity<>(jobService.getJobsBetween(minLvl, maxLvl), HttpStatus.OK); // 200 OK
    }
}