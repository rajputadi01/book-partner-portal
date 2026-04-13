package com.capg.portal.retail.repository;

import com.capg.portal.retail.entity.Sales;
import com.capg.portal.retail.entity.SalesId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesRepository extends JpaRepository<Sales, SalesId> {
    
    // Method to filter by Store ID
    List<Sales> findByStoreStorId(String storId);
    
    // New Method to filter by Title ID
    List<Sales> findByTitleTitleId(String titleId);
}