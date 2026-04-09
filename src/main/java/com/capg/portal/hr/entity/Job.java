package com.capg.portal.hr.entity;

import jakarta.persistence.*;
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

    @Column(name = "job_desc", nullable = false, length = 50)
    private String jobDesc;
    
    @Column(name = "min_lvl")
    private Integer minLvl;

    @Column(name = "max_lvl")
    private Integer maxLvl;
}