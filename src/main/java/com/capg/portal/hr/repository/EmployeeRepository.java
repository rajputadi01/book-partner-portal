package com.capg.portal.hr.repository;

import com.capg.portal.hr.entity.Employee;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> 
{
	List<Employee> findByJob_JobId(Short jobId);
}