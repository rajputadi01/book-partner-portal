package com.capg.portal.hr.controller;

import com.capg.portal.hr.entity.Job;
import com.capg.portal.hr.entity.Employee;
import com.capg.portal.creator.entity.Publisher;
import com.capg.portal.hr.service.JobService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(JobController.class)
@AutoConfigureMockMvc(addFilters = false)
class JobControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JobService jobService;

    @Autowired
    private ObjectMapper objectMapper;

    // TC01 - CREATE JOB
    @Test
    void TC01_createJob_shouldReturnCreatedJob() throws Exception {

        Job input = new Job(null, "Publisher Manager", 20, 120);
        Job saved = new Job((short) 1, "Publisher Manager", 20, 120);

        when(jobService.createJob(any(Job.class))).thenReturn(saved);

        mockMvc.perform(post("/jobs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.jobId").value(1))
                .andExpect(jsonPath("$.jobDesc").value("Publisher Manager"));
    }

    // TC02 - UPDATE JOB
    @Test
    void TC02_updateJob_shouldReturnUpdatedJob() throws Exception {

        Job input = new Job(null, "Senior Publisher Editor", 30, 180);
        Job updated = new Job((short) 1, "Senior Publisher Editor", 30, 180);

        when(jobService.updateJob(eq((short) 1), any(Job.class))).thenReturn(updated);

        mockMvc.perform(put("/jobs/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jobDesc").value("Senior Publisher Editor"));
    }

    // TC03 - GET JOB BY ID
    @Test
    void TC03_getJobById_shouldReturnJob() throws Exception {

        Job job = new Job((short) 1, "Assistant Publisher", 15, 100);

        when(jobService.getJobById((short) 1)).thenReturn(job);

        mockMvc.perform(get("/jobs/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jobDesc").value("Assistant Publisher"));
    }

    // TC04 - PATCH JOB
    @Test
    void TC04_patchJob_shouldReturnPatchedJob() throws Exception {

        Job patch = new Job();
        patch.setJobDesc("Chief Publishing Officer");

        Job result = new Job((short) 1, "Chief Publishing Officer", 10, 200);

        when(jobService.patchJob(eq((short) 1), any(Job.class))).thenReturn(result);

        mockMvc.perform(patch("/jobs/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patch)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jobDesc").value("Chief Publishing Officer"));
    }

    // TC05 - GET ALL JOBS
    @Test
    void TC05_getAllJobs_shouldReturnList() throws Exception {

        Job job = new Job((short) 1, "Publishing Coordinator", 10, 80);

        when(jobService.getAllJobs()).thenReturn(List.of(job));

        mockMvc.perform(get("/jobs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    // TC06 - FILTER JOBS
    @Test
    void TC06_filterJobs_shouldReturnList() throws Exception {

        Job job = new Job((short) 1, "Editorial Publisher", 25, 150);

        when(jobService.getJobsBetween(10, 200))
                .thenReturn(List.of(job));

        mockMvc.perform(get("/jobs/filter")
                        .param("minLvl", "10")
                        .param("maxLvl", "200"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    // TC07 - GET EMPLOYEES BY JOB
    @Test
    void TC07_getEmployees_shouldReturnList() throws Exception {

        Employee emp = new Employee();
        emp.setEmpId("PUB001");

        when(jobService.getEmployeesByJobId((short) 1))
                .thenReturn(List.of(emp));

        mockMvc.perform(get("/jobs/1/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].empId").value("PUB001"));
    }

    // TC08 - GET PUBLISHERS BY JOB
    @Test
    void TC08_getPublishers_shouldReturnList() throws Exception {

        Publisher pub = new Publisher();

        when(jobService.getPublishersByJobId((short) 1))
                .thenReturn(List.of(pub));

        mockMvc.perform(get("/jobs/1/publishers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    // TC09 - ASSIGN EMPLOYEE TO JOB
    @Test
    void TC09_assignEmployee_shouldReturnEmployee() throws Exception {

        Employee emp = new Employee();
        emp.setEmpId("PUB_EMP_01");

        when(jobService.assignEmployeeToJob(eq((short) 1), eq("PUB_EMP_01"), any()))
                .thenReturn(emp);

        mockMvc.perform(patch("/jobs/1/employees/PUB_EMP_01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.empId").value("PUB_EMP_01"));
    }
}