package com.capg.portal.catalog.service;

import com.capg.portal.catalog.entity.Title;
import com.capg.portal.catalog.repository.TitleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TitleService {

    private final TitleRepository titleRepository;

    // Constructor Injection (Replaces Lombok)
    public TitleService(TitleRepository titleRepository) {
        this.titleRepository = titleRepository;
    }

    public Title createTitle(Title title) {
        return titleRepository.save(title);
    }

    public List<Title> getAllTitles() {
        return titleRepository.findAll();
    }

    public Title getTitleById(String id) {
        return titleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Title not found with ID: " + id));
    }

    public Title updateTitle(String id, Title titleDetails) {
        Title existingTitle = getTitleById(id);
        existingTitle.setTitleName(titleDetails.getTitleName());
        existingTitle.setType(titleDetails.getType());
        existingTitle.setPublisher(titleDetails.getPublisher());
        existingTitle.setPrice(titleDetails.getPrice());
        existingTitle.setAdvance(titleDetails.getAdvance());
        existingTitle.setRoyalty(titleDetails.getRoyalty());
        existingTitle.setYtdSales(titleDetails.getYtdSales());
        existingTitle.setNotes(titleDetails.getNotes());
        existingTitle.setPubdate(titleDetails.getPubdate());
        return titleRepository.save(existingTitle);
    }

    public List<Title> getTitlesByPriceLessThan(Double maxPrice) {
        return titleRepository.findByPriceLessThan(maxPrice);
    }

    // New Method: Filter by Type
    public List<Title> getTitlesByType(String type) {
        return titleRepository.findByTypeIgnoreCase(type);
    }

    // New Method: Filter by Publisher ID
    public List<Title> getTitlesByPublisher(String pubId) {
        return titleRepository.findByPublisherPubId(pubId);
    }

    // New Method: Filter by Publish Date Before
    public List<Title> getTitlesPublishedBefore(LocalDateTime date) {
        return titleRepository.findByPubdateBefore(date);
    }
}