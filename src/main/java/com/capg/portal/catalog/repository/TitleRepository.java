package com.capg.portal.catalog.repository;

import com.capg.portal.catalog.entity.Title;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TitleRepository extends JpaRepository<Title, String> {
    
    // Existing filter
    List<Title> findByPriceLessThan(Double maxPrice);

    // New Method: Filter by Type
    List<Title> findByTypeIgnoreCase(String type);

    // New Method: Filter by Publisher ID
    List<Title> findByPublisherPubId(String pubId);

    // New Method: Filter by Publish Date before a specific date
    List<Title> findByPubdateBefore(LocalDateTime date);
}