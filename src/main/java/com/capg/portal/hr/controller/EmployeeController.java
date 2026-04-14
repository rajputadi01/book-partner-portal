package com.capg.portal.hr.controller;

import com.capg.portal.hr.entity.Employee;
import com.capg.portal.hr.service.EmployeeService;
import com.capg.portal.creator.entity.Publisher;
import com.capg.portal.catalog.entity.Title;
import com.capg.portal.hr.entity.Job;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return new ResponseEntity<>(employeeService.getAllEmployees(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable String id) {
        return new ResponseEntity<>(employeeService.getEmployeeById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@Valid @RequestBody Employee employee) {
        return new ResponseEntity<>(employeeService.createEmployee(employee), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable String id, @Valid @RequestBody Employee employee) {
        return new ResponseEntity<>(employeeService.updateEmployee(id, employee), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Employee> patchEmployee(@PathVariable String id, @RequestBody Employee updates) {
        return new ResponseEntity<>(employeeService.patchEmployee(id, updates), HttpStatus.OK);
    }

    // --- Filters ---

    @GetMapping("/filter/job")
    public ResponseEntity<List<Employee>> filterByJob(@RequestParam Short jobId) {
        return new ResponseEntity<>(employeeService.getEmployeesByJobId(jobId), HttpStatus.OK);
    }

    @GetMapping("/filter/job-level")
    public ResponseEntity<List<Employee>> filterByJobLevel(@RequestParam Integer maxLvl) {
        return new ResponseEntity<>(employeeService.getEmployeesBelowJobLevel(maxLvl), HttpStatus.OK);
    }

    @GetMapping("/filter/publisher")
    public ResponseEntity<List<Employee>> filterByPublisher(@RequestParam String pubId) {
        return new ResponseEntity<>(employeeService.getEmployeesByPublisher(pubId), HttpStatus.OK);
    }

    // --- Relational Hops ---

    @GetMapping("/{id}/job")
    public ResponseEntity<Job> getEmployeeJob(@PathVariable String id) {
        return new ResponseEntity<>(employeeService.getJobByEmployeeId(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/publisher")
    public ResponseEntity<Publisher> getEmployeePublisher(@PathVariable String id) {
        return new ResponseEntity<>(employeeService.getPublisherByEmployeeId(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/publisher/titles")
    public ResponseEntity<List<Title>> getEmployeePublisherTitles(@PathVariable String id) {
        return new ResponseEntity<>(employeeService.getTitlesByEmployeePublisher(id), HttpStatus.OK);
    }
}