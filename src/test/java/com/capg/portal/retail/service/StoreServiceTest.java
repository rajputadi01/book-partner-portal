package com.capg.portal.retail.service;

import com.capg.portal.catalog.entity.Title;
import com.capg.portal.catalog.entity.TitleAuthor;
import com.capg.portal.catalog.repository.TitleAuthorRepository;
import com.capg.portal.creator.entity.Author;
import com.capg.portal.creator.entity.Publisher;
import com.capg.portal.exception.ResourceAlreadyExistsException;
import com.capg.portal.exception.ResourceNotFoundException;
import com.capg.portal.finance.entity.Discount;
import com.capg.portal.finance.repository.DiscountRepository;
import com.capg.portal.retail.entity.Sales;
import com.capg.portal.retail.entity.Store;
import com.capg.portal.retail.repository.SalesRepository;
import com.capg.portal.retail.repository.StoreRepository;
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
public class StoreServiceTest 
{
    @Mock
    private StoreRepository storeRepository;

    @Mock
    private SalesRepository salesRepository;

    @Mock
    private TitleAuthorRepository titleAuthorRepository;

    @Mock
    private DiscountRepository discountRepository;

    @InjectMocks
    private StoreService storeService;

    private Store validStore;
    private Sales validSale;
    private Title validTitle;
    private Publisher validPublisher;
    private Author validAuthor;
    private TitleAuthor validTitleAuthor;
    private Discount validDiscount;

    @BeforeEach
    public void setup() 
    {
        validStore = new Store("7066", "Barnum's", "567 Pasadena Ave", "Tustin", "CA", "92789");
        
        validPublisher = new Publisher("9999", "Cosmic Books", "Mars City", "MC", "Mars");
        
        validTitle = new Title();
        validTitle.setTitleId("TL1001");
        validTitle.setPublisher(validPublisher);
        
        validSale = new Sales();
        validSale.setStore(validStore);
        validSale.setTitle(validTitle);
        
        validAuthor = new Author("111-22-3333", "Smith", "John", "123 456-7890", "123 Main St", "NYC", "NY", "10001", 1);
        
        validTitleAuthor = new TitleAuthor();
        validTitleAuthor.setTitle(validTitle);
        validTitleAuthor.setAuthor(validAuthor);
        
        validDiscount = new Discount();
        validDiscount.setStore(validStore);
    }

    // --- CREATE STORE TESTS ---

    @Test
    public void testCreateStore_Success() 
    {
        when(storeRepository.existsById("7066")).thenReturn(false);
        when(storeRepository.save(any(Store.class))).thenReturn(validStore);

        Store saved = storeService.createStore(validStore);

        assertNotNull(saved);
        assertEquals("7066", saved.getStorId());
        verify(storeRepository, times(1)).save(validStore);
    }

    @Test
    public void testCreateStore_Defaults() 
    {
        validStore.setState("  ");
        validStore.setZip("");
        
        when(storeRepository.existsById("7066")).thenReturn(false);
        when(storeRepository.save(any(Store.class))).thenReturn(validStore);

        Store saved = storeService.createStore(validStore);

        assertNull(saved.getState());
        assertNull(saved.getZip());
    }

    @Test
    public void testCreateStore_IdAlreadyExists_ThrowsException() 
    {
        when(storeRepository.existsById("7066")).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> {
            storeService.createStore(validStore);
        });

