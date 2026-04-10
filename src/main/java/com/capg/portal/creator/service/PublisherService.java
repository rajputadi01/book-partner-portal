package com.capg.portal.creator.service;

import com.capg.portal.creator.entity.Publisher;
import com.capg.portal.creator.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PublisherService {

    private final PublisherRepository publisherRepository;

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
}