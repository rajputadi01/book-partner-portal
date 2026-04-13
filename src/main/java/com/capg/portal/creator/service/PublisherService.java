package com.capg.portal.creator.service;

import com.capg.portal.creator.entity.Publisher;
import com.capg.portal.creator.repository.PublisherRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublisherService {

    private final PublisherRepository publisherRepository;

    // Constructor Injection (Replaces Lombok's @RequiredArgsConstructor)
    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    public Publisher createPublisher(Publisher publisher) {
        return publisherRepository.save(publisher);
    }

    public List<Publisher> getAllPublishers() {
        return publisherRepository.findAll();
    }

    public Publisher getPublisherById(String id) {
        return publisherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Publisher not found with ID: " + id));
    }

    public Publisher updatePublisher(String id, Publisher pubDetails) {
        Publisher existingPublisher = getPublisherById(id);
        existingPublisher.setPubName(pubDetails.getPubName());
        existingPublisher.setCity(pubDetails.getCity());
        existingPublisher.setState(pubDetails.getState());
        existingPublisher.setCountry(pubDetails.getCountry());
        return publisherRepository.save(existingPublisher);
    }
    
    // Filter by City
    public List<Publisher> getPublishersByCity(String city) {
        return publisherRepository.findByCityIgnoreCase(city);
    }

    // Filter by State
    public List<Publisher> getPublishersByState(String state) {
        return publisherRepository.findByStateIgnoreCase(state);
    }

    // Filter by Country
    public List<Publisher> getPublishersByCountry(String country) {
        return publisherRepository.findByCountryIgnoreCase(country);
    }
}