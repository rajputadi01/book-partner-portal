package com.capg.portal.finance.service;

import com.capg.portal.finance.entity.RoyaltySchedule;
import com.capg.portal.finance.repository.RoyaltyScheduleRepository;
import com.capg.portal.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RoyaltyScheduleService 
{
    private final RoyaltyScheduleRepository royaltyScheduleRepository;

    public RoyaltyScheduleService(RoyaltyScheduleRepository royaltyScheduleRepository) 
    {
        this.royaltyScheduleRepository = royaltyScheduleRepository;
    }

    public RoyaltySchedule createRoyaltySchedule(RoyaltySchedule schedule) 
    {
        validateRange(schedule.getLorange(), schedule.getHirange());
        return royaltyScheduleRepository.save(schedule);
    }

    public List<RoyaltySchedule> getAllRoyaltySchedules() 
    {
        return royaltyScheduleRepository.findAll();
    }

    public RoyaltySchedule getRoyaltyScheduleById(Integer id) 
    {
        return royaltyScheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Royalty Schedule not found with ID: " + id));
    }

    public RoyaltySchedule updateRoyaltySchedule(Integer id, RoyaltySchedule details) 
    {
        RoyaltySchedule existing = getRoyaltyScheduleById(id);
        
        validateRange(details.getLorange(), details.getHirange());

        existing.setTitle(details.getTitle());
        existing.setLorange(details.getLorange());
        existing.setHirange(details.getHirange());
        existing.setRoyalty(details.getRoyalty());
        
        return royaltyScheduleRepository.save(existing);
    }

    public RoyaltySchedule patchRoyaltySchedule(Integer id, RoyaltySchedule updates) 
    {
        RoyaltySchedule existing = getRoyaltyScheduleById(id);

        // We must evaluate the new combined state before applying it
        Integer finalLorange = updates.getLorange() != null ? updates.getLorange() : existing.getLorange();
        Integer finalHirange = updates.getHirange() != null ? updates.getHirange() : existing.getHirange();
        
        validateRange(finalLorange, finalHirange);

        existing.setLorange(finalLorange);
        existing.setHirange(finalHirange);

        if (updates.getTitle() != null) existing.setTitle(updates.getTitle());
        if (updates.getRoyalty() != null) existing.setRoyalty(updates.getRoyalty());

        return royaltyScheduleRepository.save(existing);
    }
    
    public List<RoyaltySchedule> getRoyaltySchedulesByRange(Integer minLorange, Integer maxHirange) 
    {
        return royaltyScheduleRepository.findByLorangeGreaterThanEqualAndHirangeLessThanEqual(minLorange, maxHirange);
    }

    public List<RoyaltySchedule> getRoyaltySchedulesByTitleId(String titleId) 
    {
        return royaltyScheduleRepository.findByTitleTitleId(titleId);
    }

    private void validateRange(Integer lorange, Integer hirange) 
    {
        if (lorange != null && hirange != null && lorange >= hirange) 
        {
            throw new IllegalArgumentException("Validation Failed: Low range (" + lorange + ") must be strictly less than High range (" + hirange + ").");
        }
    }
}