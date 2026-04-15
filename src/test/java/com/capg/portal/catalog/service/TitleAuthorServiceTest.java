package com.capg.portal.catalog.service;

import com.capg.portal.catalog.entity.*;
import com.capg.portal.catalog.repository.TitleAuthorRepository;
import com.capg.portal.creator.entity.Author;
import com.capg.portal.exception.ResourceAlreadyExistsException;
import com.capg.portal.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit Test Class for TitleAuthorService
 * 
 * This class tests all business logic methods using Mockito
 * without connecting to the actual database.
 */
@ExtendWith(MockitoExtension.class)
class TitleAuthorServiceTest {

    @Mock
    private TitleAuthorRepository repository;

    @InjectMocks
    private TitleAuthorService service;

    private TitleAuthor titleAuthor;
    private TitleAuthorId id;

    /**
     * Setup method runs before each test
     * Creates dummy data for testing
     */
    @BeforeEach
    void setup() {
        Author author = new Author();
        author.setAuId("A1");

        Title title = new Title();
        title.setTitleId("T1");

        titleAuthor = new TitleAuthor(author, title, 1, 30);
        id = new TitleAuthorId("A1", "T1");
    }

    // ================= CREATE =================

    /**
     * Happy Flow:
     * Should create TitleAuthor successfully
     */
    @Test
    void testCreateTitleAuthor_Success() {
        when(repository.existsById(id)).thenReturn(false);
        when(repository.findByTitleTitleId("T1")).thenReturn(new ArrayList<>());
        when(repository.save(titleAuthor)).thenReturn(titleAuthor);

        TitleAuthor result = service.createTitleAuthor(titleAuthor);

        assertNotNull(result);
        verify(repository).save(titleAuthor);
    }

