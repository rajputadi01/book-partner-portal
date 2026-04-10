package com.capg.portal.catalog.repository;

import com.capg.portal.catalog.entity.Title;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TitleRepository extends JpaRepository<Title, String> 
{
	
}