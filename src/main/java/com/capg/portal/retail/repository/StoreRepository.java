package com.capg.portal.retail.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capg.portal.retail.entity.Store;

@Repository
public interface StoreRepository extends JpaRepository<Store, String> 
{
	
}