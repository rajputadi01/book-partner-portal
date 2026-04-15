package com.capg.portal.hr.controller;

import com.capg.portal.catalog.entity.Title;
import com.capg.portal.creator.entity.Publisher;
import com.capg.portal.hr.entity.Employee;
import com.capg.portal.hr.entity.Job;
import com.capg.portal.hr.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = EmployeeController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class EmployeeControllerTest 
{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmployeeService employeeService;

    private Employee validEmployee;
    private Job validJob;
    private Publisher validPublisher;

    @BeforeEach
    public void setup() 
    {
        validJob = new Job();
        validJob.setJobId((short) 1);
        
        validPublisher = new Publisher();
        validPublisher.setPubId("1389");

        validEmployee = new Employee("PMA42628M", "Paolo", "M", "Accorti", validJob, 10, validPublisher, LocalDateTime.now());
    }

    @Test
    public void testGetAllEmployees() throws Exception 
    {
        when(employeeService.getAllEmployees()).thenReturn(Arrays.asList(validEmployee));

        mockMvc.perform(get("/employees"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.size()").value(1))
               .andExpect(jsonPath("$[0].empId").value("PMA42628M"));
    }

    @Test
    public void testGetEmployeeById() throws Exception 
    {
        when(employeeService.getEmployeeById("PMA42628M")).thenReturn(validEmployee);

        mockMvc.perform(get("/employees/{id}", "PMA42628M"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.empId").value("PMA42628M"));
    }

    @Test
    public void testCreateEmployee() throws Exception 
    {
        when(employeeService.createEmployee(any(Employee.class))).thenReturn(validEmployee);

        mockMvc.perform(post("/employees")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(validEmployee)))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.empId").value("PMA42628M"));
    }

    @Test
    public void testUpdateEmployee() throws Exception 
    {
        when(employeeService.updateEmployee(eq("PMA42628M"), any(Employee.class))).thenReturn(validEmployee);

        mockMvc.perform(put("/employees/{id}", "PMA42628M")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(validEmployee)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.fname").value("Paolo"));
    }

    @Test
    public void testPatchEmployee() throws Exception 
    {
        when(employeeService.patchEmployee(eq("PMA42628M"), any(Employee.class))).thenReturn(validEmployee);

        mockMvc.perform(patch("/employees/{id}", "PMA42628M")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(validEmployee)))
               .andExpect(status().isOk());
    }

    @Test
    public void testFilterByJob() throws Exception 
    {
        when(employeeService.getEmployeesByJobId((short) 1)).thenReturn(Arrays.asList(validEmployee));

        mockMvc.perform(get("/employees/filter/job").param("jobId", "1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    public void testFilterByJobLevel() throws Exception 
    {
        when(employeeService.getEmployeesBelowJobLevel(50)).thenReturn(Arrays.asList(validEmployee));

        mockMvc.perform(get("/employees/filter/job-level").param("maxLvl", "50"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    public void testFilterByPublisher() throws Exception 
    {
        when(employeeService.getEmployeesByPublisher("1389")).thenReturn(Arrays.asList(validEmployee));

        mockMvc.perform(get("/employees/filter/publisher").param("pubId", "1389"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    public void testGetEmployeeJob() throws Exception 
    {
        when(employeeService.getJobByEmployeeId("PMA42628M")).thenReturn(validJob);

        mockMvc.perform(get("/employees/{id}/job", "PMA42628M"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.jobId").value(1));
    }

    @Test
    public void testGetEmployeePublisher() throws Exception 
    {
        when(employeeService.getPublisherByEmployeeId("PMA42628M")).thenReturn(validPublisher);

        mockMvc.perform(get("/employees/{id}/publisher", "PMA42628M"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.pubId").value("1389"));
    }

    @Test
    public void testGetEmployeePublisherTitles() throws Exception 
    {
        Title validTitle = new Title();
        validTitle.setTitleId("TL100");
        when(employeeService.getTitlesByEmployeePublisher("PMA42628M")).thenReturn(Arrays.asList(validTitle));

        mockMvc.perform(get("/employees/{id}/publisher/titles", "PMA42628M"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.size()").value(1))
               .andExpect(jsonPath("$[0].titleId").value("TL100"));
    }
}