package com.capg.portal.hr.entity;

import com.capg.portal.creator.entity.Publisher;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @NotBlank(message = "Employee ID cannot be blank")
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

    // Default Constructor
    public Employee() {
    }

    // Parameterized Constructor
    public Employee(String empId, String fname, String minit, String lname, Job job, Integer jobLvl, Publisher publisher, LocalDateTime hireDate) {
        this.empId = empId;
        this.fname = fname;
        this.minit = minit;
        this.lname = lname;
        this.job = job;
        this.jobLvl = jobLvl;
        this.publisher = publisher;
        this.hireDate = hireDate;
    }

    // Getters and Setters
    public String getEmpId() { return empId; }
    public void setEmpId(String empId) { this.empId = empId; }

    public String getFname() { return fname; }
    public void setFname(String fname) { this.fname = fname; }

    public String getMinit() { return minit; }
    public void setMinit(String minit) { this.minit = minit; }

    public String getLname() { return lname; }
    public void setLname(String lname) { this.lname = lname; }

    public Job getJob() { return job; }
    public void setJob(Job job) { this.job = job; }

    public Integer getJobLvl() { return jobLvl; }
    public void setJobLvl(Integer jobLvl) { this.jobLvl = jobLvl; }

    public Publisher getPublisher() { return publisher; }
    public void setPublisher(Publisher publisher) { this.publisher = publisher; }

    public LocalDateTime getHireDate() { return hireDate; }
    public void setHireDate(LocalDateTime hireDate) { this.hireDate = hireDate; }
}