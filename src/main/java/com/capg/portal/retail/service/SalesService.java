package com.capg.portal.retail.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.capg.portal.retail.entity.Sales;
import com.capg.portal.retail.entity.SalesId;
import com.capg.portal.retail.repository.SalesRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SalesService {
	private final SalesRepository salesRepository;
	
	
	public Sales createSales(Sales sale) {
		return salesRepository.save(sale);
	}
	
	public List<Sales> getAllSales(){
		return salesRepository.findAll();
	}
	
	public Sales getSalesById(SalesId id) {
		return salesRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Sale transaction not found"));
	}
	
	public Sales updateSale(SalesId id, Sales details ) {
		Sales existing = getSalesById(id);
		existing.setOrdDate(details.getOrdDate());
		existing.setQty(details.getQty());
		existing.setPayterms(details.getPayterms());
		
		return salesRepository.save(existing);
	}
	

}
