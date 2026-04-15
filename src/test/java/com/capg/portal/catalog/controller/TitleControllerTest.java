package com.capg.portal.catalog.controller;

import com.capg.portal.catalog.entity.Title;
import com.capg.portal.catalog.entity.TitleAuthor;
import com.capg.portal.catalog.service.TitleService;
import com.capg.portal.creator.entity.Author;
import com.capg.portal.creator.entity.Publisher;
import com.capg.portal.finance.entity.RoyaltySchedule;
import com.capg.portal.retail.entity.Sales;
import com.capg.portal.retail.entity.Store;
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

@WebMvcTest(controllers = TitleController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class TitleControllerTest 
{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TitleService titleService;

    private Title validTitle;
    private Publisher validPublisher;

    @BeforeEach
    public void setup() 
    {
        validPublisher = new Publisher();
        validPublisher.setPubId("1389");

        validTitle = new Title("BU1032", "The Busy Executive's Database Guide", "business", validPublisher, 19.99, 5000.0, 10, 4095, "Notes", LocalDateTime.now());
    }

    @Test
    public void testGetAllTitles() throws Exception 
    {
        when(titleService.getAllTitles()).thenReturn(Arrays.asList(validTitle));

        mockMvc.perform(get("/titles"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.size()").value(1))
               .andExpect(jsonPath("$[0].titleId").value("BU1032"));
    }

    @Test
    public void testGetTitleById() throws Exception 
    {
        when(titleService.getTitleById("BU1032")).thenReturn(validTitle);

        mockMvc.perform(get("/titles/{id}", "BU1032"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.titleId").value("BU1032"));
    }

    @Test
    public void testCreateTitle() throws Exception 
    {
        when(titleService.createTitle(any(Title.class))).thenReturn(validTitle);

        mockMvc.perform(post("/titles")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(validTitle)))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.titleId").value("BU1032"));
    }

    @Test
    public void testUpdateTitle() throws Exception 
    {
        when(titleService.updateTitle(eq("BU1032"), any(Title.class))).thenReturn(validTitle);

        mockMvc.perform(put("/titles/{id}", "BU1032")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(validTitle)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.titleName").value("The Busy Executive's Database Guide"));
    }

    @Test
    public void testPatchTitle() throws Exception 
    {
        when(titleService.patchTitle(eq("BU1032"), any(Title.class))).thenReturn(validTitle);

        mockMvc.perform(patch("/titles/{id}", "BU1032")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(validTitle)))
               .andExpect(status().isOk());
    }

    @Test
    public void testFilterTitlesByPrice() throws Exception 
    {
        when(titleService.getTitlesByPriceLessThan(20.0)).thenReturn(Arrays.asList(validTitle));

        mockMvc.perform(get("/titles/filter/price").param("maxPrice", "20.0"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    public void testFilterTitlesByType() throws Exception 
    {
        when(titleService.getTitlesByType("business")).thenReturn(Arrays.asList(validTitle));

        mockMvc.perform(get("/titles/filter/type").param("type", "business"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    public void testFilterTitlesByPublisher() throws Exception 
    {
        when(titleService.getTitlesByPublisher("1389")).thenReturn(Arrays.asList(validTitle));

        mockMvc.perform(get("/titles/filter/publisher").param("pubId", "1389"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    public void testGetPublisherByTitle() throws Exception 
    {
        when(titleService.getPublisherByTitleId("BU1032")).thenReturn(validPublisher);

        mockMvc.perform(get("/titles/{id}/publisher", "BU1032"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.pubId").value("1389"));
    }

    @Test
    public void testGetSalesByTitle() throws Exception 
    {
        Sales sale = new Sales();
        sale.setOrdNum("ORD123");
        when(titleService.getSalesByTitleId("BU1032")).thenReturn(Arrays.asList(sale));

        mockMvc.perform(get("/titles/{id}/sales", "BU1032"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    public void testGetRoyaltiesByTitle() throws Exception 
    {
        when(titleService.getRoyaltiesByTitleId("BU1032")).thenReturn(Arrays.asList(new RoyaltySchedule()));

        mockMvc.perform(get("/titles/{id}/royalties", "BU1032"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    public void testGetTitleAuthorsByTitle() throws Exception 
    {
        when(titleService.getTitleAuthorsByTitleId("BU1032")).thenReturn(Arrays.asList(new TitleAuthor()));

        mockMvc.perform(get("/titles/{id}/title-authors", "BU1032"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    public void testGetAuthorsByTitle() throws Exception 
    {
        when(titleService.getAuthorsByTitleId("BU1032")).thenReturn(Arrays.asList(new Author()));

        mockMvc.perform(get("/titles/{id}/authors", "BU1032"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    public void testGetStoresByTitle() throws Exception 
    {
        when(titleService.getStoresByTitleId("BU1032")).thenReturn(Arrays.asList(new Store()));

        mockMvc.perform(get("/titles/{id}/stores", "BU1032"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.size()").value(1));
    }
}