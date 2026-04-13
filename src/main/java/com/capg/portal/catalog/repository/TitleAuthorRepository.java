package com.capg.portal.catalog.repository;

import com.capg.portal.catalog.entity.TitleAuthor;
import com.capg.portal.catalog.entity.TitleAuthorId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TitleAuthorRepository extends JpaRepository<TitleAuthor, TitleAuthorId> {
    
    // Existing Method
    List<TitleAuthor> findByRoyaltyPerLessThan(Integer royaltyPer);

    // New Method: Find by Author ID
    List<TitleAuthor> findByAuthorAuId(String auId);

    // New Method: Find by Title ID
    List<TitleAuthor> findByTitleTitleId(String titleId);
}