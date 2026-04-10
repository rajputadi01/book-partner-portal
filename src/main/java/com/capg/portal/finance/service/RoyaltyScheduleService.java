package com.capg.portal.finance.service;

import com.capg.portal.finance.entity.RoyaltySchedule;
import com.capg.portal.finance.repository.RoyaltyScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoyaltyScheduleService {

	 private final RoyaltyScheduleRepository royaltyScheduleRepository;
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
}