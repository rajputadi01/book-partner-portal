package com.capg.portal.creator.service;

import com.capg.portal.creator.entity.Publisher;
import com.capg.portal.creator.entity.Author;
import com.capg.portal.catalog.entity.Title;
import com.capg.portal.catalog.entity.TitleAuthor;
import com.capg.portal.hr.entity.Employee;
import com.capg.portal.retail.entity.Sales;
import com.capg.portal.retail.entity.Store;
import com.capg.portal.creator.repository.PublisherRepository;
import com.capg.portal.catalog.repository.TitleRepository;
import com.capg.portal.catalog.repository.TitleAuthorRepository;
import com.capg.portal.hr.repository.EmployeeRepository;
import com.capg.portal.retail.repository.SalesRepository;
import com.capg.portal.exception.ResourceAlreadyExistsException;
import com.capg.portal.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublisherService 
{
    private final PublisherRepository publisherRepository;
    private final EmployeeRepository employeeRepository;
    private final TitleRepository titleRepository;
    private final TitleAuthorRepository titleAuthorRepository;
    private final SalesRepository salesRepository;

    public PublisherService(PublisherRepository publisherRepository, EmployeeRepository employeeRepository, TitleRepository titleRepository, TitleAuthorRepository titleAuthorRepository, SalesRepository salesRepository) 
    {
        this.publisherRepository = publisherRepository;
        this.employeeRepository = employeeRepository;
        this.titleRepository = titleRepository;
        this.titleAuthorRepository = titleAuthorRepository;
        this.salesRepository = salesRepository;
    }

    public Publisher createPublisher(Publisher publisher) 
    {
        if (publisherRepository.existsById(publisher.getPubId())) 
        {
            throw new ResourceAlreadyExistsException("Publisher with ID " + publisher.getPubId() + " already exists.");
        }
        
        if (publisher.getCountry() == null || publisher.getCountry().trim().isEmpty()) 
        {
            publisher.setCountry("USA");
        }
        
        return publisherRepository.save(publisher);
    }

    public List<Publisher> getAllPublishers() 
    {
        return publisherRepository.findAll();
    }

    public Publisher getPublisherById(String id) 
    {
        return publisherRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Publisher not found with ID: " + id));
    }

    public Publisher updatePublisher(String id, Publisher pubDetails) 
    {
        Publisher existingPublisher = getPublisherById(id);
        
        existingPublisher.setPubName(pubDetails.getPubName());
        existingPublisher.setCity(pubDetails.getCity());
        existingPublisher.setState(pubDetails.getState());
        
        if (pubDetails.getCountry() == null || pubDetails.getCountry().trim().isEmpty()) 
        {
            existingPublisher.setCountry("USA");
        } 
        else 
        {
            existingPublisher.setCountry(pubDetails.getCountry());
        }
        
        return publisherRepository.save(existingPublisher);
    }

    public Publisher patchPublisher(String id, Publisher updates) 
    {
        Publisher existingPublisher = getPublisherById(id);

        if (updates.getPubName() != null) 
        {
            existingPublisher.setPubName(updates.getPubName());
        }
        if (updates.getCity() != null) 
        {
            existingPublisher.setCity(updates.getCity());
        }
        if (updates.getState() != null) 
        {
            existingPublisher.setState(updates.getState());
        }
        if (updates.getCountry() != null) 
        {
            if (updates.getCountry().trim().isEmpty()) 
            {
                existingPublisher.setCountry("USA");
            } 
            else 
            {
                existingPublisher.setCountry(updates.getCountry());
            }
        }

        return publisherRepository.save(existingPublisher);
    }
    
    public List<Publisher> getPublishersByCity(String city) 
    {
        return publisherRepository.findByCityIgnoreCase(city);
    }

    public List<Publisher> getPublishersByState(String state) 
    {
        return publisherRepository.findByStateIgnoreCase(state);
    }

    public List<Publisher> getPublishersByCountry(String country) 
    {
        return publisherRepository.findByCountryIgnoreCase(country);
    }

    public List<Employee> getEmployeesByPublisherId(String pubId) 
    {
        getPublisherById(pubId);
        return employeeRepository.findByPublisherPubId(pubId);
    }

    public List<Title> getTitlesByPublisherId(String pubId) 
    {
        getPublisherById(pubId);
        return titleRepository.findByPublisherPubId(pubId);
    }

    public List<Author> getAuthorsByPublisherId(String pubId) 
    {
        getPublisherById(pubId);
        return titleRepository.findByPublisherPubId(pubId).stream()
                .flatMap(title -> titleAuthorRepository.findByTitleTitleId(title.getTitleId()).stream())
                .map(TitleAuthor::getAuthor)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Store> getStoresByPublisherId(String pubId) 
    {
        getPublisherById(pubId);
        return titleRepository.findByPublisherPubId(pubId).stream()
                .flatMap(title -> salesRepository.findByTitleTitleId(title.getTitleId()).stream())
                .map(Sales::getStore)
                .distinct()
                .collect(Collectors.toList());
    }
}