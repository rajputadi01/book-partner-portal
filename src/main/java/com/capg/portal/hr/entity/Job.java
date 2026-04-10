package com.capg.portal.hr.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "jobs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Job 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_id", columnDefinition = "smallint")
    private Short jobId;


    @NotBlank(message = "Job description is required")
    @Size(max = 50, message = "Description cannot exceed 50 characters")
    @Column(name = "job_desc", nullable = false, length = 50)
    private String jobDesc;
    
    @NotNull(message = "Minimum level is required")
    @Min(value = 10, message = "Minimum level must be at least 10")
    @Column(name = "min_lvl")
    private Integer minLvl;

    @NotNull(message = "Maximum level is required")
    @Max(value = 250, message = "Maximum level cannot exceed 250")
    @Column(name = "max_lvl")
    private Integer maxLvl;
}