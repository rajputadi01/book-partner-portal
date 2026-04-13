package com.capg.portal.catalog.repository;

import com.capg.portal.catalog.entity.TitleAuthor;
import com.capg.portal.catalog.entity.TitleAuthorId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TitleAuthorRepository extends JpaRepository<TitleAuthor, TitleAuthorId> {
    
    List<TitleAuthor> findByRoyaltyPerLessThan(Integer royaltyPer);
    List<TitleAuthor> findByRoyaltyPerGreaterThan(Integer royaltyPer);

    List<TitleAuthor> findByAuthorAuId(String auId);

    List<TitleAuthor> findByTitleTitleId(String titleId);
}