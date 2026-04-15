package com.capg.portal.finance.controller;

import com.capg.portal.finance.entity.Discount;
import com.capg.portal.finance.service.DiscountService;
import com.capg.portal.retail.entity.Store;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(
        controllers = DiscountController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                SecurityFilterAutoConfiguration.class
        }
)
class DiscountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DiscountService service;

    @Autowired
    private ObjectMapper objectMapper;

    private Discount getSample() {
        Store store = new Store();
        store.setStorId("S1");

        return new Discount(
                "Seasonal",
                store,
                10,
                100,
                new BigDecimal("15.00")
        );
    }

  
    @Test
    void testGetAll() throws Exception {

        when(service.getAllDiscounts()).thenReturn(List.of(getSample()));

        mockMvc.perform(get("/discounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].discountAmount").value(15.00));
    }


    @Test
    void testGetByType() throws Exception {

        when(service.getDiscountByType("Seasonal")).thenReturn(getSample());

        mockMvc.perform(get("/discounts/Seasonal"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.discountAmount").value(15.00));
    }


    @Test
    void testCreate() throws Exception {

        Discount d = getSample();

        when(service.createDiscount(any())).thenReturn(d);

        mockMvc.perform(post("/discounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(d)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.discountAmount").value(15.00));
    }

  
    @Test
    void testUpdate() throws Exception {

        Discount d = getSample();
        d.setDiscountAmount(new BigDecimal("20.00"));

        when(service.updateDiscount(any(), any())).thenReturn(d);

        mockMvc.perform(put("/discounts/Seasonal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(d)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.discountAmount").value(20.00));
    }

    
    @Test
    void testPatch() throws Exception {

        Discount d = getSample();
        d.setDiscountAmount(new BigDecimal("25.00"));

        when(service.patchDiscount(any(), any())).thenReturn(d);

        mockMvc.perform(patch("/discounts/Seasonal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(d)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.discountAmount").value(25.00));
    }

 
    @Test
    void testFilterByQty() throws Exception {

        when(service.getDiscountsByQuantityRange(10, 100))
                .thenReturn(List.of(getSample()));

        mockMvc.perform(get("/discounts/filter/qty")
                        .param("minQty", "10")
                        .param("maxQty", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].discountAmount").value(15.00));
    }


    @Test
    void testFilterByAmount() throws Exception {

        when(service.getDiscountsLessThanAmount(new BigDecimal("20.00")))
                .thenReturn(List.of(getSample()));

        mockMvc.perform(get("/discounts/filter/amount")
                        .param("maxAmount", "20.00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].discountAmount").value(15.00));
    }


    @Test
    void testFilterByStore() throws Exception {

        when(service.getDiscountsByStoreId("S1"))
                .thenReturn(List.of(getSample()));

        mockMvc.perform(get("/discounts/filter/store")
                        .param("storId", "S1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].discountAmount").value(15.00));
    }


    @Test
    void testGetStoreByDiscountType() throws Exception {

        when(service.getDiscountByType("Seasonal")).thenReturn(getSample());

        mockMvc.perform(get("/discounts/Seasonal/store"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.storId").value("S1"));
    }


    @Test
    void testGetStoreByDiscountType_NoContent() throws Exception {

        Discount d = getSample();
        d.setStore(null);

        when(service.getDiscountByType("Seasonal")).thenReturn(d);

        mockMvc.perform(get("/discounts/Seasonal/store"))
                .andExpect(status().isNoContent());
    }
}