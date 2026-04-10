package com.capg.portal.hr.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.capg.portal.hr.entity.Job;
import com.capg.portal.hr.repository.JobRepository;
import com.capg.portal.hr.service.JobService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {
	private final JobService jobService;
	
	@PostMapping
	ResponseEntity<Job>createJob(@Valid @RequestBody Job job){
		return new ResponseEntity<>(jobService.createJob(job),HttpStatus.CREATED);
	}
	
	 
	 
}
