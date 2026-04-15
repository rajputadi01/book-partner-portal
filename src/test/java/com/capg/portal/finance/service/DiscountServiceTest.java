package com.capg.portal.finance.service;

import com.capg.portal.finance.entity.Discount;
import com.capg.portal.finance.repository.DiscountRepository;
import com.capg.portal.retail.entity.Store;
import com.capg.portal.exception.ResourceAlreadyExistsException;
import com.capg.portal.exception.ResourceNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit Test Class for DiscountService
 * Covers all methods with happy & sad flows
 */
@ExtendWith(MockitoExtension.class)
class DiscountServiceTest {

    @Mock
    private DiscountRepository repository;

    @InjectMocks
    private DiscountService service;

    private Discount discount;

    @BeforeEach
    void setup() {
        Store store = new Store();
        store.setStorId("S1");

        discount = new Discount(
                "Seasonal",
                store,
                10,
                100,
                new BigDecimal("15.00")
        );
    }

    // ================= CREATE =================

    @Test
    void testCreateDiscount_Success() {
        when(repository.existsById("Seasonal")).thenReturn(false);
        when(repository.save(discount)).thenReturn(discount);

        Discount result = service.createDiscount(discount);

        assertNotNull(result);
        verify(repository).save(discount);
    }

    @Test
    void testCreateDiscount_AlreadyExists() {
        when(repository.existsById("Seasonal")).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class,
                () -> service.createDiscount(discount));
    }

    // ================= GET =================

    @Test
    void testGetAllDiscounts() {
        when(repository.findAll()).thenReturn(List.of(discount));

        List<Discount> result = service.getAllDiscounts();

        assertEquals(1, result.size());
    }

    @Test
    void testGetDiscountByType_Success() {
        when(repository.findById("Seasonal")).thenReturn(Optional.of(discount));

        Discount result = service.getDiscountByType("Seasonal");

        assertEquals("Seasonal", result.getDiscountType());
    }

    @Test
    void testGetDiscountByType_NotFound() {
        when(repository.findById("Seasonal")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.getDiscountByType("Seasonal"));
    }

    // ================= UPDATE =================

    @Test
    void testUpdateDiscount_Success() {
        when(repository.findById("Seasonal")).thenReturn(Optional.of(discount));
        when(repository.save(any())).thenReturn(discount);

        Discount updated = new Discount(
                "Seasonal",
                null,
                20,
                200,
                new BigDecimal("20.00")
        );

        Discount result = service.updateDiscount("Seasonal", updated);

        assertEquals(20, result.getLowQty());
        assertEquals(new BigDecimal("20.00"), result.getDiscountAmount());
    }

    @Test
    void testUpdateDiscount_NotFound() {
        when(repository.findById("Seasonal")).thenReturn(Optional.empty());

        Discount updated = new Discount();

        assertThrows(ResourceNotFoundException.class,
                () -> service.updateDiscount("Seasonal", updated));
    }

    // ================= PATCH =================

    @Test
    void testPatchDiscount_Success() {
        when(repository.findById("Seasonal")).thenReturn(Optional.of(discount));
        when(repository.save(any())).thenReturn(discount);

        Discount updates = new Discount();
        updates.setDiscountAmount(new BigDecimal("25.00"));

        Discount result = service.patchDiscount("Seasonal", updates);

        assertEquals(new BigDecimal("25.00"), result.getDiscountAmount());
    }

    @Test
    void testPatchDiscount_NotFound() {
        when(repository.findById("Seasonal")).thenReturn(Optional.empty());

        Discount updates = new Discount();

        assertThrows(ResourceNotFoundException.class,
                () -> service.patchDiscount("Seasonal", updates));
    }

    // ================= FILTER =================

    @Test
    void testGetDiscountsByQuantityRange() {
        when(repository.findByLowQtyGreaterThanEqualAndHighQtyLessThanEqual(10, 100))
                .thenReturn(List.of(discount));

        List<Discount> result = service.getDiscountsByQuantityRange(10, 100);

        assertEquals(1, result.size());
    }

    @Test
    void testGetDiscountsLessThanAmount() {
        when(repository.findByDiscountAmountLessThan(new BigDecimal("20.00")))
                .thenReturn(List.of(discount));

        List<Discount> result = service.getDiscountsLessThanAmount(new BigDecimal("20.00"));

        assertEquals(1, result.size());
    }

    @Test
    void testGetDiscountsByStoreId() {
        when(repository.findByStoreStorId("S1"))
                .thenReturn(List.of(discount));

        List<Discount> result = service.getDiscountsByStoreId("S1");

        assertEquals(1, result.size());
    }
}