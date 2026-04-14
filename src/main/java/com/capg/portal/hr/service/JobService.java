package com.capg.portal.hr.service;

import com.capg.portal.hr.entity.Job;
import com.capg.portal.hr.entity.Employee;
import com.capg.portal.hr.repository.JobRepository;
import com.capg.portal.hr.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final EmployeeRepository employeeRepository;

    // Constructor Injection (Replaces Lombok's @RequiredArgsConstructor)
    public JobService(JobRepository jobRepository, EmployeeRepository employeeRepository) {
        this.jobRepository = jobRepository;
        this.employeeRepository = employeeRepository;
    }

    public Job createJob(Job job) {
        return jobRepository.save(job);
    }

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public Job getJobById(Short id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found with ID: " + id));
    }

    public Job updateJob(Short id, Job jobDetails) {
        Job existingJob = getJobById(id);
        existingJob.setJobDesc(jobDetails.getJobDesc());
        existingJob.setMinLvl(jobDetails.getMinLvl());
        existingJob.setMaxLvl(jobDetails.getMaxLvl());
        return jobRepository.save(existingJob);
    }
    
    public List<Job> getJobsBetween(Integer min, Integer max) {
        return jobRepository.findByMinLvlGreaterThanEqualAndMaxLvlLessThanEqual(min, max);
    }

    // Assign an existing employee to a job (optionally updating job level)
    public Employee assignEmployeeToJob(Short jobId, String empId, Integer jobLvl) {
        Job job = getJobById(jobId);
        Employee employee = employeeRepository.findById(empId)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + empId));

        employee.setJob(job);
        if (jobLvl != null) {
            employee.setJobLvl(jobLvl);
        }
        return employeeRepository.save(employee);
    }

    // Change job (and/or level) for an employee
    public Employee changeEmployeeJob(String empId, Short jobId, Integer jobLvl) {
        return assignEmployeeToJob(jobId, empId, jobLvl);
    }
}
