package com.capg.portal.creator.service;

import com.capg.portal.catalog.entity.Title;
import com.capg.portal.catalog.entity.TitleAuthor;
import com.capg.portal.catalog.repository.TitleAuthorRepository;
import com.capg.portal.creator.entity.Author;
import com.capg.portal.creator.entity.Publisher;
import com.capg.portal.creator.repository.AuthorRepository;
import com.capg.portal.exception.ResourceAlreadyExistsException;
import com.capg.portal.exception.ResourceNotFoundException;
import com.capg.portal.retail.entity.Sales;
import com.capg.portal.retail.entity.Store;
import com.capg.portal.retail.repository.SalesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceTest 
{
    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private TitleAuthorRepository titleAuthorRepository;

    @Mock
    private SalesRepository salesRepository;

    @InjectMocks
    private AuthorService authorService;

    private Author validAuthor;
    private TitleAuthor validTitleAuthor;
    private Title validTitle;
    private Publisher validPublisher;
    private Store validStore;
    private Sales validSale;

    @BeforeEach
    public void setup() 
    {
        validAuthor = new Author("111-22-3333", "Smith", "John", "123 456-7890", "123 Main St", "NYC", "NY", "10001", 1);
        
        validPublisher = new Publisher("9999", "Cosmic Books", "Mars City", "MC", "Mars");
        
        validTitle = new Title();
        validTitle.setTitleId("TL1001");
        validTitle.setPublisher(validPublisher);
        
        validTitleAuthor = new TitleAuthor();
        validTitleAuthor.setAuthor(validAuthor);
        validTitleAuthor.setTitle(validTitle);
        
        validStore = new Store();
        validStore.setStorId("ST100");
        
        validSale = new Sales();
        validSale.setStore(validStore);
        validSale.setTitle(validTitle);
    }

    // --- CREATE AUTHOR TESTS ---

    @Test
    public void testCreateAuthor_Success() 
    {
        when(authorRepository.existsById("111-22-3333")).thenReturn(false);
        when(authorRepository.save(any(Author.class))).thenReturn(validAuthor);

        Author saved = authorService.createAuthor(validAuthor);

        assertNotNull(saved);
        assertEquals("111-22-3333", saved.getAuId());
        verify(authorRepository, times(1)).save(validAuthor);
    }

    @Test
    public void testCreateAuthor_Defaults() 
    {
        validAuthor.setPhone("   ");
        validAuthor.setZip("");
        validAuthor.setState(" ");
        
        when(authorRepository.existsById("111-22-3333")).thenReturn(false);
        when(authorRepository.save(any(Author.class))).thenReturn(validAuthor);

        Author saved = authorService.createAuthor(validAuthor);

        assertEquals("UNKNOWN", saved.getPhone());
        assertNull(saved.getZip());
        assertNull(saved.getState());
    }

    @Test
    public void testCreateAuthor_IdAlreadyExists_ThrowsException() 
    {
        when(authorRepository.existsById("111-22-3333")).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> {
            authorService.createAuthor(validAuthor);
        });

        verify(authorRepository, never()).save(any(Author.class));
    }

    // --- READ AUTHOR TESTS ---

    @Test
    public void testGetAllAuthors_Success() 
    {
        when(authorRepository.findAll()).thenReturn(Arrays.asList(validAuthor));

        List<Author> list = authorService.getAllAuthors();

        assertEquals(1, list.size());
    }

    @Test
    public void testGetAuthorById_Success() 
    {
        when(authorRepository.findById("111-22-3333")).thenReturn(Optional.of(validAuthor));

        Author found = authorService.getAuthorById("111-22-3333");

        assertNotNull(found);
        assertEquals("111-22-3333", found.getAuId());
    }

    @Test
    public void testGetAuthorById_NotFound_ThrowsException() 
    {
        when(authorRepository.findById("INVALID")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            authorService.getAuthorById("INVALID");
        });
    }

    // --- UPDATE AUTHOR TESTS ---

    @Test
    public void testUpdateAuthor_Success() 
    {
        Author newDetails = new Author("111-22-3333", "Doe", "Jane", "999 888-7777", "456 Oak St", "LA", "CA", "90210", 0);
        
        when(authorRepository.findById("111-22-3333")).thenReturn(Optional.of(validAuthor));
        when(authorRepository.save(any(Author.class))).thenReturn(newDetails);

        Author updated = authorService.updateAuthor("111-22-3333", newDetails);

        assertEquals("Doe", updated.getAuLname());
        assertEquals("CA", updated.getState());
        assertEquals(0, updated.getContract());
    }

    @Test
    public void testUpdateAuthor_NotFound_ThrowsException() 
    {
        when(authorRepository.findById("INVALID")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            authorService.updateAuthor("INVALID", validAuthor);
        });
    }

    // --- PATCH AUTHOR TESTS ---

    @Test
    public void testPatchAuthor_Success() 
    {
        Author updates = new Author();
        updates.setCity("Chicago");
        updates.setContract(0);
        
        when(authorRepository.findById("111-22-3333")).thenReturn(Optional.of(validAuthor));
        when(authorRepository.save(any(Author.class))).thenReturn(validAuthor);

        Author patched = authorService.patchAuthor("111-22-3333", updates);

        assertEquals("Chicago", patched.getCity());
        assertEquals(0, patched.getContract());
        assertEquals("Smith", patched.getAuLname()); // Unchanged
    }

    @Test
    public void testPatchAuthor_NotFound_ThrowsException() 
    {
        when(authorRepository.findById("INVALID")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            authorService.patchAuthor("INVALID", new Author());
        });
    }

    // --- FILTER QUERIES TESTS ---

    @Test
    public void testGetAuthorsByContractStatus_Success() 
    {
        when(authorRepository.findByContract(1)).thenReturn(Arrays.asList(validAuthor));
        
        List<Author> list = authorService.getAuthorsByContractStatus(1);
        
        assertEquals(1, list.size());
    }

    @Test
    public void testGetAuthorsByCity_Success() 
    {
        when(authorRepository.findByCityIgnoreCase("NYC")).thenReturn(Arrays.asList(validAuthor));
        
        List<Author> list = authorService.getAuthorsByCity("NYC");
        
        assertEquals(1, list.size());
    }

    @Test
    public void testGetAuthorsByState_Success() 
    {
        when(authorRepository.findByStateIgnoreCase("NY")).thenReturn(Arrays.asList(validAuthor));
        
        List<Author> list = authorService.getAuthorsByState("NY");
        
        assertEquals(1, list.size());
    }

    // --- RELATIONAL SPIDERWEB TESTS (1 & 2 HOP) ---

    @Test
    public void testGetTitleAuthorsByAuthorId_Success() 
    {
        when(authorRepository.findById("111-22-3333")).thenReturn(Optional.of(validAuthor));
        when(titleAuthorRepository.findByAuthorAuId("111-22-3333")).thenReturn(Arrays.asList(validTitleAuthor));
        
        List<TitleAuthor> mappings = authorService.getTitleAuthorsByAuthorId("111-22-3333");
        
        assertEquals(1, mappings.size());
    }

    @Test
    public void testGetTitleAuthorsByAuthorId_NotFound_ThrowsException() 
    {
        when(authorRepository.findById("INVALID")).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> {
            authorService.getTitleAuthorsByAuthorId("INVALID");
        });
    }

    @Test
    public void testGetTitlesByAuthorId_Success() 
    {
        when(authorRepository.findById("111-22-3333")).thenReturn(Optional.of(validAuthor));
        when(titleAuthorRepository.findByAuthorAuId("111-22-3333")).thenReturn(Arrays.asList(validTitleAuthor));
        
        List<Title> titles = authorService.getTitlesByAuthorId("111-22-3333");
        
        assertEquals(1, titles.size());
        assertEquals("TL1001", titles.get(0).getTitleId());
    }

    @Test
    public void testGetTitlesByAuthorId_NotFound_ThrowsException() 
    {
        when(authorRepository.findById("INVALID")).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> {
            authorService.getTitlesByAuthorId("INVALID");
        });
    }

    // --- COMPLEX DEEP TRAVERSAL TESTS (3-HOP) ---

    @Test
    public void testGetPublishersByAuthorId_Success() 
    {
        when(authorRepository.findById("111-22-3333")).thenReturn(Optional.of(validAuthor));
        when(titleAuthorRepository.findByAuthorAuId("111-22-3333")).thenReturn(Arrays.asList(validTitleAuthor));
        
        List<Publisher> publishers = authorService.getPublishersByAuthorId("111-22-3333");
        
        assertEquals(1, publishers.size());
        assertEquals("9999", publishers.get(0).getPubId());
    }

    @Test
    public void testGetPublishersByAuthorId_NotFound_ThrowsException() 
    {
        when(authorRepository.findById("INVALID")).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> {
            authorService.getPublishersByAuthorId("INVALID");
        });
    }

    @Test
    public void testGetStoresByAuthorId_Success() 
    {
        when(authorRepository.findById("111-22-3333")).thenReturn(Optional.of(validAuthor));
        when(titleAuthorRepository.findByAuthorAuId("111-22-3333")).thenReturn(Arrays.asList(validTitleAuthor));
        when(salesRepository.findByTitleTitleId("TL1001")).thenReturn(Arrays.asList(validSale));
        
        List<Store> stores = authorService.getStoresByAuthorId("111-22-3333");
        
        assertEquals(1, stores.size());
        assertEquals("ST100", stores.get(0).getStorId());
    }

    @Test
    public void testGetStoresByAuthorId_NotFound_ThrowsException() 
    {
        when(authorRepository.findById("INVALID")).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> {
            authorService.getStoresByAuthorId("INVALID");
        });
    }
}