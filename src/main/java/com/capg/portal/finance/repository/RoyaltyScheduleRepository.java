package com.capg.portal.finance.repository;

import com.capg.portal.finance.entity.RoyaltySchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RoyaltyScheduleRepository extends JpaRepository<RoyaltySchedule, Integer> 
{
    List<RoyaltySchedule> findByLorangeGreaterThanEqualAndHirangeLessThanEqual(Integer minLorange, Integer maxHirange);
    
    List<RoyaltySchedule> findByTitleTitleId(String titleId);
}