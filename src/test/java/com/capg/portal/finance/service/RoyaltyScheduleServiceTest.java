package com.capg.portal.finance.service;

import com.capg.portal.catalog.entity.Title;
import com.capg.portal.exception.ResourceNotFoundException;
import com.capg.portal.finance.entity.RoyaltySchedule;
import com.capg.portal.finance.repository.RoyaltyScheduleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class RoyaltyScheduleServiceTest {
	
	@Mock
    private RoyaltyScheduleRepository repository;

    @InjectMocks
    private RoyaltyScheduleService service;

    private Title title;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        title = new Title();
        title.setTitleId("T1");
    }
    
    private RoyaltySchedule createSchedule(int id, int lo, int hi, int royalty) {
        RoyaltySchedule rs = new RoyaltySchedule();
        rs.setRoyschedId(id);
        rs.setTitle(title);
        rs.setLorange(lo);
        rs.setHirange(hi);
        rs.setRoyalty(royalty);
        return rs;
    }
    
    @Test
    void createRoyaltySchedule_success() {
        RoyaltySchedule rs = createSchedule(1, 0, 100, 10);

        when(repository.save(rs)).thenReturn(rs);

        RoyaltySchedule result = service.createRoyaltySchedule(rs);

        assertEquals(100, result.getHirange());
        verify(repository).save(rs);
    }
    
    @Test
    void createRoyaltySchedule_invalidRange_shouldThrow() {
        RoyaltySchedule rs = createSchedule(1, 100, 50, 10);

        assertThrows(IllegalArgumentException.class,
                () -> service.createRoyaltySchedule(rs));

        verify(repository, never()).save(any());
    }
    
    @Test
    void getAllRoyaltySchedules() {
        List<RoyaltySchedule> list = List.of(
                createSchedule(1, 0, 100, 10),
                createSchedule(2, 100, 200, 15)
        );

        when(repository.findAll()).thenReturn(list);

        List<RoyaltySchedule> result = service.getAllRoyaltySchedules();

        assertEquals(2, result.size());
    }
    
    @Test
    void getAllRoyaltySchedules_empty() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        List<RoyaltySchedule> result = service.getAllRoyaltySchedules();

        assertTrue(result.isEmpty());
    }
    
    @Test
    void getRoyaltyScheduleById_success() {
        RoyaltySchedule rs = createSchedule(1, 0, 100, 10);

        when(repository.findById(1)).thenReturn(Optional.of(rs));

        RoyaltySchedule result = service.getRoyaltyScheduleById(1);

        assertEquals(10, result.getRoyalty());
    }
    
    @Test
    void getRoyaltyScheduleById_notFound() {
        when(repository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.getRoyaltyScheduleById(1));
    }

    @Test
    void updateRoyaltySchedule_success() {
        RoyaltySchedule existing = createSchedule(1, 0, 100, 10);
        RoyaltySchedule updated = createSchedule(1, 50, 200, 20);

        when(repository.findById(1)).thenReturn(Optional.of(existing));
        when(repository.save(any())).thenReturn(existing);

        RoyaltySchedule result = service.updateRoyaltySchedule(1, updated);

        assertEquals(50, result.getLorange());
        assertEquals(200, result.getHirange());
        assertEquals(20, result.getRoyalty());
    }
    
    @Test
    void updateRoyaltySchedule_notFound() {
        RoyaltySchedule updated = createSchedule(1, 50, 200, 20);

        when(repository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.updateRoyaltySchedule(1, updated));
    }
    
    @Test
    void updateRoyaltySchedule_invalidRange() {
        RoyaltySchedule existing = createSchedule(1, 0, 100, 10);
        RoyaltySchedule updated = createSchedule(1, 200, 100, 20);

        when(repository.findById(1)).thenReturn(Optional.of(existing));

        assertThrows(IllegalArgumentException.class,
                () -> service.updateRoyaltySchedule(1, updated));
    }
    
    @Test
    void patchRoyaltySchedule_partialUpdate() {
        RoyaltySchedule existing = createSchedule(1, 0, 100, 10);

        RoyaltySchedule updates = new RoyaltySchedule();
        updates.setRoyalty(25);

        when(repository.findById(1)).thenReturn(Optional.of(existing));
        when(repository.save(any())).thenReturn(existing);

        RoyaltySchedule result = service.patchRoyaltySchedule(1, updates);

        assertEquals(25, result.getRoyalty());
        assertEquals(0, result.getLorange()); // unchanged
    }
    
    @Test
    void patchRoyaltySchedule_updateRange() {
        RoyaltySchedule existing = createSchedule(1, 0, 100, 10);

        RoyaltySchedule updates = new RoyaltySchedule();
        updates.setLorange(10);
        updates.setHirange(200);

        when(repository.findById(1)).thenReturn(Optional.of(existing));
        when(repository.save(any())).thenReturn(existing);

        RoyaltySchedule result = service.patchRoyaltySchedule(1, updates);

        assertEquals(10, result.getLorange());
        assertEquals(200, result.getHirange());
    }
    
    @Test
    void patchRoyaltySchedule_invalidRange() {
        RoyaltySchedule existing = createSchedule(1, 0, 100, 10);

        RoyaltySchedule updates = new RoyaltySchedule();
        updates.setLorange(200);
        updates.setHirange(100);

        when(repository.findById(1)).thenReturn(Optional.of(existing));

        assertThrows(IllegalArgumentException.class,
                () -> service.patchRoyaltySchedule(1, updates));
    }
    
    @Test
    void getRoyaltySchedulesByRange() {
        List<RoyaltySchedule> list = List.of(
                createSchedule(1, 0, 100, 10)
        );

        when(repository.findByLorangeGreaterThanEqualAndHirangeLessThanEqual(0, 100))
                .thenReturn(list);

        List<RoyaltySchedule> result =
                service.getRoyaltySchedulesByRange(0, 100);

        assertEquals(1, result.size());
    }
    
    @Test
    void getRoyaltySchedulesByTitleId() {
        List<RoyaltySchedule> list = List.of(
                createSchedule(1, 0, 100, 10)
        );

        when(repository.findByTitleTitleId("T1")).thenReturn(list);

        List<RoyaltySchedule> result =
                service.getRoyaltySchedulesByTitleId("T1");

        assertEquals(1, result.size());
    }
}
