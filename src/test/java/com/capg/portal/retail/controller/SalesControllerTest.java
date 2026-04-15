package com.capg.portal.retail.controller;

import com.capg.portal.retail.entity.Sales;
import com.capg.portal.retail.service.SalesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SalesController.class)
@AutoConfigureMockMvc(addFilters = false)
public class SalesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SalesService salesService;

    @Autowired
    private ObjectMapper objectMapper;

    private final String VALID_JSON = """
        {
          "store": { "storId": "S1" },
          "ordNum": "O1",
          "title": { "titleId": "T1" },
          "qty": 10,
          "payterms": "Net 30"
        }
        """;

    private Sales getMockSale() {
        Sales sale = new Sales();
        sale.setOrdNum("O1");
        sale.setQty((short) 10); 
        sale.setPayterms("Net 30");
        return sale;
    }

    @Test
    void getAllSales_success() throws Exception {
        when(salesService.getAllSales()).thenReturn(List.of(getMockSale()));

        mockMvc.perform(get("/sales"))
                .andExpect(status().isOk());
    }

    @Test
    void getSaleById_success() throws Exception {
        when(salesService.getSaleById(any())).thenReturn(getMockSale());

        mockMvc.perform(get("/sales/S1/O1/T1"))
                .andExpect(status().isOk());
    }

    @Test
    void createSale_success() throws Exception {
        when(salesService.createSale(any())).thenReturn(getMockSale());

        mockMvc.perform(post("/sales")
                .contentType(MediaType.APPLICATION_JSON)
                .content(VALID_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void createSale_validationFail() throws Exception {
        String invalidJson = "{}";

        mockMvc.perform(post("/sales")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateSale_success() throws Exception {
        when(salesService.updateSale(any(), any())).thenReturn(getMockSale());

        mockMvc.perform(put("/sales/S1/O1/T1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(VALID_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void patchSale_success() throws Exception {
        when(salesService.patchSale(any(), any())).thenReturn(getMockSale());

        mockMvc.perform(patch("/sales/S1/O1/T1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(VALID_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void filterByStore_success() throws Exception {
        when(salesService.getSalesByStoreId("S1"))
                .thenReturn(List.of(getMockSale()));

        mockMvc.perform(get("/sales/filter/store")
                .param("storId", "S1"))
                .andExpect(status().isOk());
    }

    @Test
    void filterByTitle_success() throws Exception {
        when(salesService.getSalesByTitleId("T1"))
                .thenReturn(List.of(getMockSale()));

        mockMvc.perform(get("/sales/filter/title")
                .param("titleId", "T1"))
                .andExpect(status().isOk());
    }

    @Test
    void filterByPayterms_success() throws Exception {
        when(salesService.getSalesByPayterms("Net 30"))
                .thenReturn(List.of(getMockSale()));

        mockMvc.perform(get("/sales/filter/payterms")
                .param("terms", "Net 30"))
                .andExpect(status().isOk());
    }

    @Test
    void getTotalQtyByStore_success() throws Exception {
        when(salesService.getTotalQtyByStore("S1")).thenReturn(100);

        mockMvc.perform(get("/sales/store/S1/total-qty"))
                .andExpect(status().isOk())
                .andExpect(content().string("100"));
    }

    @Test
    void getTransactionCountByStore_success() throws Exception {
        when(salesService.getTransactionCountByStore("S1")).thenReturn(5L);

        mockMvc.perform(get("/sales/store/S1/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }

    @Test
    void getTotalQtyByTitle_success() throws Exception {
        when(salesService.getTotalQtyByTitle("T1")).thenReturn(200);

        mockMvc.perform(get("/sales/title/T1/total-qty"))
                .andExpect(status().isOk())
                .andExpect(content().string("200"));
    }
}