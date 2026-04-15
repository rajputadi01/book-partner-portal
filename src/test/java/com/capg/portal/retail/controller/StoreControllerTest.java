package com.capg.portal.retail.controller;

import com.capg.portal.catalog.entity.Title;
import com.capg.portal.creator.entity.Author;
import com.capg.portal.creator.entity.Publisher;
import com.capg.portal.finance.entity.Discount;
import com.capg.portal.retail.entity.Sales;
import com.capg.portal.retail.entity.Store;
import com.capg.portal.retail.service.StoreService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = StoreController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class StoreControllerTest 
{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StoreService storeService;

    private Store validStore;

    @BeforeEach
    public void setup() 
    {
        validStore = new Store("7066", "Barnum's", "567 Pasadena Ave.", "Tustin", "CA", "92789");
    }

    @Test
    public void testGetAllStores() throws Exception 
    {
        when(storeService.getAllStores()).thenReturn(Arrays.asList(validStore));

        mockMvc.perform(get("/stores"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.size()").value(1))
               .andExpect(jsonPath("$[0].storId").value("7066"));
    }

    @Test
    public void testGetStoreById() throws Exception 
    {
        when(storeService.getStoreById("7066")).thenReturn(validStore);

        mockMvc.perform(get("/stores/{id}", "7066"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.storId").value("7066"))
               .andExpect(jsonPath("$.storName").value("Barnum's"));
    }

    @Test
    public void testCreateStore() throws Exception 
    {
        when(storeService.createStore(any(Store.class))).thenReturn(validStore);

        mockMvc.perform(post("/stores")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(validStore)))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.storId").value("7066"));
    }

    @Test
    public void testUpdateStore() throws Exception 
    {
        when(storeService.updateStore(eq("7066"), any(Store.class))).thenReturn(validStore);

        mockMvc.perform(put("/stores/{id}", "7066")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(validStore)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.storName").value("Barnum's"));
    }

    @Test
    public void testPatchStore() throws Exception 
    {
        when(storeService.patchStore(eq("7066"), any(Store.class))).thenReturn(validStore);

        mockMvc.perform(patch("/stores/{id}", "7066")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(validStore)))
               .andExpect(status().isOk());
    }

    @Test
    public void testFilterStoresByCity() throws Exception 
    {
        when(storeService.getStoresByCity("Tustin")).thenReturn(Arrays.asList(validStore));

        mockMvc.perform(get("/stores/filter/city").param("city", "Tustin"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    public void testFilterStoresByState() throws Exception 
    {
        when(storeService.getStoresByState("CA")).thenReturn(Arrays.asList(validStore));

        mockMvc.perform(get("/stores/filter/state").param("state", "CA"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    public void testGetSalesByStore() throws Exception 
    {
        when(storeService.getSalesByStoreId("7066")).thenReturn(Arrays.asList(new Sales()));

        mockMvc.perform(get("/stores/{id}/sales", "7066"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    public void testGetTitlesByStore() throws Exception 
    {
        Title validTitle = new Title();
        validTitle.setTitleId("TL100");
        when(storeService.getTitlesByStoreId("7066")).thenReturn(Arrays.asList(validTitle));

        mockMvc.perform(get("/stores/{id}/titles", "7066"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.size()").value(1))
               .andExpect(jsonPath("$[0].titleId").value("TL100"));
    }

    @Test
    public void testGetPublishersByStore() throws Exception 
    {
        Publisher validPublisher = new Publisher();
        validPublisher.setPubId("9999");
        when(storeService.getPublishersByStoreId("7066")).thenReturn(Arrays.asList(validPublisher));

        mockMvc.perform(get("/stores/{id}/publishers", "7066"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.size()").value(1))
               .andExpect(jsonPath("$[0].pubId").value("9999"));
    }

    @Test
    public void testGetAuthorsByStore() throws Exception 
    {
        Author validAuthor = new Author();
        validAuthor.setAuId("111-22-3333");
        when(storeService.getAuthorsByStoreId("7066")).thenReturn(Arrays.asList(validAuthor));

        mockMvc.perform(get("/stores/{id}/authors", "7066"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.size()").value(1))
               .andExpect(jsonPath("$[0].auId").value("111-22-3333"));
    }

    @Test
    public void testGetDiscountsByStore() throws Exception 
    {
        when(storeService.getDiscountsByStoreId("7066")).thenReturn(Arrays.asList(new Discount()));

        mockMvc.perform(get("/stores/{id}/discounts", "7066"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.size()").value(1));
    }
}