package com.capg.portal.catalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capg.portal.catalog.entity.TitleAuthor;
import com.capg.portal.catalog.entity.TitleAuthorId;

@Repository
public interface TitleAuthorRepository extends JpaRepository<TitleAuthor,TitleAuthorId>{

}
