package com.capg.portal.hr.service;

import com.capg.portal.catalog.entity.Title;
import com.capg.portal.catalog.repository.TitleRepository;
import com.capg.portal.creator.entity.Publisher;
import com.capg.portal.exception.ResourceAlreadyExistsException;
import com.capg.portal.exception.ResourceNotFoundException;
import com.capg.portal.hr.entity.Employee;
import com.capg.portal.hr.entity.Job;
import com.capg.portal.hr.repository.EmployeeRepository;
import com.capg.portal.hr.repository.JobRepository;
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
public class EmployeeServiceTest 
{
    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private JobRepository jobRepository;

    @Mock
    private TitleRepository titleRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private Job validJob;
    private Publisher validPublisher;
    private Employee validEmployee;

    @BeforeEach
    public void setup() 
    {
        validJob = new Job((short) 99, "Senior Galactic Analyst", 100, 200);
        validPublisher = new Publisher("9999", "Cosmic Books", "Mars City", "MC", "Mars");
        validEmployee = new Employee("ZXX98765F", "Zaphod", "B", "Beeblebrox", validJob, 150, validPublisher, LocalDateTime.now());
    }

    // --- CREATE EMPLOYEE TESTS ---

    @Test
    public void testCreateEmployee_Success() 
    {
        when(employeeRepository.existsById("ZXX98765F")).thenReturn(false);
        when(jobRepository.findById((short) 99)).thenReturn(Optional.of(validJob));
        when(employeeRepository.save(any(Employee.class))).thenReturn(validEmployee);

        Employee saved = employeeService.createEmployee(validEmployee);

        assertNotNull(saved);
        assertEquals("ZXX98765F", saved.getEmpId());
        verify(employeeRepository, times(1)).save(validEmployee);
    }

    @Test
    public void testCreateEmployee_DefaultsHireDate() 
    {
        validEmployee.setHireDate(null);
        
        when(employeeRepository.existsById("ZXX98765F")).thenReturn(false);
        when(jobRepository.findById((short) 99)).thenReturn(Optional.of(validJob));
        when(employeeRepository.save(any(Employee.class))).thenReturn(validEmployee);

        Employee saved = employeeService.createEmployee(validEmployee);

        assertNotNull(saved.getHireDate());
    }

    @Test
    public void testCreateEmployee_DefaultsMinit() 
    {
        validEmployee.setMinit(" ");
        
        when(employeeRepository.existsById("ZXX98765F")).thenReturn(false);
        when(jobRepository.findById((short) 99)).thenReturn(Optional.of(validJob));
        when(employeeRepository.save(any(Employee.class))).thenReturn(validEmployee);

        Employee saved = employeeService.createEmployee(validEmployee);

        assertNull(saved.getMinit());
    }

    @Test
    public void testCreateEmployee_IdAlreadyExists_ThrowsException() 
    {
        when(employeeRepository.existsById("ZXX98765F")).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> {
            employeeService.createEmployee(validEmployee);
        });

        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    public void testCreateEmployee_JobLevelBelowMin_ThrowsException() 
    {
        validEmployee.setJobLvl(50); 
        
        when(employeeRepository.existsById("ZXX98765F")).thenReturn(false);
        when(jobRepository.findById((short) 99)).thenReturn(Optional.of(validJob));

        assertThrows(IllegalArgumentException.class, () -> {
            employeeService.createEmployee(validEmployee);
        });
    }

    @Test
    public void testCreateEmployee_JobLevelAboveMax_ThrowsException() 
    {
        validEmployee.setJobLvl(250); 
        
        when(employeeRepository.existsById("ZXX98765F")).thenReturn(false);
        when(jobRepository.findById((short) 99)).thenReturn(Optional.of(validJob));

        assertThrows(IllegalArgumentException.class, () -> {
            employeeService.createEmployee(validEmployee);
        });
    }

    @Test
    public void testCreateEmployee_JobNotFound_ThrowsException() 
    {
        when(employeeRepository.existsById("ZXX98765F")).thenReturn(false);
        when(jobRepository.findById((short) 99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.createEmployee(validEmployee);
        });
    }

    // --- READ EMPLOYEE TESTS ---

    @Test
    public void testGetAllEmployees_Success() 
    {
        when(employeeRepository.findAll()).thenReturn(Arrays.asList(validEmployee));

        List<Employee> list = employeeService.getAllEmployees();

        assertEquals(1, list.size());
    }

    @Test
    public void testGetEmployeeById_Success() 
    {
        when(employeeRepository.findById("ZXX98765F")).thenReturn(Optional.of(validEmployee));

        Employee found = employeeService.getEmployeeById("ZXX98765F");

        assertNotNull(found);
        assertEquals("ZXX98765F", found.getEmpId());
    }

