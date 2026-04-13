package com.capg.portal.hr.service;

import com.capg.portal.hr.entity.Employee;
import com.capg.portal.hr.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    // Constructor Injection
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

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
    
    public List<Employee> getEmployeesByJobId(Short jobId) {
        return employeeRepository.findByJob_JobId(jobId);
    }

    // New Method: Filter by Job Level Less Than
    public List<Employee> getEmployeesBelowJobLevel(Integer jobLvl) {
        return employeeRepository.findByJobLvlLessThan(jobLvl);
    }

    // New Method: Filter by Hire Date Before
    public List<Employee> getEmployeesHiredBefore(LocalDateTime date) {
        return employeeRepository.findByHireDateBefore(date);
    }

    // New Method: Filter by Publisher ID
    public List<Employee> getEmployeesByPublisher(String pubId) {
        return employeeRepository.findByPublisherPubId(pubId);
    }
}