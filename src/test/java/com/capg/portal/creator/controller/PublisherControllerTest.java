package com.capg.portal.creator.controller;

import com.capg.portal.creator.entity.Publisher;
import com.capg.portal.creator.entity.Author;
import com.capg.portal.creator.service.PublisherService;
import com.capg.portal.catalog.entity.Title;
import com.capg.portal.hr.entity.Employee;
import com.capg.portal.retail.entity.Store;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PublisherController.class)
@AutoConfigureMockMvc(addFilters = false)
class PublisherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PublisherService publisherService;

    @Autowired
    private ObjectMapper objectMapper;

    // TC01 - Get all publishers
    @Test
    void TC01_getAllPublishers() throws Exception {

        when(publisherService.getAllPublishers())
                .thenReturn(List.of(new Publisher("1389", "ABC Pub", "Pune", "MH", "India")));

        mockMvc.perform(get("/publishers"))
                .andExpect(status().isOk());
    }

    // TC02 - Get publisher by ID
    @Test
    void TC02_getPublisherById() throws Exception {

        Publisher p = new Publisher("1389", "ABC Pub", "Pune", "MH", "India");

        when(publisherService.getPublisherById("1389")).thenReturn(p);

        mockMvc.perform(get("/publishers/1389"))
                .andExpect(status().isOk());
    }

    // TC03 - Create publisher
    @Test
    void TC03_createPublisher() throws Exception {

        Publisher p = new Publisher("1389", "ABC Pub", "Pune", "MH", "India");

        when(publisherService.createPublisher(any(Publisher.class))).thenReturn(p);

        mockMvc.perform(post("/publishers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(p)))
                .andExpect(status().isCreated());
    }

    // TC04 - Update publisher
    @Test
    void TC04_updatePublisher() throws Exception {

        Publisher p = new Publisher("1389", "Updated Pub", "Mumbai", "MH", "India");

        when(publisherService.updatePublisher(eq("1389"), any(Publisher.class)))
                .thenReturn(p);

        mockMvc.perform(put("/publishers/1389")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(p)))
                .andExpect(status().isOk());
    }

    // TC05 - Patch publisher
    @Test
    void TC05_patchPublisher() throws Exception {

        Publisher patch = new Publisher();
        patch.setCity("Delhi");

        Publisher updated = new Publisher("1389", "ABC Pub", "Delhi", "MH", "India");

        when(publisherService.patchPublisher(eq("1389"), any(Publisher.class)))
                .thenReturn(updated);

        mockMvc.perform(patch("/publishers/1389")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patch)))
                .andExpect(status().isOk());
    }

    // TC06 - Filter by city
    @Test
    void TC06_filterByCity() throws Exception {

        when(publisherService.getPublishersByCity("Pune"))
                .thenReturn(List.of(new Publisher("1389", "ABC Pub", "Pune", "MH", "India")));

        mockMvc.perform(get("/publishers/filter/city")
                        .param("city", "Pune"))
                .andExpect(status().isOk());
    }

    // TC07 - Filter by state
    @Test
    void TC07_filterByState() throws Exception {

        when(publisherService.getPublishersByState("MH"))
                .thenReturn(List.of());

        mockMvc.perform(get("/publishers/filter/state")
                        .param("state", "MH"))
                .andExpect(status().isOk());
    }

    // TC08 - Filter by country
    @Test
    void TC08_filterByCountry() throws Exception {

        when(publisherService.getPublishersByCountry("India"))
                .thenReturn(List.of());

        mockMvc.perform(get("/publishers/filter/country")
                        .param("country", "India"))
                .andExpect(status().isOk());
    }

    // TC09 - Get employees by publisher
    @Test
    void TC09_getEmployeesByPublisher() throws Exception {

        when(publisherService.getEmployeesByPublisherId("1389"))
                .thenReturn(List.of(new Employee()));

        mockMvc.perform(get("/publishers/1389/employees"))
                .andExpect(status().isOk());
    }

    // TC10 - Get titles by publisher
    @Test
    void TC10_getTitlesByPublisher() throws Exception {

        when(publisherService.getTitlesByPublisherId("1389"))
                .thenReturn(List.of(new Title()));

        mockMvc.perform(get("/publishers/1389/titles"))
                .andExpect(status().isOk());
    }

    // TC11 - Get authors by publisher
    @Test
    void TC11_getAuthorsByPublisher() throws Exception {

        when(publisherService.getAuthorsByPublisherId("1389"))
                .thenReturn(List.of(new Author()));

        mockMvc.perform(get("/publishers/1389/authors"))
                .andExpect(status().isOk());
    }

    // TC12 - Get stores by publisher
    @Test
    void TC12_getStoresByPublisher() throws Exception {

        when(publisherService.getStoresByPublisherId("1389"))
                .thenReturn(List.of(new Store()));

        mockMvc.perform(get("/publishers/1389/stores"))
                .andExpect(status().isOk());
    }
}