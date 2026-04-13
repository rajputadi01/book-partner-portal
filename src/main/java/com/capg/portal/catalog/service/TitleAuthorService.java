package com.capg.portal.catalog.service;

import com.capg.portal.catalog.entity.TitleAuthor;
import com.capg.portal.catalog.entity.TitleAuthorId;
import com.capg.portal.catalog.repository.TitleAuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TitleAuthorService {

    private final TitleAuthorRepository titleAuthorRepository;

    // Constructor Injection
    public TitleAuthorService(TitleAuthorRepository titleAuthorRepository) {
        this.titleAuthorRepository = titleAuthorRepository;
    }

    public TitleAuthor createTitleAuthor(TitleAuthor titleAuthor) {
        return titleAuthorRepository.save(titleAuthor);
    }

    public List<TitleAuthor> getAllTitleAuthors() {
        return titleAuthorRepository.findAll();
    }

    public TitleAuthor getTitleAuthorById(TitleAuthorId id) {
        return titleAuthorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TitleAuthor mapping not found"));
    }

    public TitleAuthor updateTitleAuthor(TitleAuthorId id, TitleAuthor details) {
        TitleAuthor existing = getTitleAuthorById(id);
        existing.setAuOrd(details.getAuOrd());
        existing.setRoyaltyPer(details.getRoyaltyPer());
        return titleAuthorRepository.save(existing);
    }
    
    public List<TitleAuthor> getTitleAuthorsByRoyaltyLessThan(Integer maxRoyalty) {
        return titleAuthorRepository.findByRoyaltyPerLessThan(maxRoyalty);
    }

    // New Method: Get titles by Author ID
    public List<TitleAuthor> getTitleAuthorsByAuthorId(String auId) {
        return titleAuthorRepository.findByAuthorAuId(auId);
    }

    // New Method: Get authors by Title ID
    public List<TitleAuthor> getTitleAuthorsByTitleId(String titleId) {
        return titleAuthorRepository.findByTitleTitleId(titleId);
    }
}