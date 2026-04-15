package com.capg.portal.finance.controller;

import com.capg.portal.finance.entity.RoyaltySchedule;
import com.capg.portal.catalog.entity.Title;
import com.capg.portal.finance.service.RoyaltyScheduleService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RoyaltyScheduleController.class)
@AutoConfigureMockMvc(addFilters = false)
public class RoyaltyScheduleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoyaltyScheduleService service;

    @Autowired
    private ObjectMapper objectMapper;

    private RoyaltySchedule schedule;
    private Title title;

    @BeforeEach
    void setup() {
        title = new Title();
        title.setTitleId("T1");

        schedule = new RoyaltySchedule();
        schedule.setRoyschedId(1);
        schedule.setTitle(title);
        schedule.setLorange(100);
        schedule.setHirange(500);
        schedule.setRoyalty(10);
    }


    @Test
    void getAll_success() throws Exception {
        Mockito.when(service.getAllRoyaltySchedules())
                .thenReturn(List.of(schedule));

        mockMvc.perform(get("/roysched"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].royschedId").value(1));
    }

    @Test
    void getAll_empty() throws Exception {
        Mockito.when(service.getAllRoyaltySchedules())
                .thenReturn(List.of());

        mockMvc.perform(get("/roysched"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    void getById_success() throws Exception {
        Mockito.when(service.getRoyaltyScheduleById(1))
                .thenReturn(schedule);

        mockMvc.perform(get("/roysched/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.royschedId").value(1));
    }

    @Test
    void getById_notFound() throws Exception {
        Mockito.when(service.getRoyaltyScheduleById(99))
                .thenThrow(new RuntimeException("Not found"));

        mockMvc.perform(get("/roysched/99"))
                .andExpect(status().isInternalServerError()); 
    }


    @Test
    void create_success() throws Exception {
        Mockito.when(service.createRoyaltySchedule(Mockito.any()))
                .thenReturn(schedule);

        mockMvc.perform(post("/roysched")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(schedule)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.royschedId").value(1));
    }

    @Test
    void create_validationFail() throws Exception {
        RoyaltySchedule invalid = new RoyaltySchedule();

        mockMvc.perform(post("/roysched")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_serviceException() throws Exception {
        Mockito.when(service.createRoyaltySchedule(Mockito.any()))
                .thenThrow(new RuntimeException("DB error"));

        mockMvc.perform(post("/roysched")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(schedule)))
                .andExpect(status().isInternalServerError());
    }


    @Test
    void update_success() throws Exception {
        Mockito.when(service.updateRoyaltySchedule(Mockito.eq(1), Mockito.any()))
                .thenReturn(schedule);

        mockMvc.perform(put("/roysched/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(schedule)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.royschedId").value(1));
    }

    @Test
    void update_notFound() throws Exception {
        Mockito.when(service.updateRoyaltySchedule(Mockito.eq(99), Mockito.any()))
                .thenThrow(new RuntimeException("Not found"));

        mockMvc.perform(put("/roysched/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(schedule)))
                .andExpect(status().isInternalServerError());
    }


    @Test
    void patch_success() throws Exception {
        Mockito.when(service.patchRoyaltySchedule(Mockito.eq(1), Mockito.any()))
                .thenReturn(schedule);

        mockMvc.perform(patch("/roysched/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(schedule)))
                .andExpect(status().isOk());
    }

    @Test
    void patch_notFound() throws Exception {
        Mockito.when(service.patchRoyaltySchedule(Mockito.eq(99), Mockito.any()))
                .thenThrow(new RuntimeException("Not found"));

        mockMvc.perform(patch("/roysched/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(schedule)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void patch_emptyBody() throws Exception {
        mockMvc.perform(patch("/roysched/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk());
    }


    @Test
    void filterByRange_success() throws Exception {
        Mockito.when(service.getRoyaltySchedulesByRange(100, 500))
                .thenReturn(List.of(schedule));

        mockMvc.perform(get("/roysched/filter/range")
                        .param("minLorange", "100")
                        .param("maxHirange", "500"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].royalty").value(10));
    }

    @Test
    void filterByRange_missingParams() throws Exception {
        mockMvc.perform(get("/roysched/filter/range"))
        .andExpect(status().isInternalServerError());    }


    @Test
    void filterByTitle_success() throws Exception {
        Mockito.when(service.getRoyaltySchedulesByTitleId("T1"))
                .thenReturn(List.of(schedule));

        mockMvc.perform(get("/roysched/filter/title")
                        .param("titleId", "T1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].royschedId").value(1));
    }

    @Test
    void filterByTitle_empty() throws Exception {
        Mockito.when(service.getRoyaltySchedulesByTitleId("T1"))
                .thenReturn(List.of());

        mockMvc.perform(get("/roysched/filter/title")
                        .param("titleId", "T1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    void getTitle_success() throws Exception {
        Mockito.when(service.getRoyaltyScheduleById(1))
                .thenReturn(schedule);

        mockMvc.perform(get("/roysched/1/title"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titleId").value("T1"));
    }

    @Test
    void getTitle_notFound() throws Exception {
        Mockito.when(service.getRoyaltyScheduleById(99))
                .thenThrow(new RuntimeException("Not found"));

        mockMvc.perform(get("/roysched/99/title"))
                .andExpect(status().isInternalServerError());
    }
}