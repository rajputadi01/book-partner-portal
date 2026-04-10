package com.capg.portal.creator.repository;

import com.capg.portal.creator.entity.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, String> 
{
	
}