    @Test
    public void testGetEmployeeById_NotFound_ThrowsException() 
    {
        when(employeeRepository.findById("NON99999M")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.getEmployeeById("NON99999M");
        });
    }

    // --- UPDATE EMPLOYEE TESTS ---

    @Test
    public void testUpdateEmployee_Success() 
    {
        Employee newDetails = new Employee("ZXX98765F", "Arthur", "P", "Dent", validJob, 120, validPublisher, LocalDateTime.now());
        
        when(employeeRepository.findById("ZXX98765F")).thenReturn(Optional.of(validEmployee));
        when(jobRepository.findById((short) 99)).thenReturn(Optional.of(validJob));
        when(employeeRepository.save(any(Employee.class))).thenReturn(newDetails);

        Employee updated = employeeService.updateEmployee("ZXX98765F", newDetails);

        assertEquals("Arthur", updated.getFname());
        assertEquals("Dent", updated.getLname());
    }

    @Test
    public void testUpdateEmployee_NotFound_ThrowsException() 
    {
        when(employeeRepository.findById("NON99999M")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.updateEmployee("NON99999M", validEmployee);
        });
    }

    @Test
    public void testUpdateEmployee_InvalidJobLevel_ThrowsException() 
    {
        Employee badDetails = new Employee("ZXX98765F", "Arthur", "P", "Dent", validJob, 300, validPublisher, LocalDateTime.now());
        
        when(employeeRepository.findById("ZXX98765F")).thenReturn(Optional.of(validEmployee));
        when(jobRepository.findById((short) 99)).thenReturn(Optional.of(validJob));

        assertThrows(IllegalArgumentException.class, () -> {
            employeeService.updateEmployee("ZXX98765F", badDetails);
        });
    }

    // --- PATCH EMPLOYEE TESTS ---

    @Test
    public void testPatchEmployee_Success() 
    {
        Employee updates = new Employee();
        updates.setFname("Trillian");
        updates.setJobLvl(199);
        
        when(employeeRepository.findById("ZXX98765F")).thenReturn(Optional.of(validEmployee));
        when(jobRepository.findById((short) 99)).thenReturn(Optional.of(validJob));
        when(employeeRepository.save(any(Employee.class))).thenReturn(validEmployee);

        Employee patched = employeeService.patchEmployee("ZXX98765F", updates);

        assertEquals("Trillian", patched.getFname());
        assertEquals("Beeblebrox", patched.getLname()); 
        assertEquals(199, patched.getJobLvl());
    }

    @Test
    public void testPatchEmployee_NotFound_ThrowsException() 
    {
        when(employeeRepository.findById("NON99999M")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.patchEmployee("NON99999M", new Employee());
        });
    }

    @Test
    public void testPatchEmployee_InvalidJobLevel_ThrowsException() 
    {
        Employee updates = new Employee();
        updates.setJobLvl(10); 
        
        when(employeeRepository.findById("ZXX98765F")).thenReturn(Optional.of(validEmployee));
        when(jobRepository.findById((short) 99)).thenReturn(Optional.of(validJob));

        assertThrows(IllegalArgumentException.class, () -> {
            employeeService.patchEmployee("ZXX98765F", updates);
        });
    }

    // --- FILTER QUERIES TESTS ---

    @Test
    public void testGetEmployeesByJobId_Success() 
    {
        when(employeeRepository.findByJobJobId((short) 99)).thenReturn(Arrays.asList(validEmployee));
        
        List<Employee> list = employeeService.getEmployeesByJobId((short) 99);
        
        assertEquals(1, list.size());
    }

    @Test
    public void testGetEmployeesBelowJobLevel_Success() 
    {
        when(employeeRepository.findByJobLvlLessThan(200)).thenReturn(Arrays.asList(validEmployee));
        
        List<Employee> list = employeeService.getEmployeesBelowJobLevel(200);
        
        assertEquals(1, list.size());
    }

    @Test
    public void testGetEmployeesHiredBefore_Success() 
    {
        LocalDateTime date = LocalDateTime.now();
        when(employeeRepository.findByHireDateBefore(date)).thenReturn(Arrays.asList(validEmployee));
        
        List<Employee> list = employeeService.getEmployeesHiredBefore(date);
        
        assertEquals(1, list.size());
    }

    @Test
    public void testGetEmployeesByPublisher_Success() 
    {
        when(employeeRepository.findByPublisherPubId("9999")).thenReturn(Arrays.asList(validEmployee));
        
        List<Employee> list = employeeService.getEmployeesByPublisher("9999");
        
        assertEquals(1, list.size());
    }

    // --- RELATIONAL HOP TESTS ---

    @Test
    public void testGetJobByEmployeeId_Success() 
    {
        when(employeeRepository.findById("ZXX98765F")).thenReturn(Optional.of(validEmployee));
        
        Job job = employeeService.getJobByEmployeeId("ZXX98765F");
        
        assertNotNull(job);
        assertEquals((short) 99, job.getJobId());
    }

    @Test
    public void testGetJobByEmployeeId_NotFound_ThrowsException() 
    {
        when(employeeRepository.findById("NON99999M")).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.getJobByEmployeeId("NON99999M");
        });
    }

    @Test
    public void testGetPublisherByEmployeeId_Success() 
    {
        when(employeeRepository.findById("ZXX98765F")).thenReturn(Optional.of(validEmployee));
        
        Publisher pub = employeeService.getPublisherByEmployeeId("ZXX98765F");
        
        assertNotNull(pub);
        assertEquals("9999", pub.getPubId());
    }

    @Test
    public void testGetPublisherByEmployeeId_NotFound_ThrowsException() 
    {
        when(employeeRepository.findById("NON99999M")).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.getPublisherByEmployeeId("NON99999M");
        });
    }

    @Test
    public void testGetTitlesByEmployeePublisher_Success() 
    {
        Title t1 = new Title(); t1.setTitleId("TL001");
        
        when(employeeRepository.findById("ZXX98765F")).thenReturn(Optional.of(validEmployee));
        when(titleRepository.findByPublisherPubId("9999")).thenReturn(Arrays.asList(t1));
        
        List<Title> titles = employeeService.getTitlesByEmployeePublisher("ZXX98765F");
        
        assertEquals(1, titles.size());
        assertEquals("TL001", titles.get(0).getTitleId());
    }

    @Test
    public void testGetTitlesByEmployeePublisher_NotFound_ThrowsException() 
    {
        when(employeeRepository.findById("NON99999M")).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.getTitlesByEmployeePublisher("NON99999M");
        });
    }
}