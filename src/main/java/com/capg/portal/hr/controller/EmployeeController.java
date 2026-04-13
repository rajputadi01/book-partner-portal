package com.capg.portal.hr.controller;

import com.capg.portal.hr.entity.Employee;
import com.capg.portal.hr.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    // Constructor Injection
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // 1. GET Request: Get all employees
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return new ResponseEntity<>(employeeService.getAllEmployees(), HttpStatus.OK); // 200 OK
    }

    // 2. GET Request: Get employee by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getEmployeeById(@PathVariable("id") String id) {
        try {
            Employee employee = employeeService.getEmployeeById(id);
            return new ResponseEntity<>(employee, HttpStatus.OK); // 200 OK
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND); // 404 Not Found
        }
    }

    // 3. POST Request: Create a new employee
    @PostMapping
    public ResponseEntity<?> createEmployee(@Valid @RequestBody Employee employee, BindingResult result) {
        
        // 1. Validation Check
        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
        
        // Empty String Cleanup & Date Failsafe
        if (employee.getMinit() != null && employee.getMinit().trim().isEmpty()) {
            employee.setMinit(null);
        }
        if (employee.getHireDate() == null) {
            employee.setHireDate(LocalDateTime.now());
        }

        // 2. Save to Database
        try {
            Employee savedEmployee = employeeService.createEmployee(employee);
            return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED); // 201 Created
        } catch (Exception e) {
            return new ResponseEntity<>("Database Error: Employee ID may already exist or violates constraints.", HttpStatus.CONFLICT); // 409 Conflict
        }
    }

    // 4. PUT Request: Update an existing employee
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable("id") String id, @Valid @RequestBody Employee employee, BindingResult result) {
        
        // 1. Validation Check
        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST); // 400 Bad Request
        }

        // Empty String Cleanup & Date Failsafe
        if (employee.getMinit() != null && employee.getMinit().trim().isEmpty()) {
            employee.setMinit(null);
        }
        if (employee.getHireDate() == null) {
            employee.setHireDate(LocalDateTime.now());
        }

        // 2. Update Database
        try {
            Employee updatedEmployee = employeeService.updateEmployee(id, employee);
            return new ResponseEntity<>(updatedEmployee, HttpStatus.OK); // 200 OK
        } catch (Exception e) {
            return new ResponseEntity<>("Database Error: Could not update employee.", HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error
        }
    }

    // 5. GET Request: Filter by Job ID
    @GetMapping("/filter/job")
    public ResponseEntity<List<Employee>> filterEmployeesByJobId(@RequestParam("jobId") Short jobId) 
    {
        return new ResponseEntity<>(employeeService.getEmployeesByJobId(jobId), HttpStatus.OK); // 200 OK
    }

    // 6. GET Request: Filter by Job Level Less Than
    @GetMapping("/filter/job-level")
    public ResponseEntity<List<Employee>> filterEmployeesBelowJobLevel(@RequestParam("maxLvl") Integer maxLvl) {
        return new ResponseEntity<>(employeeService.getEmployeesBelowJobLevel(maxLvl), HttpStatus.OK); // 200 OK
    }

    // 7. GET Request: Filter by Hire Date Before
    // Example Postman URL format: /filter/hire-date?beforeDate=2024-01-01T00:00:00
    @GetMapping("/filter/hire-date")
    public ResponseEntity<List<Employee>> filterEmployeesHiredBefore(
            @RequestParam("beforeDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime beforeDate) {
        return new ResponseEntity<>(employeeService.getEmployeesHiredBefore(beforeDate), HttpStatus.OK); // 200 OK
    }

    // 8. GET Request: Filter by Publisher ID
    @GetMapping("/filter/publisher")
    public ResponseEntity<List<Employee>> filterEmployeesByPublisher(@RequestParam("pubId") String pubId) {
        return new ResponseEntity<>(employeeService.getEmployeesByPublisher(pubId), HttpStatus.OK); // 200 OK
    }
}