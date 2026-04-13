package com.capg.portal.creator.controller;

import com.capg.portal.creator.entity.Publisher;
import com.capg.portal.creator.service.PublisherService;
import com.capg.portal.hr.entity.Employee;
import com.capg.portal.hr.service.EmployeeService;
import com.capg.portal.catalog.entity.Title;
import com.capg.portal.catalog.service.TitleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/publishers")
public class PublisherController {

    private final PublisherService publisherService;
    private final EmployeeService employeeService;
    private final TitleService titleService;

    // Constructor Injection (Replaces Lombok's @RequiredArgsConstructor)
    public PublisherController(PublisherService publisherService,
                               EmployeeService employeeService,
                               TitleService titleService) {
        this.publisherService = publisherService;
        this.employeeService = employeeService;
        this.titleService = titleService;
    }

    // 1. GET Request: Get all publishers
    @GetMapping
    public ResponseEntity<List<Publisher>> getAllPublishers() {
        return new ResponseEntity<>(publisherService.getAllPublishers(), HttpStatus.OK); // 200 OK
    }

    // 2. GET Request: Get publisher by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getPublisherById(@PathVariable("id") String id) {
        try {
            Publisher publisher = publisherService.getPublisherById(id);
            return new ResponseEntity<>(publisher, HttpStatus.OK); // 200 OK
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND); // 404 Not Found
        }
    }

    // 3. POST Request: Create a new publisher
    @PostMapping
    public ResponseEntity<?> createPublisher(@Valid @RequestBody Publisher publisher, BindingResult result) {
        
        // 1. Validation Check
        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST); // 400 Bad Request
        }

        // Failsafe: If JSON passed an empty country string, force it to USA
        if (publisher.getCountry() == null || publisher.getCountry().trim().isEmpty()) {
            publisher.setCountry("USA");
        }

        // 2. Save to Database
        try {
            Publisher savedPublisher = publisherService.createPublisher(publisher);
            return new ResponseEntity<>(savedPublisher, HttpStatus.CREATED); // 201 Created
        } catch (Exception e) {
            return new ResponseEntity<>("Database Error: Publisher ID already exists or violates a rule.", HttpStatus.CONFLICT); // 409 Conflict
        }
    }

    // 4. PUT Request: Update an existing publisher
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePublisher(@PathVariable("id") String id, @Valid @RequestBody Publisher publisher, BindingResult result) {
        
        // 1. Validation Check
        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST); // 400 Bad Request
        }

        // Failsafe: If JSON passed an empty country string, force it to USA
        if (publisher.getCountry() == null || publisher.getCountry().trim().isEmpty()) {
            publisher.setCountry("USA");
        }

        // 2. Update Database
        try {
            Publisher updatedPublisher = publisherService.updatePublisher(id, publisher);
            return new ResponseEntity<>(updatedPublisher, HttpStatus.OK); // 200 OK
        } catch (Exception e) {
            return new ResponseEntity<>("Database Error: Could not update publisher.", HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error
        }
    }

    // 5. GET Request: Filter by City
    @GetMapping("/filter/city")
    public ResponseEntity<List<Publisher>> filterPublishersByCity(@RequestParam("name") String city) {
        return new ResponseEntity<>(publisherService.getPublishersByCity(city), HttpStatus.OK); // 200 OK
    }

    // 6. GET Request: Filter by State
    @GetMapping("/filter/state")
    public ResponseEntity<List<Publisher>> filterPublishersByState(@RequestParam("code") String state) {
        return new ResponseEntity<>(publisherService.getPublishersByState(state), HttpStatus.OK); // 200 OK
    }

    // 7. GET Request: Filter by Country
    @GetMapping("/filter/country")
    public ResponseEntity<List<Publisher>> filterPublishersByCountry(@RequestParam("name") String country) {
        return new ResponseEntity<>(publisherService.getPublishersByCountry(country), HttpStatus.OK); // 200 OK
    }

    // 8. GET: Employees by Publisher
    @GetMapping("/{id}/employees")
    public ResponseEntity<?> getEmployeesByPublisher(@PathVariable("id") String pubId) {
        try {
            publisherService.getPublisherById(pubId); // ensure exists
            List<Employee> employees = employeeService.getEmployeesByPublisher(pubId);
            return new ResponseEntity<>(employees, HttpStatus.OK); // 200 OK
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND); // 404
        }
    }

    // 9. GET: Titles by Publisher
    @GetMapping("/{id}/titles")
    public ResponseEntity<?> getTitlesByPublisher(@PathVariable("id") String pubId) {
        try {
            publisherService.getPublisherById(pubId); // ensure exists
            return new ResponseEntity<>(titleService.getTitlesByPublisher(pubId), HttpStatus.OK); // 200 OK
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND); // 404
        }
    }

    // 10. POST: Create a title under a publisher
    @PostMapping("/{id}/titles")
    public ResponseEntity<?> createTitleForPublisher(@PathVariable("id") String pubId,
                                                     @Valid @RequestBody Title title,
                                                     BindingResult result) {

        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST); // 400
        }

        try {
            Publisher publisher = publisherService.getPublisherById(pubId);
            title.setPublisher(publisher);
            Title savedTitle = titleService.createTitle(title);
            return new ResponseEntity<>(savedTitle, HttpStatus.CREATED); // 201
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND); // 404 if publisher missing
        } catch (Exception e) {
            return new ResponseEntity<>("Database Error: Could not create title.", HttpStatus.CONFLICT); // 409/500 fallback
        }
    }
}
