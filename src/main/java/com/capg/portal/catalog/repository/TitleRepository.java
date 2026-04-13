package com.capg.portal.catalog.repository;

import com.capg.portal.catalog.entity.Title;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TitleRepository extends JpaRepository<Title, String> 
{
	List<Title> findByPriceLessThan(Double maxPrice);
}