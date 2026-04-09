package com.capg.portal.hr.entity;

import com.capg.portal.creator.entity.Publisher;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "employee")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

	@Id
    @Size(min = 9, max = 9, message = "Employee ID must be exactly 9 characters")
    @Column(name = "emp_id", columnDefinition = "char(9)")
    private String empId;

    @NotBlank(message = "First name is required")
    @Column(name = "fname", nullable = false, length = 20)
    private String fname;

    @Column(name = "minit", columnDefinition = "char(1)")
    private String minit;

    @NotBlank(message = "Last name is required")
    @Column(name = "lname", nullable = false, length = 30)
    private String lname;

    // Foreign Key mapping to Jobs table
    @NotNull(message = "Job assignment is required")
    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    @NotNull(message = "Job level is required")
    @Column(name = "job_lvl", columnDefinition = "tinyint")
    private Integer jobLvl;

    // Foreign Key mapping to Publishers table
    @NotNull(message = "Publisher assignment is required")
    @ManyToOne
    @JoinColumn(name = "pub_id")
    private Publisher publisher;

    @Column(name = "hire_date")
    private LocalDateTime hireDate;
}