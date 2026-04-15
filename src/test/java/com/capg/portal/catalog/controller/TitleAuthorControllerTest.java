package com.capg.portal.catalog.controller;

import com.capg.portal.catalog.entity.TitleAuthor;
import com.capg.portal.catalog.service.TitleAuthorService;
import com.capg.portal.creator.entity.Author;
import com.capg.portal.catalog.entity.Title;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(
        controllers = TitleAuthorController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                SecurityFilterAutoConfiguration.class
        }
)
class TitleAuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TitleAuthorService service;

    @Autowired
    private ObjectMapper objectMapper;

    private TitleAuthor getSample() {
        Author author = new Author();
        author.setAuId("A1");

        Title title = new Title();
        title.setTitleId("T1");

        return new TitleAuthor(author, title, 1, 30);
    }


    @Test
    void testGetAll() throws Exception {

        when(service.getAllTitleAuthors()).thenReturn(List.of(getSample()));

        mockMvc.perform(get("/titleauthors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].royaltyPer").value(30));
    }


    @Test
    void testGetById() throws Exception {

        when(service.getTitleAuthorById(any())).thenReturn(getSample());

        mockMvc.perform(get("/titleauthors/A1/T1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.royaltyPer").value(30));
    }


    @Test
    void testCreate() throws Exception {

        TitleAuthor ta = getSample();

        when(service.createTitleAuthor(any())).thenReturn(ta);

        mockMvc.perform(post("/titleauthors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ta)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.royaltyPer").value(30));
    }


    @Test
    void testUpdate() throws Exception {

        TitleAuthor ta = getSample();
        ta.setRoyaltyPer(50);

        when(service.updateTitleAuthor(any(), any())).thenReturn(ta);

        mockMvc.perform(put("/titleauthors/A1/T1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ta)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.royaltyPer").value(50));
    }


    @Test
    void testPatch() throws Exception {

        TitleAuthor ta = getSample();
        ta.setRoyaltyPer(60);

        when(service.patchTitleAuthor(any(), any())).thenReturn(ta);

        mockMvc.perform(patch("/titleauthors/A1/T1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ta)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.royaltyPer").value(60));
    }
    
    
    @Test
    void testFilterByRoyalty() throws Exception {

        when(service.getTitleAuthorsByRoyaltyLessThan(50))
                .thenReturn(List.of(getSample()));

        mockMvc.perform(get("/titleauthors/filter/royalty")
                        .param("maxRoyalty", "50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].royaltyPer").value(30));
    }
    
    
    @Test
    void testFilterByAuthor() throws Exception {

        when(service.getTitleAuthorsByAuthorId("A1"))
                .thenReturn(List.of(getSample()));

        mockMvc.perform(get("/titleauthors/filter/author")
                        .param("auId", "A1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].royaltyPer").value(30));
    }
    
    
    @Test
    void testFilterByTitle() throws Exception {

        when(service.getTitleAuthorsByTitleId("T1"))
                .thenReturn(List.of(getSample()));

        mockMvc.perform(get("/titleauthors/filter/title")
                        .param("titleId", "T1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].royaltyPer").value(30));
    }
    
    
    @Test
    void testGetLeadAuthors() throws Exception {

        when(service.getLeadAuthors())
                .thenReturn(List.of(getSample()));

        mockMvc.perform(get("/titleauthors/filter/lead"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].royaltyPer").value(30));
    }
    
    
    @Test
    void testSearch() throws Exception {

        when(service.search(any(), any(), any(), any()))
                .thenReturn(List.of(getSample()));

        mockMvc.perform(get("/titleauthors/search")
                        .param("auId", "A1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].royaltyPer").value(30));
    }
}