    /**
     * Sad Flow:
     * Should throw exception if mapping already exists
     */
    @Test
    void testCreateTitleAuthor_AlreadyExists() {
        when(repository.existsById(id)).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class,
                () -> service.createTitleAuthor(titleAuthor));
    }

    /**
     * Sad Flow:
     * Should throw exception if royalty exceeds 100%
     */
    @Test
    void testCreateTitleAuthor_RoyaltyExceedsLimit() {

        Author anotherAuthor = new Author();
        anotherAuthor.setAuId("A2"); // DIFFERENT AUTHOR

        TitleAuthor existing = new TitleAuthor(anotherAuthor, titleAuthor.getTitle(), 1, 80);

        when(repository.existsById(id)).thenReturn(false);
        when(repository.findByTitleTitleId("T1")).thenReturn(List.of(existing));

        titleAuthor.setRoyaltyPer(30); // 80 + 30 = 110

        assertThrows(IllegalArgumentException.class,
                () -> service.createTitleAuthor(titleAuthor));
    }

    // ================= GET BY ID =================

    /**
     * Happy Flow:
     * Should return TitleAuthor when ID exists
     */
    @Test
    void testGetById_Success() {
        when(repository.findById(id)).thenReturn(Optional.of(titleAuthor));

        TitleAuthor result = service.getTitleAuthorById(id);

        assertEquals("A1", result.getAuthor().getAuId());
    }

    /**
     * Sad Flow:
     * Should throw exception when ID not found
     */
    @Test
    void testGetById_NotFound() {
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.getTitleAuthorById(id));
    }

    // ================= UPDATE =================

    /**
     * Happy Flow:
     * Should update existing record successfully
     */
    @Test
    void testUpdateTitleAuthor_Success() {
        when(repository.findById(id)).thenReturn(Optional.of(titleAuthor));
        when(repository.findByTitleTitleId("T1")).thenReturn(List.of(titleAuthor));
        when(repository.save(any())).thenReturn(titleAuthor);

        TitleAuthor updated = new TitleAuthor(titleAuthor.getAuthor(), titleAuthor.getTitle(), 2, 40);

        TitleAuthor result = service.updateTitleAuthor(id, updated);

        assertEquals(40, result.getRoyaltyPer());
    }

    /**
     * Sad Flow:
     * Should throw exception if record not found during update
     */
    @Test
    void testUpdateTitleAuthor_NotFound() {
        when(repository.findById(id)).thenReturn(Optional.empty());

        TitleAuthor updated = new TitleAuthor();

        assertThrows(ResourceNotFoundException.class,
                () -> service.updateTitleAuthor(id, updated));
    }

    /**
     * Sad Flow:
     * Should throw exception if royalty exceeds limit during update
     */
    @Test
    void testUpdateTitleAuthor_RoyaltyExceeds() {

        Author anotherAuthor = new Author();
        anotherAuthor.setAuId("A2"); // DIFFERENT

        TitleAuthor existing = new TitleAuthor(anotherAuthor, titleAuthor.getTitle(), 1, 80);

        when(repository.findById(id)).thenReturn(Optional.of(titleAuthor));
        when(repository.findByTitleTitleId("T1")).thenReturn(List.of(existing));

        TitleAuthor updated = new TitleAuthor(titleAuthor.getAuthor(), titleAuthor.getTitle(), 1, 30);

        assertThrows(IllegalArgumentException.class,
                () -> service.updateTitleAuthor(id, updated));
    }

    // ================= PATCH =================

    /**
     * Happy Flow:
     * Should update only provided fields
     */
    @Test
    void testPatchTitleAuthor_Success() {
        when(repository.findById(id)).thenReturn(Optional.of(titleAuthor));
        when(repository.findByTitleTitleId("T1")).thenReturn(new ArrayList<>());
        when(repository.save(any())).thenReturn(titleAuthor);

        TitleAuthor updates = new TitleAuthor();
        updates.setRoyaltyPer(50);

        TitleAuthor result = service.patchTitleAuthor(id, updates);

        assertEquals(50, result.getRoyaltyPer());
    }

    /**
     * Sad Flow:
     * Should throw exception if record not found
     */
    @Test
    void testPatchTitleAuthor_NotFound() {
        when(repository.findById(id)).thenReturn(Optional.empty());

        TitleAuthor updates = new TitleAuthor();

        assertThrows(ResourceNotFoundException.class,
                () -> service.patchTitleAuthor(id, updates));
    }

    /**
     * Sad Flow:
     * Should throw exception if royalty exceeds limit
     */
    @Test
    void testPatchTitleAuthor_RoyaltyExceeds() {

        Author anotherAuthor = new Author();
        anotherAuthor.setAuId("A2"); // DIFFERENT

        TitleAuthor existing = new TitleAuthor(anotherAuthor, titleAuthor.getTitle(), 1, 90);

        when(repository.findById(id)).thenReturn(Optional.of(titleAuthor));
        when(repository.findByTitleTitleId("T1")).thenReturn(List.of(existing));

        TitleAuthor updates = new TitleAuthor();
        updates.setRoyaltyPer(20); // 90 + 20 = 110

        assertThrows(IllegalArgumentException.class,
                () -> service.patchTitleAuthor(id, updates));
    }

    // ================= FILTER =================

    /**
     * Happy Flow:
     * Should return filtered results based on royalty
     */
    @Test
    void testGetByRoyalty() {
        when(repository.findByRoyaltyPerLessThan(50)).thenReturn(List.of(titleAuthor));

        List<TitleAuthor> result = service.getTitleAuthorsByRoyaltyLessThan(50);

        assertEquals(1, result.size());
    }

    /**
     * Happy Flow:
     * Should return authors by Author ID
     */
    @Test
    void testGetByAuthorId() {
        when(repository.findByAuthorAuId("A1")).thenReturn(List.of(titleAuthor));

        List<TitleAuthor> result = service.getTitleAuthorsByAuthorId("A1");

        assertEquals(1, result.size());
    }

    /**
     * Happy Flow:
     * Should return authors by Title ID
     */
    @Test
    void testGetByTitleId() {
        when(repository.findByTitleTitleId("T1")).thenReturn(List.of(titleAuthor));

        List<TitleAuthor> result = service.getTitleAuthorsByTitleId("T1");

        assertEquals(1, result.size());
    }

    /**
     * Happy Flow:
     * Should return lead authors (auOrd = 1)
     */
    @Test
    void testGetLeadAuthors() {
        when(repository.findByAuOrd(1)).thenReturn(List.of(titleAuthor));

        List<TitleAuthor> result = service.getLeadAuthors();

        assertEquals(1, result.size());
    }

    // ================= SEARCH =================

    /**
     * Happy Flow:
     * Should filter based on given parameters
     */
    @Test
    void testSearch_ByAuthor() {
        when(repository.findAll()).thenReturn(List.of(titleAuthor));

        List<TitleAuthor> result = service.search("A1", null, null, null);

        assertEquals(1, result.size());
    }

    /**
     * Edge Case:
     * Should return empty list if no match
     */
    @Test
    void testSearch_NoMatch() {
        when(repository.findAll()).thenReturn(List.of(titleAuthor));

        List<TitleAuthor> result = service.search("A2", null, null, null);

        assertEquals(0, result.size());
    }
}