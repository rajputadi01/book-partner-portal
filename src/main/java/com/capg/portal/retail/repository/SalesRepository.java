package com.capg.portal.retail.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capg.portal.retail.entity.Sales;
import com.capg.portal.retail.entity.SalesId;

@Repository
public interface SalesRepository extends JpaRepository <Sales, SalesId> {
	

}
