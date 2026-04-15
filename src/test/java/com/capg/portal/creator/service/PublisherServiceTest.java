package com.capg.portal.creator.service;

import com.capg.portal.creator.entity.*;
import com.capg.portal.catalog.entity.*;
import com.capg.portal.hr.entity.Employee;
import com.capg.portal.retail.entity.*;
import com.capg.portal.creator.repository.*;
import com.capg.portal.catalog.repository.*;
import com.capg.portal.hr.repository.EmployeeRepository;
import com.capg.portal.retail.repository.SalesRepository;
import com.capg.portal.exception.*;

import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PublisherServiceTest {

    @Mock private PublisherRepository publisherRepository;
    @Mock private EmployeeRepository employeeRepository;
    @Mock private TitleRepository titleRepository;
    @Mock private TitleAuthorRepository titleAuthorRepository;
    @Mock private SalesRepository salesRepository;

    @InjectMocks private PublisherService publisherService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // ==================================================
    // 1. CREATE PUBLISHER
    // ==================================================

    @Test
    void createPublisher_success() {
        Publisher pub = new Publisher();
        pub.setPubId("P1");

        when(publisherRepository.existsById("P1")).thenReturn(false);
        when(publisherRepository.save(pub)).thenReturn(pub);

        Publisher result = publisherService.createPublisher(pub);

        assertNotNull(result);
        verify(publisherRepository).save(pub);
    }

    @Test
    void createPublisher_duplicateId_shouldThrow409() {
        Publisher pub = new Publisher();
        pub.setPubId("P1");

        when(publisherRepository.existsById("P1")).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class,
                () -> publisherService.createPublisher(pub));
    }

    @Test
    void createPublisher_nullId_shouldCallRepository() {
        Publisher pub = new Publisher();

        when(publisherRepository.existsById(null)).thenReturn(false);
        when(publisherRepository.save(pub)).thenReturn(pub);

        Publisher result = publisherService.createPublisher(pub);

        assertNotNull(result);
    }

    @Test
    void createPublisher_defaultCountry() {
        Publisher pub = new Publisher();
        pub.setPubId("P1");
        pub.setCountry("");

        when(publisherRepository.existsById("P1")).thenReturn(false);
        when(publisherRepository.save(pub)).thenReturn(pub);

        publisherService.createPublisher(pub);

        assertEquals("USA", pub.getCountry());
    }

    // ==================================================
    // 2. GET PUBLISHER
    // ==================================================

    @Test
    void getPublisher_success() {
        when(publisherRepository.findById("P1"))
                .thenReturn(Optional.of(new Publisher()));

        assertNotNull(publisherService.getPublisherById("P1"));
    }

    @Test
    void getPublisher_notFound_shouldThrow404() {
        when(publisherRepository.findById("P1"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> publisherService.getPublisherById("P1"));
    }

    // ==================================================
    // 3. UPDATE (PUT)
    // ==================================================

    @Test
    void updatePublisher_success() {
        Publisher existing = new Publisher();
        Publisher updates = new Publisher();

        updates.setCity("Delhi");
        updates.setCountry("India");

        when(publisherRepository.findById("P1")).thenReturn(Optional.of(existing));
        when(publisherRepository.save(existing)).thenReturn(existing);

        Publisher result = publisherService.updatePublisher("P1", updates);

        assertEquals("Delhi", result.getCity());
        assertEquals("India", result.getCountry());
    }

    @Test
    void updatePublisher_notFound_shouldThrow404() {
        when(publisherRepository.findById("P1")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> publisherService.updatePublisher("P1", new Publisher()));
    }

    @Test
    void updatePublisher_nullBody_shouldThrowException() {
        when(publisherRepository.findById("P1")).thenReturn(Optional.of(new Publisher()));

        assertThrows(NullPointerException.class,
                () -> publisherService.updatePublisher("P1", null));
    }

    // ==================================================
    // 4. PATCH
    // ==================================================

    @Test
    void patchPublisher_partialUpdate() {
        Publisher existing = new Publisher();
        existing.setCity("Old");

        Publisher updates = new Publisher();
        updates.setCity("New");

        when(publisherRepository.findById("P1")).thenReturn(Optional.of(existing));
        when(publisherRepository.save(existing)).thenReturn(existing);

        Publisher result = publisherService.patchPublisher("P1", updates);

        assertEquals("New", result.getCity());
    }

    @Test
    void patchPublisher_nullUpdate_shouldNotChange() {
        Publisher existing = new Publisher();
        existing.setCity("Same");

        Publisher updates = new Publisher(); // all null

        when(publisherRepository.findById("P1")).thenReturn(Optional.of(existing));
        when(publisherRepository.save(existing)).thenReturn(existing);

        Publisher result = publisherService.patchPublisher("P1", updates);

        assertEquals("Same", result.getCity());
    }

    @Test
    void patchPublisher_notFound_shouldThrow404() {
        when(publisherRepository.findById("P1")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> publisherService.patchPublisher("P1", new Publisher()));
    }

    // ==================================================
    // 5. FILTER
    // ==================================================

    @Test
    void filterByCity_success() {
        when(publisherRepository.findByCityIgnoreCase("mumbai"))
                .thenReturn(List.of(new Publisher()));

        assertEquals(1, publisherService.getPublishersByCity("mumbai").size());
    }

    @Test
    void filterByCity_null_shouldReturnEmptyOrCallRepo() {
        when(publisherRepository.findByCityIgnoreCase(null))
                .thenReturn(Collections.emptyList());

        List<Publisher> result = publisherService.getPublishersByCity(null);

        assertTrue(result.isEmpty());
    }

    // ==================================================
    // 6. RELATIONSHIPS
    // ==================================================

    @Test
    void getEmployees_success() {
        when(publisherRepository.findById("P1")).thenReturn(Optional.of(new Publisher()));
        when(employeeRepository.findByPublisherPubId("P1"))
                .thenReturn(List.of(new Employee()));

        assertEquals(1, publisherService.getEmployeesByPublisherId("P1").size());
    }

    @Test
    void getEmployees_notFound_shouldThrow404() {
        when(publisherRepository.findById("P1")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> publisherService.getEmployeesByPublisherId("P1"));
    }

    @Test
    void getAuthors_emptyTitles_shouldReturnEmpty() {
        when(publisherRepository.findById("P1")).thenReturn(Optional.of(new Publisher()));
        when(titleRepository.findByPublisherPubId("P1"))
                .thenReturn(Collections.emptyList());

        List<Author> result = publisherService.getAuthorsByPublisherId("P1");

        assertTrue(result.isEmpty());
    }

    @Test
    void getStores_emptySales_shouldReturnEmpty() {
        Title t = new Title();
        t.setTitleId("T1");

        when(publisherRepository.findById("P1")).thenReturn(Optional.of(new Publisher()));
        when(titleRepository.findByPublisherPubId("P1")).thenReturn(List.of(t));
        when(salesRepository.findByTitleTitleId("T1"))
                .thenReturn(Collections.emptyList());

        List<Store> result = publisherService.getStoresByPublisherId("P1");

        assertTrue(result.isEmpty());
    }
}