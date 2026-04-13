package com.capg.portal.catalog.service;

import com.capg.portal.catalog.entity.Title;
import com.capg.portal.catalog.entity.TitleAuthor;
import com.capg.portal.catalog.repository.TitleAuthorRepository;
import com.capg.portal.catalog.repository.TitleRepository;
import com.capg.portal.finance.entity.RoyaltySchedule;
import com.capg.portal.finance.repository.RoyaltyScheduleRepository;
import com.capg.portal.retail.entity.Sales;
import com.capg.portal.retail.repository.SalesRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TitleService {

    private final TitleRepository titleRepository;
    private final SalesRepository salesRepository;
    private final RoyaltyScheduleRepository royaltyRepository;
    private final TitleAuthorRepository titleAuthorRepository;

    // Update your constructor to include them
    public TitleService(TitleRepository titleRepository, 
                        SalesRepository salesRepository, 
                        RoyaltyScheduleRepository royaltyRepository, 
                        TitleAuthorRepository titleAuthorRepository) {
        this.titleRepository = titleRepository;
        this.salesRepository = salesRepository;
        this.royaltyRepository = royaltyRepository;
        this.titleAuthorRepository = titleAuthorRepository;
    }

    // 1. Fetch Sales for a Title
    public List<Sales> getSalesByTitle(String titleId) {
        return salesRepository.findByTitleTitleId(titleId);
    }

    // 2. Fetch Royalties for a Title
    public List<RoyaltySchedule> getRoyaltiesByTitle(String titleId) {
        return royaltyRepository.findByTitleTitleId(titleId);
    }

    // 3. Fetch Authors for a Title
    public List<TitleAuthor> getAuthorsByTitle(String titleId) {
        return titleAuthorRepository.findByTitleTitleId(titleId);
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