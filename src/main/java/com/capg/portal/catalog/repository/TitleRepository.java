package com.capg.portal.catalog.repository;

import com.capg.portal.catalog.entity.Title;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TitleRepository extends JpaRepository<Title, String> 
{
    List<Title> findByPriceLessThan(Double maxPrice);

    List<Title> findByTypeIgnoreCase(String type);

    List<Title> findByPublisherPubId(String pubId);

    List<Title> findByPubdateBefore(LocalDateTime date);
}