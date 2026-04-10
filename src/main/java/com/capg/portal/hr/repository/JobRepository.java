package com.capg.portal.hr.repository;

import com.capg.portal.hr.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<Job, Short> 
{
	
}