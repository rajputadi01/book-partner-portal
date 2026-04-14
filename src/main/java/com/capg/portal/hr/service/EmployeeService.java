package com.capg.portal.hr.service;

import com.capg.portal.hr.entity.Employee;
import com.capg.portal.hr.entity.Job;
import com.capg.portal.hr.repository.EmployeeRepository;
import com.capg.portal.hr.repository.JobRepository;
import com.capg.portal.catalog.entity.Title;
import com.capg.portal.catalog.repository.TitleRepository;
import com.capg.portal.creator.entity.Publisher;
import com.capg.portal.exception.ResourceAlreadyExistsException;
import com.capg.portal.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeService {

	private final EmployeeRepository employeeRepository;
	private final TitleRepository titleRepository;
	private final JobRepository jobRepository; 

    public EmployeeService(EmployeeRepository employeeRepository, TitleRepository titleRepository, JobRepository jobRepository) 
    {
        this.employeeRepository = employeeRepository;
        this.titleRepository = titleRepository;
        this.jobRepository = jobRepository;
    }

    public Employee createEmployee(Employee employee) 
    {
        if (employeeRepository.existsById(employee.getEmpId())) {
            throw new ResourceAlreadyExistsException("Employee ID '" + employee.getEmpId() + "' already exists.");
        }
        processDefaults(employee);
        validateJobLevel(employee);
        return employeeRepository.save(employee);
    }

    public List<Employee> getAllEmployees() 
    {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(String id) 
    {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));
    }

    public Employee updateEmployee(String id, Employee details) 
    {
        Employee existing = getEmployeeById(id);
        
        existing.setFname(details.getFname());
        existing.setMinit(details.getMinit());
        existing.setLname(details.getLname());
        existing.setJob(details.getJob());
        existing.setJobLvl(details.getJobLvl());
        existing.setPublisher(details.getPublisher());
        existing.setHireDate(details.getHireDate());

        processDefaults(existing);
        validateJobLevel(existing);
        return employeeRepository.save(existing);
    }

    public Employee patchEmployee(String id, Employee updates) 
    {
        Employee existing = getEmployeeById(id);

        if (updates.getFname() != null) existing.setFname(updates.getFname());
        if (updates.getLname() != null) existing.setLname(updates.getLname());
        if (updates.getMinit() != null) existing.setMinit(updates.getMinit());
        if (updates.getJob() != null) existing.setJob(updates.getJob());
        if (updates.getJobLvl() != null) existing.setJobLvl(updates.getJobLvl());
        if (updates.getPublisher() != null) existing.setPublisher(updates.getPublisher());
        
        processDefaults(existing);
        validateJobLevel(existing);
        return employeeRepository.save(existing);
    }

    public List<Employee> getEmployeesByJobId(Short jobId) 
    {
        return employeeRepository.findByJobJobId(jobId);
    }

    public List<Employee> getEmployeesBelowJobLevel(Integer jobLvl) 
    {
        return employeeRepository.findByJobLvlLessThan(jobLvl);
    }

    public List<Employee> getEmployeesHiredBefore(LocalDateTime date) 
    {
        return employeeRepository.findByHireDateBefore(date);
    }


    public List<Employee> getEmployeesByPublisher(String pubId) 
    {
        return employeeRepository.findByPublisherPubId(pubId);
    }
    
    public Job getJobByEmployeeId(String empId) 
    {
        return getEmployeeById(empId).getJob();
    }
    
    public Publisher getPublisherByEmployeeId(String empId) 
    {
        return getEmployeeById(empId).getPublisher();
    }
    
    public List<Title> getTitlesByEmployeePublisher(String empId) 
    {
        Employee emp = getEmployeeById(empId);
        String pubId = emp.getPublisher().getPubId();
        return titleRepository.findByPublisherPubId(pubId);
    }
    
    private void processDefaults(Employee emp) 
    {
        if (emp.getMinit() != null && emp.getMinit().trim().isEmpty()) emp.setMinit(null);
        if (emp.getHireDate() == null) emp.setHireDate(LocalDateTime.now());
    }

    private void validateJobLevel(Employee emp) 
    {
        if (emp.getJob() == null || emp.getJob().getJobId() == null) return;

        Job actualJob = jobRepository.findById(emp.getJob().getJobId())
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with ID: " + emp.getJob().getJobId()));

        Integer min = (int) actualJob.getMinLvl();
        Integer max = (int) actualJob.getMaxLvl();

        if (emp.getJobLvl() < min || emp.getJobLvl() > max) 
        {
            throw new IllegalArgumentException("Job Level " + emp.getJobLvl() + 
                " is out of range for Job '" + actualJob.getJobDesc() + "' (" + min + "-" + max + ")");
        }
    }
}