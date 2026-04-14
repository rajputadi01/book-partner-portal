package com.capg.portal.catalog.service;

import com.capg.portal.catalog.entity.Title;
import com.capg.portal.catalog.entity.TitleAuthor;
import com.capg.portal.catalog.repository.TitleAuthorRepository;
import com.capg.portal.catalog.repository.TitleRepository;
import com.capg.portal.creator.entity.Author;
import com.capg.portal.creator.entity.Publisher;
import com.capg.portal.exception.ResourceAlreadyExistsException;
import com.capg.portal.exception.ResourceNotFoundException;
import com.capg.portal.finance.entity.RoyaltySchedule;
import com.capg.portal.finance.repository.RoyaltyScheduleRepository;
import com.capg.portal.retail.entity.Sales;
import com.capg.portal.retail.entity.Store;
import com.capg.portal.retail.repository.SalesRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class TitleService 
{
    private final TitleRepository titleRepository;
    private final SalesRepository salesRepository;
    private final RoyaltyScheduleRepository royaltyScheduleRepository;
    private final TitleAuthorRepository titleAuthorRepository;
    
    public TitleService(TitleRepository titleRepository, SalesRepository salesRepository, RoyaltyScheduleRepository royaltyScheduleRepository, TitleAuthorRepository titleAuthorRepository) 
    {
        this.titleRepository = titleRepository;
        this.salesRepository = salesRepository;
        this.royaltyScheduleRepository = royaltyScheduleRepository;
        this.titleAuthorRepository = titleAuthorRepository;
    }

    public Title createTitle(Title title) 
    {
        if (titleRepository.existsById(title.getTitleId())) 
        {
            throw new ResourceAlreadyExistsException("Title with ID " + title.getTitleId() + " already exists.");
        }
        return titleRepository.save(title);
    }

    public List<Title> getAllTitles() 
    {
        return titleRepository.findAll();
    }

    public Title getTitleById(String id) 
    {
        return titleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Title not found with ID: " + id));
    }

    public Title updateTitle(String id, Title titleDetails) 
    {
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

    public Title patchTitle(String id, Title updates) 
    {
        Title existingTitle = getTitleById(id);

        if (updates.getTitleName() != null) 
        {
            existingTitle.setTitleName(updates.getTitleName());
        }
        if (updates.getType() != null) 
        {
            existingTitle.setType(updates.getType());
        }
        if (updates.getPublisher() != null) 
        {
            existingTitle.setPublisher(updates.getPublisher());
        }
        if (updates.getPrice() != null) 
        {
            existingTitle.setPrice(updates.getPrice());
        }
        if (updates.getAdvance() != null) 
        {
            existingTitle.setAdvance(updates.getAdvance());
        }
        if (updates.getRoyalty() != null) 
        {
            existingTitle.setRoyalty(updates.getRoyalty());
        }
        if (updates.getYtdSales() != null) 
        {
            existingTitle.setYtdSales(updates.getYtdSales());
        }
        if (updates.getNotes() != null) 
        {
            existingTitle.setNotes(updates.getNotes());
        }
        if (updates.getPubdate() != null) 
        {
            existingTitle.setPubdate(updates.getPubdate());
        }

        return titleRepository.save(existingTitle);
    }
    
    public List<Title> getTitlesByPriceLessThan(Double maxPrice) 
    {
        return titleRepository.findByPriceLessThan(maxPrice);
    }

    public List<Title> getTitlesByType(String type) 
    {
        return titleRepository.findByTypeIgnoreCase(type);
    }

    public List<Title> getTitlesByPublisher(String pubId) 
    {
        return titleRepository.findByPublisherPubId(pubId);
    }

    public List<Title> getTitlesPublishedBefore(LocalDateTime date) 
    {
        return titleRepository.findByPubdateBefore(date);
    }
    
    public Publisher getPublisherByTitleId(String titleId) 
    {
        Title title = titleRepository.findById(titleId).orElseThrow(() -> new ResourceNotFoundException("Title not found with ID: " + titleId));
        if (title.getPublisher() == null) 
        {
            throw new ResourceNotFoundException("No publisher assigned to this title.");
        }
        return title.getPublisher();
    }

    public List<Sales> getSalesByTitleId(String titleId) 
    {
        titleRepository.findById(titleId).orElseThrow(() -> new ResourceNotFoundException("Title not found"));   
        return salesRepository.findByTitleTitleId(titleId);
    }

    public List<RoyaltySchedule> getRoyaltiesByTitleId(String titleId) 
    {
        titleRepository.findById(titleId).orElseThrow(() -> new ResourceNotFoundException("Title not found"));
        return royaltyScheduleRepository.findByTitleTitleId(titleId);
    }

    public List<TitleAuthor> getTitleAuthorsByTitleId(String titleId) 
    {
        titleRepository.findById(titleId).orElseThrow(() -> new ResourceNotFoundException("Title not found"));
        return titleAuthorRepository.findByTitleTitleId(titleId);
    }
    
    public List<Author> getAuthorsByTitleId(String titleId) 
    {
        titleRepository.findById(titleId).orElseThrow(() -> new ResourceNotFoundException("Title not found with ID: " + titleId));
        return titleAuthorRepository.findByTitleTitleId(titleId).stream()
                .map(TitleAuthor::getAuthor)
                .collect(Collectors.toList());
    }

    public List<Store> getStoresByTitleId(String titleId) 
    {
        titleRepository.findById(titleId).orElseThrow(() -> new ResourceNotFoundException("Title not found with ID: " + titleId));
        return salesRepository.findByTitleTitleId(titleId).stream()
                .map(Sales::getStore)
                .distinct()
                .collect(Collectors.toList());
    }
}