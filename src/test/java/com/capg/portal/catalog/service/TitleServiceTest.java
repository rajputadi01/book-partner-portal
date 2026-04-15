package com.capg.portal.catalog.service;

import com.capg.portal.catalog.entity.Title;
import com.capg.portal.catalog.entity.TitleAuthor;
import com.capg.portal.catalog.repository.TitleAuthorRepository;
import com.capg.portal.catalog.repository.TitleRepository;
import com.capg.portal.creator.entity.Author;
import com.capg.portal.creator.entity.Publisher;
import com.capg.portal.exception.ResourceAlreadyExistsException;
import com.capg.portal.exception.ResourceNotFoundException;
import com.capg.portal.finance.entity.RoyaltySchedule;
import com.capg.portal.finance.repository.RoyaltyScheduleRepository;
import com.capg.portal.retail.entity.Sales;
import com.capg.portal.retail.entity.Store;
import com.capg.portal.retail.repository.SalesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TitleServiceTest 
{
    @Mock
    private TitleRepository titleRepository;

    @Mock
    private SalesRepository salesRepository;

    @Mock
    private RoyaltyScheduleRepository royaltyScheduleRepository;

    @Mock
    private TitleAuthorRepository titleAuthorRepository;

    @InjectMocks
    private TitleService titleService;

    private Publisher validPublisher;
    private Title validTitle;

    @BeforeEach
    public void setup() 
    {
        validPublisher = new Publisher("9999", "Cosmic Books", "Mars City", "MC", "Mars");
        validTitle = new Title("TL1001", "The Hitchhiker's Guide", "SCI-FI", validPublisher, 42.0, 1000.0, 10, 5000, "Don't Panic", LocalDateTime.now());
    }

    // --- CREATE TITLE TESTS ---

    @Test
    public void testCreateTitle_Success() 
    {
        when(titleRepository.existsById("TL1001")).thenReturn(false);
        when(titleRepository.save(any(Title.class))).thenReturn(validTitle);

        Title saved = titleService.createTitle(validTitle);

        assertNotNull(saved);
        assertEquals("TL1001", saved.getTitleId());
        verify(titleRepository, times(1)).save(validTitle);
    }

    @Test
    public void testCreateTitle_IdAlreadyExists_ThrowsException() 
    {
        when(titleRepository.existsById("TL1001")).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> {
            titleService.createTitle(validTitle);
        });

        verify(titleRepository, never()).save(any(Title.class));
    }

    // --- READ TITLE TESTS ---

    @Test
    public void testGetAllTitles_Success() 
    {
        when(titleRepository.findAll()).thenReturn(Arrays.asList(validTitle));

        List<Title> list = titleService.getAllTitles();

        assertEquals(1, list.size());
    }

    @Test
    public void testGetTitleById_Success() 
    {
        when(titleRepository.findById("TL1001")).thenReturn(Optional.of(validTitle));

        Title found = titleService.getTitleById("TL1001");

        assertNotNull(found);
        assertEquals("TL1001", found.getTitleId());
    }

    @Test
    public void testGetTitleById_NotFound_ThrowsException() 
    {
        when(titleRepository.findById("INVALID")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            titleService.getTitleById("INVALID");
        });
    }

    // --- UPDATE TITLE TESTS ---

    @Test
    public void testUpdateTitle_Success() 
    {
        Title newDetails = new Title("TL1001", "Updated Name", "FANTASY", validPublisher, 50.0, 2000.0, 15, 6000, "Updated Notes", LocalDateTime.now());
        
        when(titleRepository.findById("TL1001")).thenReturn(Optional.of(validTitle));
        when(titleRepository.save(any(Title.class))).thenReturn(newDetails);

        Title updated = titleService.updateTitle("TL1001", newDetails);

        assertEquals("Updated Name", updated.getTitleName());
        assertEquals("FANTASY", updated.getType());
        assertEquals(50.0, updated.getPrice());
    }

    @Test
    public void testUpdateTitle_NotFound_ThrowsException() 
    {
        when(titleRepository.findById("INVALID")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            titleService.updateTitle("INVALID", validTitle);
        });
    }

    // --- PATCH TITLE TESTS ---

    @Test
    public void testPatchTitle_Success() 
    {
        Title updates = new Title();
        updates.setPrice(99.99);
        updates.setType("HISTORY");
        
        when(titleRepository.findById("TL1001")).thenReturn(Optional.of(validTitle));
        when(titleRepository.save(any(Title.class))).thenReturn(validTitle);

        Title patched = titleService.patchTitle("TL1001", updates);

        assertEquals(99.99, patched.getPrice());
        assertEquals("HISTORY", patched.getType());
        assertEquals("The Hitchhiker's Guide", patched.getTitleName()); // Unchanged
    }

    @Test
    public void testPatchTitle_NotFound_ThrowsException() 
    {
        when(titleRepository.findById("INVALID")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            titleService.patchTitle("INVALID", new Title());
        });
    }

    // --- FILTER QUERIES TESTS ---

    @Test
    public void testGetTitlesByPriceLessThan_Success() 
    {
        when(titleRepository.findByPriceLessThan(50.0)).thenReturn(Arrays.asList(validTitle));
        
        List<Title> list = titleService.getTitlesByPriceLessThan(50.0);
        
        assertEquals(1, list.size());
    }

    @Test
    public void testGetTitlesByType_Success() 
    {
        when(titleRepository.findByTypeIgnoreCase("SCI-FI")).thenReturn(Arrays.asList(validTitle));
        
        List<Title> list = titleService.getTitlesByType("SCI-FI");
        
        assertEquals(1, list.size());
    }

    @Test
    public void testGetTitlesByPublisher_Success() 
    {
        when(titleRepository.findByPublisherPubId("9999")).thenReturn(Arrays.asList(validTitle));
        
        List<Title> list = titleService.getTitlesByPublisher("9999");
        
        assertEquals(1, list.size());
    }

    @Test
    public void testGetTitlesPublishedBefore_Success() 
    {
        LocalDateTime date = LocalDateTime.now();
        when(titleRepository.findByPubdateBefore(date)).thenReturn(Arrays.asList(validTitle));
        
        List<Title> list = titleService.getTitlesPublishedBefore(date);
        
        assertEquals(1, list.size());
    }

    // --- RELATIONAL HOP TESTS ---

    @Test
    public void testGetPublisherByTitleId_Success() 
    {
        when(titleRepository.findById("TL1001")).thenReturn(Optional.of(validTitle));
        
        Publisher pub = titleService.getPublisherByTitleId("TL1001");
        
        assertNotNull(pub);
        assertEquals("9999", pub.getPubId());
    }

    @Test
    public void testGetPublisherByTitleId_NotFound_ThrowsException() 
    {
        when(titleRepository.findById("INVALID")).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> {
            titleService.getPublisherByTitleId("INVALID");
        });
    }

    @Test
    public void testGetPublisherByTitleId_NoPublisher_ThrowsException() 
    {
        validTitle.setPublisher(null);
        when(titleRepository.findById("TL1001")).thenReturn(Optional.of(validTitle));
        
        assertThrows(ResourceNotFoundException.class, () -> {
            titleService.getPublisherByTitleId("TL1001");
        });
    }

    @Test
    public void testGetSalesByTitleId_Success() 
    {
        Sales dummySale = new Sales();
        dummySale.setOrdNum("ORD1");
        
        when(titleRepository.findById("TL1001")).thenReturn(Optional.of(validTitle));
        when(salesRepository.findByTitleTitleId("TL1001")).thenReturn(Arrays.asList(dummySale));
        
        List<Sales> sales = titleService.getSalesByTitleId("TL1001");
        
        assertEquals(1, sales.size());
    }

    @Test
    public void testGetSalesByTitleId_NotFound_ThrowsException() 
    {
        when(titleRepository.findById("INVALID")).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> {
            titleService.getSalesByTitleId("INVALID");
        });
    }

    @Test
    public void testGetRoyaltiesByTitleId_Success() 
    {
        RoyaltySchedule dummyRoyalty = new RoyaltySchedule();
        
        when(titleRepository.findById("TL1001")).thenReturn(Optional.of(validTitle));
        when(royaltyScheduleRepository.findByTitleTitleId("TL1001")).thenReturn(Arrays.asList(dummyRoyalty));
        
        List<RoyaltySchedule> royalties = titleService.getRoyaltiesByTitleId("TL1001");
        
        assertEquals(1, royalties.size());
    }

    @Test
    public void testGetRoyaltiesByTitleId_NotFound_ThrowsException() 
    {
        when(titleRepository.findById("INVALID")).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> {
            titleService.getRoyaltiesByTitleId("INVALID");
        });
    }

    @Test
    public void testGetTitleAuthorsByTitleId_Success() 
    {
        TitleAuthor dummyTA = new TitleAuthor();
        
        when(titleRepository.findById("TL1001")).thenReturn(Optional.of(validTitle));
        when(titleAuthorRepository.findByTitleTitleId("TL1001")).thenReturn(Arrays.asList(dummyTA));
        
        List<TitleAuthor> titleAuthors = titleService.getTitleAuthorsByTitleId("TL1001");
        
        assertEquals(1, titleAuthors.size());
    }

    @Test
    public void testGetTitleAuthorsByTitleId_NotFound_ThrowsException() 
    {
        when(titleRepository.findById("INVALID")).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> {
            titleService.getTitleAuthorsByTitleId("INVALID");
        });
    }

    // --- COMPLEX 2-HOP TESTS ---

    @Test
    public void testGetAuthorsByTitleId_Success() 
    {
        Author dummyAuthor = new Author();
        dummyAuthor.setAuId("AU100");
        
        TitleAuthor dummyTA = new TitleAuthor();
        dummyTA.setAuthor(dummyAuthor);
        
        when(titleRepository.findById("TL1001")).thenReturn(Optional.of(validTitle));
        when(titleAuthorRepository.findByTitleTitleId("TL1001")).thenReturn(Arrays.asList(dummyTA));
        
        List<Author> authors = titleService.getAuthorsByTitleId("TL1001");
        
        assertEquals(1, authors.size());
        assertEquals("AU100", authors.get(0).getAuId());
    }

    @Test
    public void testGetAuthorsByTitleId_NotFound_ThrowsException() 
    {
        when(titleRepository.findById("INVALID")).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> {
            titleService.getAuthorsByTitleId("INVALID");
        });
    }

    @Test
    public void testGetStoresByTitleId_Success() 
    {
        Store dummyStore = new Store();
        dummyStore.setStorId("ST100");
        
        Sales dummySale1 = new Sales();
        dummySale1.setStore(dummyStore);
        
        Sales dummySale2 = new Sales();
        dummySale2.setStore(dummyStore); // Duplicate store to test distinct()
        
        when(titleRepository.findById("TL1001")).thenReturn(Optional.of(validTitle));
        when(salesRepository.findByTitleTitleId("TL1001")).thenReturn(Arrays.asList(dummySale1, dummySale2));
        
        List<Store> stores = titleService.getStoresByTitleId("TL1001");
        
        // Distinct should reduce it to 1
        assertEquals(1, stores.size());
        assertEquals("ST100", stores.get(0).getStorId());
    }

    @Test
    public void testGetStoresByTitleId_NotFound_ThrowsException() 
    {
        when(titleRepository.findById("INVALID")).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> {
            titleService.getStoresByTitleId("INVALID");
        });
    }
}