        verify(storeRepository, never()).save(any(Store.class));
    }

    // --- READ STORE TESTS ---

    @Test
    public void testGetAllStores_Success() 
    {
        when(storeRepository.findAll()).thenReturn(Arrays.asList(validStore));

        List<Store> list = storeService.getAllStores();

        assertEquals(1, list.size());
    }

    @Test
    public void testGetStoreById_Success() 
    {
        when(storeRepository.findById("7066")).thenReturn(Optional.of(validStore));

        Store found = storeService.getStoreById("7066");

        assertNotNull(found);
        assertEquals("7066", found.getStorId());
    }

    @Test
    public void testGetStoreById_NotFound_ThrowsException() 
    {
        when(storeRepository.findById("INVALID")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            storeService.getStoreById("INVALID");
        });
    }

    // --- UPDATE STORE TESTS ---

    @Test
    public void testUpdateStore_Success() 
    {
        Store newDetails = new Store("7066", "New Name", "New Address", "New City", "NY", "10001");
        
        when(storeRepository.findById("7066")).thenReturn(Optional.of(validStore));
        when(storeRepository.save(any(Store.class))).thenReturn(newDetails);

        Store updated = storeService.updateStore("7066", newDetails);

        assertEquals("New Name", updated.getStorName());
        assertEquals("NY", updated.getState());
    }

    @Test
    public void testUpdateStore_NotFound_ThrowsException() 
    {
        when(storeRepository.findById("INVALID")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            storeService.updateStore("INVALID", validStore);
        });
    }

    // --- PATCH STORE TESTS ---

    @Test
    public void testPatchStore_Success() 
    {
        Store updates = new Store();
        updates.setCity("Seattle");
        updates.setState("WA");
        
        when(storeRepository.findById("7066")).thenReturn(Optional.of(validStore));
        when(storeRepository.save(any(Store.class))).thenReturn(validStore);

        Store patched = storeService.patchStore("7066", updates);

        assertEquals("Seattle", patched.getCity());
        assertEquals("WA", patched.getState());
        assertEquals("Barnum's", patched.getStorName()); 
    }

    @Test
    public void testPatchStore_NotFound_ThrowsException() 
    {
        when(storeRepository.findById("INVALID")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            storeService.patchStore("INVALID", new Store());
        });
    }

    // --- FILTER QUERIES TESTS ---

    @Test
    public void testGetStoresByCity_Success() 
    {
        when(storeRepository.findByCityIgnoreCase("Tustin")).thenReturn(Arrays.asList(validStore));
        
        List<Store> list = storeService.getStoresByCity("Tustin");
        
        assertEquals(1, list.size());
    }

    @Test
    public void testGetStoresByState_Success() 
    {
        when(storeRepository.findByStateIgnoreCase("CA")).thenReturn(Arrays.asList(validStore));
        
        List<Store> list = storeService.getStoresByState("CA");
        
        assertEquals(1, list.size());
    }

    // --- 1-HOP RELATIONAL TESTS ---

    @Test
    public void testGetSalesByStoreId_Success() 
    {
        when(storeRepository.findById("7066")).thenReturn(Optional.of(validStore));
        when(salesRepository.findByStoreStorId("7066")).thenReturn(Arrays.asList(validSale));
        
        List<Sales> sales = storeService.getSalesByStoreId("7066");
        
        assertEquals(1, sales.size());
    }

    @Test
    public void testGetSalesByStoreId_NotFound_ThrowsException() 
    {
        when(storeRepository.findById("INVALID")).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> {
            storeService.getSalesByStoreId("INVALID");
        });
    }

    @Test
    public void testGetDiscountsByStoreId_Success() 
    {
        when(storeRepository.findById("7066")).thenReturn(Optional.of(validStore));
        when(discountRepository.findByStoreStorId("7066")).thenReturn(Arrays.asList(validDiscount));
        
        List<Discount> discounts = storeService.getDiscountsByStoreId("7066");
        
        assertEquals(1, discounts.size());
    }

    @Test
    public void testGetDiscountsByStoreId_NotFound_ThrowsException() 
    {
        when(storeRepository.findById("INVALID")).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> {
            storeService.getDiscountsByStoreId("INVALID");
        });
    }

    // --- DEEP TRAVERSAL TESTS (2-HOP & 3-HOP) ---

    @Test
    public void testGetTitlesByStoreId_Success() 
    {
        when(storeRepository.findById("7066")).thenReturn(Optional.of(validStore));
        when(salesRepository.findByStoreStorId("7066")).thenReturn(Arrays.asList(validSale));
        
        List<Title> titles = storeService.getTitlesByStoreId("7066");
        
        assertEquals(1, titles.size());
        assertEquals("TL1001", titles.get(0).getTitleId());
    }

    @Test
    public void testGetTitlesByStoreId_NotFound_ThrowsException() 
    {
        when(storeRepository.findById("INVALID")).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> {
            storeService.getTitlesByStoreId("INVALID");
        });
    }

    @Test
    public void testGetPublishersByStoreId_Success() 
    {
        when(storeRepository.findById("7066")).thenReturn(Optional.of(validStore));
        when(salesRepository.findByStoreStorId("7066")).thenReturn(Arrays.asList(validSale));
        
        List<Publisher> publishers = storeService.getPublishersByStoreId("7066");
        
        assertEquals(1, publishers.size());
        assertEquals("9999", publishers.get(0).getPubId());
    }

    @Test
    public void testGetPublishersByStoreId_NotFound_ThrowsException() 
    {
        when(storeRepository.findById("INVALID")).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> {
            storeService.getPublishersByStoreId("INVALID");
        });
    }

    @Test
    public void testGetAuthorsByStoreId_Success() 
    {
        when(storeRepository.findById("7066")).thenReturn(Optional.of(validStore));
        when(salesRepository.findByStoreStorId("7066")).thenReturn(Arrays.asList(validSale));
        when(titleAuthorRepository.findByTitleTitleId("TL1001")).thenReturn(Arrays.asList(validTitleAuthor));
        
        List<Author> authors = storeService.getAuthorsByStoreId("7066");
        
        assertEquals(1, authors.size());
        assertEquals("111-22-3333", authors.get(0).getAuId());
    }

    @Test
    public void testGetAuthorsByStoreId_NotFound_ThrowsException() 
    {
        when(storeRepository.findById("INVALID")).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> {
            storeService.getAuthorsByStoreId("INVALID");
        });
    }
}