package com.capg.portal.finance.service;

import com.capg.portal.finance.entity.RoyaltySchedule;
import com.capg.portal.finance.repository.RoyaltyScheduleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoyaltyScheduleService {

    private final RoyaltyScheduleRepository royaltyScheduleRepository;

    // Constructor Injection
    public RoyaltyScheduleService(RoyaltyScheduleRepository royaltyScheduleRepository) {
        this.royaltyScheduleRepository = royaltyScheduleRepository;
    }

    public RoyaltySchedule createRoyaltySchedule(RoyaltySchedule schedule) {
        return royaltyScheduleRepository.save(schedule);
    }

    public List<RoyaltySchedule> getAllRoyaltySchedules() {
        return royaltyScheduleRepository.findAll();
    }

    public RoyaltySchedule getRoyaltyScheduleById(Integer id) {
        return royaltyScheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Royalty Schedule not found"));
    }

    public RoyaltySchedule updateRoyaltySchedule(Integer id, RoyaltySchedule details) {
        RoyaltySchedule existing = getRoyaltyScheduleById(id);
        
        existing.setTitle(details.getTitle());
        existing.setLorange(details.getLorange());
        existing.setHirange(details.getHirange());
        existing.setRoyalty(details.getRoyalty());
        
        return royaltyScheduleRepository.save(existing);
    }
    
    public List<RoyaltySchedule> getRoyaltySchedulesByRange(Integer minLorange, Integer maxHirange) {
        return royaltyScheduleRepository.findByLorangeGreaterThanEqualAndHirangeLessThanEqual(minLorange, maxHirange);
    }

    // New Method: Get schedules by Title ID
    public List<RoyaltySchedule> getRoyaltySchedulesByTitleId(String titleId) {
        return royaltyScheduleRepository.findByTitleTitleId(titleId);
    }
}