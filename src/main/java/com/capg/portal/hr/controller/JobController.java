package com.capg.portal.hr.controller;

import com.capg.portal.hr.entity.Job;
import com.capg.portal.hr.entity.Employee;
import com.capg.portal.creator.entity.Publisher;
import com.capg.portal.hr.service.JobService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/jobs")
public class JobController 
{
    private final JobService jobService;

    public JobController(JobService jobService) 
    {
        this.jobService = jobService;
    }

    @GetMapping
    public ResponseEntity<List<Job>> getAllJobs() 
    {
        return new ResponseEntity<>(jobService.getAllJobs(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable("id") Short id) 
    {
        Job job = jobService.getJobById(id);
        return new ResponseEntity<>(job, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Job> createJob(@Valid @RequestBody Job job) 
    {
        Job savedJob = jobService.createJob(job);
        return new ResponseEntity<>(savedJob, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Job> updateJob(@PathVariable("id") Short id, @Valid @RequestBody Job job) 
    {
        Job updatedJob = jobService.updateJob(id, job);
        return new ResponseEntity<>(updatedJob, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Job> patchJob(@PathVariable("id") Short id, @RequestBody Job updates) 
    {
        Job patchedJob = jobService.patchJob(id, updates);
        return new ResponseEntity<>(patchedJob, HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Job>> filterJobsByLevels(@RequestParam(value = "minLvl", defaultValue = "10") Integer minLvl, @RequestParam(value = "maxLvl", defaultValue = "250") Integer maxLvl) 
    {
        return new ResponseEntity<>(jobService.getJobsBetween(minLvl, maxLvl), HttpStatus.OK);
    }

    @GetMapping("/{id}/employees")
    public ResponseEntity<List<Employee>> getEmployeesByJob(@PathVariable("id") Short id) 
    {
        return new ResponseEntity<>(jobService.getEmployeesByJobId(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/publishers")
    public ResponseEntity<List<Publisher>> getPublishersByJob(@PathVariable("id") Short id) 
    {
        return new ResponseEntity<>(jobService.getPublishersByJobId(id), HttpStatus.OK);
    }

    @PatchMapping("/{jobId}/employees/{empId}")
    public ResponseEntity<Employee> assignEmployeeToJob(@PathVariable("jobId") Short jobId, @PathVariable("empId") String empId, @RequestBody(required = false) JobLevelUpdateRequest request) 
    {
        Integer jobLvl = (request != null) ? request.jobLvl() : null;
        Employee updatedEmployee = jobService.assignEmployeeToJob(jobId, empId, jobLvl);
        return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
    }

    public record JobLevelUpdateRequest(Integer jobLvl) {}
}