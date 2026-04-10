package com.capg.portal.catalog.service;

import com.capg.portal.catalog.entity.Title;
import com.capg.portal.catalog.repository.TitleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TitleService 
{
    private final TitleRepository titleRepository;
    
    public Title createTitle(Title title) 
    {
        return titleRepository.save(title);
    }

    public List<Title> getAllTitles() 
    {
        return titleRepository.findAll();
    }

    public Title getTitleById(String id) 
    {
        return titleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Title not found with ID: " + id));
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
}