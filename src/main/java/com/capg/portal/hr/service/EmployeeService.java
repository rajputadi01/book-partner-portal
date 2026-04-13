package com.capg.portal.hr.service;

import com.capg.portal.hr.entity.Employee;
import com.capg.portal.hr.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(String id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + id));
    }

    public Employee updateEmployee(String id, Employee employeeDetails) {
        Employee existingEmployee = getEmployeeById(id);
        existingEmployee.setFname(employeeDetails.getFname());
        existingEmployee.setMinit(employeeDetails.getMinit());
        existingEmployee.setLname(employeeDetails.getLname());
        existingEmployee.setJob(employeeDetails.getJob());
        existingEmployee.setJobLvl(employeeDetails.getJobLvl());
        existingEmployee.setPublisher(employeeDetails.getPublisher());
        existingEmployee.setHireDate(employeeDetails.getHireDate());
        return employeeRepository.save(existingEmployee);
    }
    
    // Add this to EmployeeService.java
    public List<Employee> getEmployeesByJobId(Short jobId) 
    {
        return employeeRepository.findByJob_JobId(jobId);
    }
}