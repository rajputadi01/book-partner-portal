package com.capg.portal.catalog.service;

import com.capg.portal.catalog.entity.TitleAuthor;
import com.capg.portal.catalog.entity.TitleAuthorId;
import com.capg.portal.catalog.repository.TitleAuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TitleAuthorService {

    private final TitleAuthorRepository titleAuthorRepository;

    public TitleAuthorService(TitleAuthorRepository titleAuthorRepository) {
        this.titleAuthorRepository = titleAuthorRepository;
    }

    public TitleAuthor createTitleAuthor(TitleAuthor titleAuthor) {

        String titleId = titleAuthor.getTitle().getTitleId();
        Integer newRoyalty = titleAuthor.getRoyaltyPer();

        List<TitleAuthor> existing = titleAuthorRepository.findByTitleTitleId(titleId);

        
        int total = existing.stream()
                .mapToInt(t -> t.getRoyaltyPer())
                .sum();

        if (total + newRoyalty > 100) {
            throw new RuntimeException("Total royalty cannot exceed 100%");
        }

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

        String titleId = existing.getTitle().getTitleId();

        List<TitleAuthor> list = titleAuthorRepository.findByTitleTitleId(titleId);

        int total = list.stream()
                .filter(t -> !(t.getAuthor().getAuId().equals(id.getAuthor())
                            && t.getTitle().getTitleId().equals(id.getTitle())))
                .mapToInt(t -> t.getRoyaltyPer())
                .sum();

        if (total + details.getRoyaltyPer() > 100) {
            throw new RuntimeException("Total royalty cannot exceed 100%");
        }

        existing.setRoyaltyPer(details.getRoyaltyPer());
        existing.setAuOrd(details.getAuOrd());

        return titleAuthorRepository.save(existing);
    }
    
    public List<TitleAuthor> getTitleAuthorsByRoyaltyLessThan(Integer maxRoyalty) {
        return titleAuthorRepository.findByRoyaltyPerLessThan(maxRoyalty);
    }

    public List<TitleAuthor> getTitleAuthorsByAuthorId(String auId) {
        return titleAuthorRepository.findByAuthorAuId(auId);
    }

    public List<TitleAuthor> getTitleAuthorsByTitleId(String titleId) {
        return titleAuthorRepository.findByTitleTitleId(titleId);
    }
    
    public List<TitleAuthor> search(String auId, String titleId, Integer maxRoyalty, Integer minRoyalty) {

        List<TitleAuthor> list = titleAuthorRepository.findAll();

        if (auId != null) {
            list = list.stream()
                    .filter(t -> t.getAuthor().getAuId().equals(auId))
                    .toList();
        }

        if (titleId != null) {
            list = list.stream()
                    .filter(t -> t.getTitle().getTitleId().equals(titleId))
                    .toList();
        }

        if (maxRoyalty != null) {
            list = list.stream()
                    .filter(t -> t.getRoyaltyPer() <= maxRoyalty)
                    .toList();
        }
        
        if (minRoyalty != null) {
            list = list.stream()
                    .filter(t -> t.getRoyaltyPer() >= minRoyalty)
                    .toList();
        }

        return list;
    }
}