package com.capg.portal.creator.repository;

import com.capg.portal.creator.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, String> {
    
    List<Author> findByContract(Integer contract);
    
    List<Author> findByCityIgnoreCase(String city);
    
    List<Author> findByStateIgnoreCase(String state);
}