package com.capg.portal.hr.service;

import com.capg.portal.hr.entity.Job;
import com.capg.portal.hr.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobService  {

    private final JobRepository jobRepository;

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
}