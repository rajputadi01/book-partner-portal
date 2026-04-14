package com.capg.portal.hr.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "jobs")
public class Job 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_id", columnDefinition = "smallint")
    private Short jobId;

    @NotBlank(message = "Job description is required")
    @Size(max = 50, message = "Description cannot exceed 50 characters")
    @Column(name = "job_desc", nullable = false, length = 50)
    private String jobDesc = "New Position - title not formalized yet";
    
    @NotNull(message = "Minimum level is required")
    @Min(value = 10, message = "Minimum level must be at least 10")
    @Column(name = "min_lvl")
    private Integer minLvl;

    @NotNull(message = "Maximum level is required")
    @Max(value = 250, message = "Maximum level cannot exceed 250")
    @Column(name = "max_lvl")
    private Integer maxLvl;

    public Job() 
    {
    	
    }

    public Job(Short jobId, String jobDesc, Integer minLvl, Integer maxLvl) 
    {
        this.jobId = jobId;
        this.jobDesc = jobDesc;
        this.minLvl = minLvl;
        this.maxLvl = maxLvl;
    }

    public Short getJobId() 
    { 
    	return jobId; 
    }
    
    public void setJobId(Short jobId) 
    { 
    	this.jobId = jobId; 
    }

    public String getJobDesc() 
    { 
    	return jobDesc; 
    }
    
    public void setJobDesc(String jobDesc) 
    { 
    	this.jobDesc = jobDesc; 
    }

    public Integer getMinLvl() 
    { 
    	return minLvl; 
    }
    
    public void setMinLvl(Integer minLvl) 
    { 
    	this.minLvl = minLvl; 
    }
    
    public Integer getMaxLvl() 
    { 
    	return maxLvl; 
    }
    
    public void setMaxLvl(Integer maxLvl) 
    { 
    	this.maxLvl = maxLvl;
    }
}