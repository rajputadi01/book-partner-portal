package com.capg.portal.hr.repository;

import com.capg.portal.hr.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {
    
    // Existing Method
    List<Employee> findByJob_JobId(Short jobId);
    
    // New Method: Employees below a certain job level
    List<Employee> findByJobLvlLessThan(Integer jobLvl);
    
    // New Method: Employees hired before a specific date
    List<Employee> findByHireDateBefore(LocalDateTime hireDate);
    
    // New Method: Employees belonging to a specific publisher
    List<Employee> findByPublisherPubId(String pubId);
}