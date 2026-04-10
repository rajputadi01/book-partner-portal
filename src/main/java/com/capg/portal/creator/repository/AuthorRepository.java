package com.capg.portal.creator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capg.portal.creator.entity.Author;

@Repository
public interface AuthorRepository extends JpaRepository<Author, String> 
{
	
}