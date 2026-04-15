package com.capg.portal.retail.service;

import com.capg.portal.retail.entity.*;
import com.capg.portal.retail.repository.SalesRepository;
import com.capg.portal.catalog.entity.Title;
import com.capg.portal.exception.ResourceAlreadyExistsException;
import com.capg.portal.exception.ResourceNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SalesServiceTest {
	
	@Mock
    private SalesRepository salesRepository;

    @InjectMocks
    private SalesService salesService;

    private Sales createSale(String storId, String ordNum, String titleId, short qty) {
        Store store = new Store();
        store.setStorId(storId);

        Title title = new Title();
        title.setTitleId(titleId);

        Sales sale = new Sales();
        sale.setStore(store);
        sale.setTitle(title);
        sale.setOrdNum(ordNum);
        sale.setQty(qty);
        sale.setPayterms("Net 30");

        return sale;
    }
    
    @Test
    void createSale_success() {
    	Sales sale = createSale("S1", "01", "T1",(short) 5);
    	SalesId id = new SalesId("S1", "01", "T1");
    	
    	when(salesRepository.existsById(id)).thenReturn(false);
    	when(salesRepository.save(any())).thenReturn(sale);
    	
    	Sales result = salesService.createSale(sale);
    	
    	assertNotNull(result);
    	assertNotNull(result.getOrdDate());
    	verify(salesRepository).save(sale);    	
    }
    
    @Test
    void createSale_alreadyExists() {
    	Sales sale = createSale("S1","01","T1", (short) 5);
    	SalesId id = new SalesId("S1", "01","T1");
    	
    	when(salesRepository.existsById(id)).thenReturn(true);
    	
    	assertThrows(ResourceAlreadyExistsException.class,
    			() -> salesService.createSale(sale));
    	
    	verify(salesRepository, never()).save(any());
    }
    
    @Test
    void getAllSales_nonEmpty() {
    	when(salesRepository.findAll()).thenReturn(List.of(createSale("S1","O1","T1",(short)5)));
    	
    	assertEquals(1,salesService.getAllSales().size());
    }
    
    @Test
    void getAllSales_empty() {
    	when(salesRepository.findAll()).thenReturn(Collections.emptyList());
    	
    	assertTrue(salesService.getAllSales().isEmpty());
    }
    
    @Test
    void getSaleById_found() {
    	Sales sale = createSale("S1","O1","T1",(short)5);
        SalesId id = new SalesId("S1","O1","T1");
        
        when(salesRepository.findById(id)).thenReturn(Optional.of(sale));
        
        assertNotNull(salesService.getSaleById(id));
    }

    @Test
    void getSaleById_notFound() {
        SalesId id = new SalesId("S1","O1","T1");

        when(salesRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> salesService.getSaleById(id));
    }

    @Test
    void updateSale_success() {
        Sales existing = createSale("S1","O1","T1",(short)5);
        Sales updated = createSale("S1","O1","T1",(short)10);
        updated.setPayterms("Net 60");

        SalesId id = new SalesId("S1","O1","T1");

        when(salesRepository.findById(id)).thenReturn(Optional.of(existing));
        when(salesRepository.save(any())).thenReturn(existing);

        Sales result = salesService.updateSale(id, updated);

        assertEquals((short) 10, result.getQty());
        assertEquals("Net 60", result.getPayterms());
    }
    
    @Test
    void updateSale_notFound() {
        SalesId id = new SalesId("S1","O1","T1");

        when(salesRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> salesService.updateSale(id, new Sales()));
    }
    

    @Test
    void patchSale_partialUpdate() {
        Sales existing = createSale("S1","O1","T1",(short)10);

        Sales updates = new Sales();
        updates.setQty((short)20);

        SalesId id = new SalesId("S1","O1","T1");

        when(salesRepository.findById(id)).thenReturn(Optional.of(existing));
        when(salesRepository.save(any())).thenReturn(existing);

        Sales result = salesService.patchSale(id, updates);

        assertEquals((short)20, result.getQty());
    }

    @Test
    void patchSale_notFound() {
        SalesId id = new SalesId("S1","O1","T1");

        when(salesRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> salesService.patchSale(id, new Sales()));
    }
    
    @Test
    void getSalesByStoreId_found() {
        when(salesRepository.findByStoreStorId("S1"))
                .thenReturn(List.of(createSale("S1","O1","T1",(short)5)));

        assertEquals(1, salesService.getSalesByStoreId("S1").size());
    }

    @Test
    void getSalesByStoreId_empty() {
        when(salesRepository.findByStoreStorId("S1"))
                .thenReturn(Collections.emptyList());

        assertTrue(salesService.getSalesByStoreId("S1").isEmpty());
    }

    @Test
    void getSalesByTitleId_found() {
        when(salesRepository.findByTitleTitleId("T1"))
                .thenReturn(List.of(createSale("S1","O1","T1",(short)5)));

        assertEquals(1, salesService.getSalesByTitleId("T1").size());
    }

    @Test
    void getSalesByPayterms_found() {
        when(salesRepository.findByPaytermsIgnoreCase("net 30"))
                .thenReturn(List.of(createSale("S1","O1","T1",(short)5)));

        assertEquals(1, salesService.getSalesByPayterms("net 30").size());
    }
    
    @Test
    void getTotalQtyByStore() {
        Sales s1 = createSale("S1","O1","T1",(short)5);
        Sales s2 = createSale("S1","O2","T1",(short)10);

        when(salesRepository.findByStoreStorId("S1"))
                .thenReturn(List.of(s1, s2));

        int total = salesService.getTotalQtyByStore("S1");

        assertEquals(15, total);
    }
    
    @Test
    void getTransactionCountByStore() {
        when(salesRepository.findByStoreStorId("S1"))
                .thenReturn(List.of(
                        createSale("S1","O1","T1",(short)5),
                        createSale("S1","O2","T1",(short)10)
                ));

        assertEquals(2, salesService.getTransactionCountByStore("S1"));
    }
    
    @Test
    void getTotalQtyByTitle() {
        Sales s1 = createSale("S1","O1","T1",(short)3);
        Sales s2 = createSale("S1","O2","T1",(short)7);

        when(salesRepository.findByTitleTitleId("T1"))
                .thenReturn(List.of(s1, s2));

        assertEquals(10, salesService.getTotalQtyByTitle("T1"));
    }
}
