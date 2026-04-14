package com.capg.portal.catalog.service;

import com.capg.portal.catalog.entity.TitleAuthor;
import com.capg.portal.catalog.entity.TitleAuthorId;
import com.capg.portal.catalog.repository.TitleAuthorRepository;
import com.capg.portal.exception.ResourceAlreadyExistsException;
import com.capg.portal.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TitleAuthorService 
{
    private final TitleAuthorRepository titleAuthorRepository;

    public TitleAuthorService(TitleAuthorRepository titleAuthorRepository) 
    {
        this.titleAuthorRepository = titleAuthorRepository;
    }

    public TitleAuthor createTitleAuthor(TitleAuthor titleAuthor) 
    {
        TitleAuthorId id = new TitleAuthorId(titleAuthor.getAuthor().getAuId(), titleAuthor.getTitle().getTitleId());
        
        if (titleAuthorRepository.existsById(id)) 
        {
            throw new ResourceAlreadyExistsException("This Author is already assigned to this Title.");
        }

        validateRoyaltyLimit(titleAuthor.getTitle().getTitleId(), titleAuthor.getRoyaltyPer(), id);
        return titleAuthorRepository.save(titleAuthor);
    }

    public List<TitleAuthor> getAllTitleAuthors() 
    {
        return titleAuthorRepository.findAll();
    }

    public TitleAuthor getTitleAuthorById(TitleAuthorId id) 
    {
        return titleAuthorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TitleAuthor mapping not found."));
    }

    public TitleAuthor updateTitleAuthor(TitleAuthorId id, TitleAuthor details) 
    {
        TitleAuthor existing = getTitleAuthorById(id);
        
        validateRoyaltyLimit(id.getTitle(), details.getRoyaltyPer(), id);

        existing.setRoyaltyPer(details.getRoyaltyPer());
        existing.setAuOrd(details.getAuOrd());

        return titleAuthorRepository.save(existing);
    }

    public TitleAuthor patchTitleAuthor(TitleAuthorId id, TitleAuthor updates) 
    {
        TitleAuthor existing = getTitleAuthorById(id);

        if (updates.getRoyaltyPer() != null) 
        {
            validateRoyaltyLimit(id.getTitle(), updates.getRoyaltyPer(), id);
            existing.setRoyaltyPer(updates.getRoyaltyPer());
        }
        if (updates.getAuOrd() != null) 
        {
            existing.setAuOrd(updates.getAuOrd());
        }

        return titleAuthorRepository.save(existing);
    }

    public List<TitleAuthor> getTitleAuthorsByRoyaltyLessThan(Integer maxRoyalty) 
    {
        return titleAuthorRepository.findByRoyaltyPerLessThan(maxRoyalty);
    }

    public List<TitleAuthor> getTitleAuthorsByAuthorId(String auId) 
    {
        return titleAuthorRepository.findByAuthorAuId(auId);
    }

    public List<TitleAuthor> getTitleAuthorsByTitleId(String titleId) 
    {
        return titleAuthorRepository.findByTitleTitleId(titleId);
    }

    public List<TitleAuthor> getLeadAuthors() 
    {
        return titleAuthorRepository.findByAuOrd(1);
    }

    // Restored your highly readable Stream-based search
    public List<TitleAuthor> search(String auId, String titleId, Integer maxRoyalty, Integer minRoyalty) 
    {
        List<TitleAuthor> list = titleAuthorRepository.findAll();

        if (auId != null) 
        {
            list = list.stream()
                    .filter(t -> t.getAuthor().getAuId().equals(auId))
                    .collect(Collectors.toList());
        }

        if (titleId != null) 
        {
            list = list.stream()
                    .filter(t -> t.getTitle().getTitleId().equals(titleId))
                    .collect(Collectors.toList());
        }

        if (maxRoyalty != null) 
        {
            list = list.stream()
                    .filter(t -> t.getRoyaltyPer() != null && t.getRoyaltyPer() <= maxRoyalty)
                    .collect(Collectors.toList());
        }
        
        if (minRoyalty != null) 
        {
            list = list.stream()
                    .filter(t -> t.getRoyaltyPer() != null && t.getRoyaltyPer() >= minRoyalty)
                    .collect(Collectors.toList());
        }

        return list;
    }

    private void validateRoyaltyLimit(String titleId, Integer newRoyalty, TitleAuthorId currentId) 
    {
        if (newRoyalty == null) return;

        List<TitleAuthor> list = titleAuthorRepository.findByTitleTitleId(titleId);

        int currentTotal = list.stream()
                // Ignore the current mapping if we are updating it, so we don't double-count
                .filter(t -> currentId == null || 
                        !(t.getAuthor().getAuId().equals(currentId.getAuthor()) && t.getTitle().getTitleId().equals(currentId.getTitle())))
                .mapToInt(t -> t.getRoyaltyPer() != null ? t.getRoyaltyPer() : 0)
                .sum();

        if (currentTotal + newRoyalty > 100) 
        {
            throw new IllegalArgumentException("Validation Failed: Total royalty for this title cannot exceed 100%.");
        }
    }
}