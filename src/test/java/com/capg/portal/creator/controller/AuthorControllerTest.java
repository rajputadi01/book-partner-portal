package com.capg.portal.creator.controller;

import com.capg.portal.catalog.entity.Title;
import com.capg.portal.catalog.entity.TitleAuthor;
import com.capg.portal.creator.entity.Author;
import com.capg.portal.creator.entity.Publisher;
import com.capg.portal.creator.service.AuthorService;
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

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AuthorController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class AuthorControllerTest 
{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthorService authorService;

    private Author validAuthor;

    @BeforeEach
    public void setup() 
    {
        validAuthor = new Author("111-22-3333", "Smith", "John", "123 456-7890", "123 Main St", "NYC", "NY", "10001", 1);
    }

    @Test
    public void testGetAllAuthors() throws Exception 
    {
        when(authorService.getAllAuthors()).thenReturn(Arrays.asList(validAuthor));

        mockMvc.perform(get("/authors"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.size()").value(1))
               .andExpect(jsonPath("$[0].auId").value("111-22-3333"));
    }

    @Test
    public void testGetAuthorById() throws Exception 
    {
        when(authorService.getAuthorById("111-22-3333")).thenReturn(validAuthor);

        mockMvc.perform(get("/authors/{id}", "111-22-3333"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.auId").value("111-22-3333"))
               .andExpect(jsonPath("$.auLname").value("Smith"));
    }

    @Test
    public void testCreateAuthor() throws Exception 
    {
        when(authorService.createAuthor(any(Author.class))).thenReturn(validAuthor);

        mockMvc.perform(post("/authors")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(validAuthor)))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.auId").value("111-22-3333"));
    }

    @Test
    public void testUpdateAuthor() throws Exception 
    {
        when(authorService.updateAuthor(eq("111-22-3333"), any(Author.class))).thenReturn(validAuthor);

        mockMvc.perform(put("/authors/{id}", "111-22-3333")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(validAuthor)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.auFname").value("John"));
    }

    @Test
    public void testPatchAuthor() throws Exception 
    {
        when(authorService.patchAuthor(eq("111-22-3333"), any(Author.class))).thenReturn(validAuthor);

        mockMvc.perform(patch("/authors/{id}", "111-22-3333")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(validAuthor)))
               .andExpect(status().isOk());
    }

    @Test
    public void testFilterAuthorsByContract() throws Exception 
    {
        when(authorService.getAuthorsByContractStatus(1)).thenReturn(Arrays.asList(validAuthor));

        mockMvc.perform(get("/authors/filter/contract").param("status", "1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    public void testFilterAuthorsByCity() throws Exception 
    {
        when(authorService.getAuthorsByCity("NYC")).thenReturn(Arrays.asList(validAuthor));

        mockMvc.perform(get("/authors/filter/city").param("city", "NYC"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    public void testFilterAuthorsByState() throws Exception 
    {
        when(authorService.getAuthorsByState("NY")).thenReturn(Arrays.asList(validAuthor));

        mockMvc.perform(get("/authors/filter/state").param("state", "NY"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    public void testGetTitleAuthorsByAuthor() throws Exception 
    {
        when(authorService.getTitleAuthorsByAuthorId("111-22-3333")).thenReturn(Arrays.asList(new TitleAuthor()));

        mockMvc.perform(get("/authors/{id}/title-authors", "111-22-3333"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    public void testGetTitlesByAuthor() throws Exception 
    {
        Title validTitle = new Title();
        validTitle.setTitleId("TL100");
        when(authorService.getTitlesByAuthorId("111-22-3333")).thenReturn(Arrays.asList(validTitle));

        mockMvc.perform(get("/authors/{id}/titles", "111-22-3333"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.size()").value(1))
               .andExpect(jsonPath("$[0].titleId").value("TL100"));
    }

    @Test
    public void testGetPublishersByAuthor() throws Exception 
    {
        Publisher validPublisher = new Publisher();
        validPublisher.setPubId("9999");
        when(authorService.getPublishersByAuthorId("111-22-3333")).thenReturn(Arrays.asList(validPublisher));

        mockMvc.perform(get("/authors/{id}/publishers", "111-22-3333"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.size()").value(1))
               .andExpect(jsonPath("$[0].pubId").value("9999"));
    }

    @Test
    public void testGetStoresByAuthor() throws Exception 
    {
        Store validStore = new Store();
        validStore.setStorId("ST01");
        when(authorService.getStoresByAuthorId("111-22-3333")).thenReturn(Arrays.asList(validStore));

        mockMvc.perform(get("/authors/{id}/stores", "111-22-3333"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.size()").value(1))
               .andExpect(jsonPath("$[0].storId").value("ST01"));
    }
}