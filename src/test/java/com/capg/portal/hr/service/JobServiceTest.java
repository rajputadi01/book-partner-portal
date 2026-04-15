package com.capg.portal.hr.service;

import com.capg.portal.hr.entity.*;
import com.capg.portal.creator.entity.Publisher;
import com.capg.portal.hr.repository.*;
import com.capg.portal.exception.ResourceNotFoundException;

import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JobServiceTest {

    @Mock private JobRepository jobRepository;
    @Mock private EmployeeRepository employeeRepository;

    @InjectMocks private JobService jobService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // ==================================================
    // 1. CREATE JOB
    // ==================================================

    @Test
    void TC01_createJob_valid() {
        Job job = new Job();
        when(jobRepository.save(job)).thenReturn(job);

        Job result = jobService.createJob(job);

        assertNotNull(result);
        verify(jobRepository).save(job);
    }

   
    @Test
    void TC03_createJob_repositoryFailure() {
        Job job = new Job();

        when(jobRepository.save(job)).thenThrow(new RuntimeException("DB Error"));

        assertThrows(RuntimeException.class,
                () -> jobService.createJob(job));
    }

    // ==================================================
    // 2. GET ALL JOBS
    // ==================================================

    @Test
    void TC04_getAllJobs_multipleRecords() {
        when(jobRepository.findAll()).thenReturn(List.of(new Job(), new Job()));

        List<Job> result = jobService.getAllJobs();

        assertEquals(2, result.size());
    }

    @Test
    void TC05_getAllJobs_empty() {
        when(jobRepository.findAll()).thenReturn(Collections.emptyList());

        assertTrue(jobService.getAllJobs().isEmpty());
    }

    @Test
    void TC06_getAllJobs_repositoryFailure() {
        when(jobRepository.findAll()).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class,
                () -> jobService.getAllJobs());
    }

    // ==================================================
    // 3. GET JOB BY ID
    // ==================================================

    @Test
    void TC07_getJobById_valid() {
        Job job = new Job();
        job.setJobId((short) 1);

        when(jobRepository.findById((short) 1)).thenReturn(Optional.of(job));

        assertEquals(1, jobService.getJobById((short) 1).getJobId().intValue());
    }

    @Test
    void TC08_getJobById_notFound() {
        when(jobRepository.findById((short) 1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> jobService.getJobById((short) 1));
    }

    

    // ==================================================
    // 4. UPDATE JOB (PUT)
    // ==================================================

    @Test
    void TC10_updateJob_valid() {
        Job existing = new Job();
        Job updates = new Job();
        updates.setJobDesc("Updated");

        when(jobRepository.findById((short) 1)).thenReturn(Optional.of(existing));
        when(jobRepository.save(any())).thenReturn(existing);

        Job result = jobService.updateJob((short) 1, updates);

        assertEquals("Updated", result.getJobDesc());
    }

    @Test
    void TC11_updateJob_notFound() {
        when(jobRepository.findById((short) 1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> jobService.updateJob((short) 1, new Job()));
    }

    @Test
    void TC12_updateJob_nullBody() {
        when(jobRepository.findById((short) 1)).thenReturn(Optional.of(new Job()));

        assertThrows(NullPointerException.class,
                () -> jobService.updateJob((short) 1, null));
    }

    // ==================================================
    // 5. PATCH JOB
    // ==================================================

    

    @Test
    void TC14_patch_allFields() {
        Job existing = new Job();
        Job updates = new Job();

        updates.setJobDesc("New");
        updates.setMinLvl(10);
        updates.setMaxLvl(50);

        when(jobRepository.findById((short) 1)).thenReturn(Optional.of(existing));
        when(jobRepository.save(any())).thenReturn(existing);

        Job result = jobService.patchJob((short) 1, updates);

        assertEquals("New", result.getJobDesc());
        assertEquals(10, result.getMinLvl());
    }

    @Test
    void TC15_patch_notFound() {
        when(jobRepository.findById((short) 1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> jobService.patchJob((short) 1, new Job()));
    }

    @Test
    void TC16_patch_nullBody() {
        when(jobRepository.findById((short) 1)).thenReturn(Optional.of(new Job()));

        assertThrows(NullPointerException.class,
                () -> jobService.patchJob((short) 1, null));
    }

    // ==================================================
    // 6. FILTER JOBS
    // ==================================================

    @Test
    void TC17_filter_validRange() {
        when(jobRepository.findByMinLvlGreaterThanEqualAndMaxLvlLessThanEqual(10, 50))
                .thenReturn(List.of(new Job()));

        assertEquals(1, jobService.getJobsBetween(10, 50).size());
    }

    @Test
    void TC18_filter_noResults() {
        when(jobRepository.findByMinLvlGreaterThanEqualAndMaxLvlLessThanEqual(10, 50))
                .thenReturn(Collections.emptyList());

        assertTrue(jobService.getJobsBetween(10, 50).isEmpty());
    }

    @Test
    void TC19_filter_invalidRange() {
        List<Job> result = jobService.getJobsBetween(50, 10);

        assertNotNull(result); // depends on repo behavior
    }

    // ==================================================
    // 7. GET EMPLOYEES BY JOB
    // ==================================================

    @Test
    void TC20_getEmployees_success() {
        when(jobRepository.findById((short) 1)).thenReturn(Optional.of(new Job()));
        when(employeeRepository.findByJobJobId((short) 1))
                .thenReturn(List.of(new Employee()));

        assertEquals(1, jobService.getEmployeesByJobId((short) 1).size());
    }

    @Test
    void TC21_getEmployees_notFound() {
        when(jobRepository.findById((short) 1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> jobService.getEmployeesByJobId((short) 1));
    }

    // ==================================================
    // 8. GET PUBLISHERS BY JOB
    // ==================================================

    @Test
    void TC22_getPublishers_success() {
        Employee emp = new Employee();
        emp.setPublisher(new Publisher());

        when(jobRepository.findById((short) 1)).thenReturn(Optional.of(new Job()));
        when(employeeRepository.findByJobJobId((short) 1))
                .thenReturn(List.of(emp));

        assertEquals(1, jobService.getPublishersByJobId((short) 1).size());
    }

    @Test
    void TC23_getPublishers_nullPublisherFiltered() {
        Employee emp = new Employee();
        emp.setPublisher(null);

        when(jobRepository.findById((short) 1)).thenReturn(Optional.of(new Job()));
        when(employeeRepository.findByJobJobId((short) 1))
                .thenReturn(List.of(emp));

        assertEquals(0, jobService.getPublishersByJobId((short) 1).size());
    }

    // ==================================================
    // 9. ASSIGN EMPLOYEE TO JOB
    // ==================================================

    @Test
    void TC24_assignEmployee_success() {
        Job job = new Job();
        Employee emp = new Employee();

        when(jobRepository.findById((short) 1)).thenReturn(Optional.of(job));
        when(employeeRepository.findById("E1")).thenReturn(Optional.of(emp));
        when(employeeRepository.save(any())).thenReturn(emp);

        Employee result = jobService.assignEmployeeToJob((short) 1, "E1", 5);

        assertEquals(job, result.getJob());
        assertEquals(5, result.getJobLvl());
    }

    @Test
    void TC25_assignEmployee_noJobLvl() {
        Job job = new Job();
        Employee emp = new Employee();

        when(jobRepository.findById((short) 1)).thenReturn(Optional.of(job));
        when(employeeRepository.findById("E1")).thenReturn(Optional.of(emp));
        when(employeeRepository.save(any())).thenReturn(emp);

        Employee result = jobService.assignEmployeeToJob((short) 1, "E1", null);

        assertEquals(job, result.getJob());
    }

    @Test
    void TC26_assignEmployee_empNotFound() {
        when(jobRepository.findById((short) 1)).thenReturn(Optional.of(new Job()));
        when(employeeRepository.findById("E1")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> jobService.assignEmployeeToJob((short) 1, "E1", 5));
    }

    @Test
    void TC27_assignEmployee_jobNotFound() {
        when(jobRepository.findById((short) 1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> jobService.assignEmployeeToJob((short) 1, "E1", 5));
    }
}