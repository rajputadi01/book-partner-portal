package com.capg.portal.hr.controller;

import com.capg.portal.hr.entity.Employee;
import com.capg.portal.hr.service.EmployeeService;
import com.capg.portal.hr.service.JobService;              // Shireen's service
import com.capg.portal.creator.service.PublisherService; // Shireen's service
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final JobService jobService;             
    private final PublisherService publisherService; 

    @GetMapping
    public ModelAndView viewEmployeesPage() {
        ModelAndView mav = new ModelAndView("employees");
        
        // Populate the dropdown menus
        mav.addObject("listEmployees", employeeService.getAllEmployees());
        mav.addObject("listJobs", jobService.getAllJobs()); 
        mav.addObject("listPublishers", publisherService.getAllPublishers()); 
        
        // Default the hire date to right now
        Employee emp = new Employee();
        emp.setHireDate(LocalDateTime.now());
        mav.addObject("employee", emp);
        
        return mav;
    }

    @PostMapping
    public ModelAndView saveEmployee(@Valid @ModelAttribute("employee") Employee employee, BindingResult result) {
        
        // 1. Spring Boot Validation Failure
        if (result.hasErrors()) {
            ModelAndView mav = new ModelAndView("employees");
            mav.addObject("employee", employee);
            mav.addObject("org.springframework.validation.BindingResult.employee", result);
            
            mav.addObject("listEmployees", employeeService.getAllEmployees());
            mav.addObject("listJobs", jobService.getAllJobs());
            mav.addObject("listPublishers", publisherService.getAllPublishers());
            return mav;
        }
        
        // Empty String Cleanup & Date Failsafe
        if (employee.getMinit() != null && employee.getMinit().trim().isEmpty()) {
            employee.setMinit(null);
        }
        if (employee.getHireDate() == null) {
            employee.setHireDate(LocalDateTime.now());
        }

        // 2. Database Validation Failure
        try {
            employeeService.createEmployee(employee);
        } catch (Exception e) {
            result.rejectValue("empId", "error.employee", "Database Error: Employee ID may already exist.");
            
            ModelAndView mav = new ModelAndView("employees");
            mav.addObject("employee", employee);
            mav.addObject("org.springframework.validation.BindingResult.employee", result);
            
            mav.addObject("listEmployees", employeeService.getAllEmployees());
            mav.addObject("listJobs", jobService.getAllJobs());
            mav.addObject("listPublishers", publisherService.getAllPublishers());
            return mav;
        }

        return new ModelAndView("redirect:/employees");
    }
    
 // 3. GET Request: Load the Edit Page for a specific Employee
    @GetMapping("/edit/{id}")
    public ModelAndView viewEditEmployeePage(@PathVariable("id") String id) {
        ModelAndView mav = new ModelAndView("edit-employee"); // Points to the new HTML file
        
        mav.addObject("employee", employeeService.getEmployeeById(id));
        mav.addObject("listJobs", jobService.getAllJobs());             // Required for Job dropdown
        mav.addObject("listPublishers", publisherService.getAllPublishers()); // Required for Publisher dropdown
        
        return mav;
    }

    // 4. POST Request: Save the updated data
    @PostMapping("/update/{id}")
    public ModelAndView saveUpdatedEmployee(@PathVariable("id") String id, @Valid @ModelAttribute("employee") Employee employee, BindingResult result) {
        
        // 1. Spring Validation Failure
        if (result.hasErrors()) {
            ModelAndView mav = new ModelAndView("edit-employee");
            mav.addObject("employee", employee);
            mav.addObject("org.springframework.validation.BindingResult.employee", result);
            
            mav.addObject("listJobs", jobService.getAllJobs()); // Reload dropdown data!
            mav.addObject("listPublishers", publisherService.getAllPublishers()); // Reload dropdown data!
            return mav;
        }

        // Empty String Cleanup & Date Failsafe
        if (employee.getMinit() != null && employee.getMinit().trim().isEmpty()) {
            employee.setMinit(null);
        }
        if (employee.getHireDate() == null) {
            employee.setHireDate(LocalDateTime.now());
        }

        // 2. Database Validation Failure
        try {
            employeeService.updateEmployee(id, employee);
        } catch (Exception e) {
            result.rejectValue("fname", "error.employee", "Database Error: Could not update employee.");
            
            ModelAndView mav = new ModelAndView("edit-employee");
            mav.addObject("employee", employee);
            mav.addObject("org.springframework.validation.BindingResult.employee", result);
            
            mav.addObject("listJobs", jobService.getAllJobs());
            mav.addObject("listPublishers", publisherService.getAllPublishers());
            return mav;
        }

        return new ModelAndView("redirect:/employees");
    }

    // 5. GET Request: Filter the table by Job ID
    @GetMapping("/filter")
    public ModelAndView filterEmployeesByJob(@RequestParam(value = "jobId", required = false) Short jobId) {
        ModelAndView mav = new ModelAndView("employees");
        
        // If the user submits a blank search, return all employees. Otherwise, filter them.
        if (jobId == null) {
            mav.addObject("listEmployees", employeeService.getAllEmployees());
        } else {
            mav.addObject("listEmployees", employeeService.getEmployeesByJobId(jobId));
        }
        
        mav.addObject("listJobs", jobService.getAllJobs()); // Reload dropdown data
        mav.addObject("listPublishers", publisherService.getAllPublishers()); // Reload dropdown data
        
        // Keeps the "Add New" form working with a default date
        Employee emp = new Employee();
        emp.setHireDate(LocalDateTime.now());
        mav.addObject("employee", emp); 
        
        return mav;
    }
}