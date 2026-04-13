package com.capg.portal.creator.repository;

import com.capg.portal.creator.entity.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, String> {
    
    List<Publisher> findByCityIgnoreCase(String city);
    
    List<Publisher> findByStateIgnoreCase(String state);
    
    List<Publisher> findByCountryIgnoreCase(String country);
}