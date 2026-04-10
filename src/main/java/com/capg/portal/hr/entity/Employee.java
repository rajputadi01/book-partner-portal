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
    @NotBlank(message = "Employee ID cannot be blank")
    // THE FIX: Regex now allows both AAA12345M and A-A12345M formats, and ensures the first digit is 1-9!
    @Pattern(regexp = "^([A-Z]{3}[1-9][0-9]{4}[FM]|[A-Z]-[A-Z][1-9][0-9]{4}[FM])$", 
             message = "Format: 3 Uppercase OR Letter-Letter, 5 digits (starts 1-9), ends in F/M (e.g. PMA42628M or P-A42628M)")
    @Column(name = "emp_id", columnDefinition = "varchar(10)")
    private String empId;

    @NotBlank(message = "First name is required")
    @Size(max = 20, message = "First name cannot exceed 20 characters")
    @Column(name = "fname", nullable = false, length = 20)
    private String fname;

    @Size(max = 1, message = "Middle initial must be 1 character")
    @Column(name = "minit", columnDefinition = "char(1)")
    private String minit;

    @NotBlank(message = "Last name is required")
    @Size(max = 30, message = "Last name cannot exceed 30 characters")
    @Column(name = "lname", nullable = false, length = 30)
    private String lname;

    // Foreign Key mapping to Jobs table
    @NotNull(message = "Job assignment is required")
    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    @NotNull(message = "Job level is required")
    @Column(name = "job_lvl")
    private Integer jobLvl;

    // Foreign Key mapping to Publishers table
    @NotNull(message = "Publisher assignment is required")
    @ManyToOne
    @JoinColumn(name = "pub_id")
    private Publisher publisher;

    @Column(name = "hire_date")
    private LocalDateTime hireDate;
}