package com.capg.portal.retail.repository;

import com.capg.portal.retail.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store, String> {
    
    List<Store> findByCityIgnoreCase(String city);
    
    // New Method: Filter by State
    List<Store> findByStateIgnoreCase(String state);
}