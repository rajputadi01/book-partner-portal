package com.capg.portal.hr.service;

import com.capg.portal.hr.entity.Job;
import com.capg.portal.hr.entity.Employee;
import com.capg.portal.creator.entity.Publisher;
import com.capg.portal.hr.repository.JobRepository;
import com.capg.portal.hr.repository.EmployeeRepository;
import com.capg.portal.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Objects;

@Service
public class JobService 
{
    private final JobRepository jobRepository;
    private final EmployeeRepository employeeRepository;

    public JobService(JobRepository jobRepository, EmployeeRepository employeeRepository) 
    {
        this.jobRepository = jobRepository;
        this.employeeRepository = employeeRepository;
    }

    public Job createJob(Job job) 
    {
        return jobRepository.save(job);
    }

    public List<Job> getAllJobs() 
    {
        return jobRepository.findAll();
    }

    public Job getJobById(Short id) 
    {
        return jobRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Job not found with ID: " + id));
    }

    public Job updateJob(Short id, Job jobDetails) 
    {
        Job existingJob = getJobById(id);
        existingJob.setJobDesc(jobDetails.getJobDesc());
        existingJob.setMinLvl(jobDetails.getMinLvl());
        existingJob.setMaxLvl(jobDetails.getMaxLvl());
        return jobRepository.save(existingJob);
    }

    public Job patchJob(Short id, Job updates) 
    {
        Job existingJob = getJobById(id);

        if (updates.getJobDesc() != null) 
        {
            existingJob.setJobDesc(updates.getJobDesc());
        }
        if (updates.getMinLvl() != null) 
        {
            existingJob.setMinLvl(updates.getMinLvl());
        }
        if (updates.getMaxLvl() != null) 
        {
            existingJob.setMaxLvl(updates.getMaxLvl());
        }

        return jobRepository.save(existingJob);
    }

    public List<Job> getJobsBetween(Integer min, Integer max) 
    {
        return jobRepository.findByMinLvlGreaterThanEqualAndMaxLvlLessThanEqual(min, max);
    }

    public List<Employee> getEmployeesByJobId(Short jobId) 
    {
        getJobById(jobId);
        return employeeRepository.findByJobJobId(jobId);
    }

    public List<Publisher> getPublishersByJobId(Short jobId) 
    {
        getJobById(jobId);
        return employeeRepository.findByJobJobId(jobId).stream()
                .map(Employee::getPublisher)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
    }

    public Employee assignEmployeeToJob(Short jobId, String empId, Integer jobLvl) 
    {
        Job job = getJobById(jobId);
        Employee employee = employeeRepository.findById(empId).orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + empId));

        employee.setJob(job);
        if (jobLvl != null) 
        {
            employee.setJobLvl(jobLvl);
        }
        
        return employeeRepository.save(employee);
    }
}