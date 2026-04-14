package com.capg.portal.hr.repository;

import com.capg.portal.hr.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> 
{
    List<Employee> findByJobJobId(Short jobId);
    List<Employee> findByJobLvlLessThan(Integer jobLvl);
    List<Employee> findByHireDateBefore(LocalDateTime hireDate);
    List<Employee> findByPublisherPubId(String pubId);
}