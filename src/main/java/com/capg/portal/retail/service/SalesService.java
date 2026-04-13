package com.capg.portal.retail.service;

import com.capg.portal.retail.entity.Sales;
import com.capg.portal.retail.entity.SalesId;
import com.capg.portal.retail.repository.SalesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalesService {

    private final SalesRepository salesRepository;

    // Constructor Injection
    public SalesService(SalesRepository salesRepository) {
        this.salesRepository = salesRepository;
    }

    public Sales createSale(Sales sale) {
        return salesRepository.save(sale);
    }

    public List<Sales> getAllSales() {
        return salesRepository.findAll();
    }

    public Sales getSaleById(SalesId id) {
        return salesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sale transaction not found"));
    }

    public Sales updateSale(SalesId id, Sales details) {
        Sales existing = getSaleById(id);
        existing.setOrdDate(details.getOrdDate());
        existing.setQty(details.getQty());
        existing.setPayterms(details.getPayterms());
        return salesRepository.save(existing);
    }
    
    // Filter by Store ID
    public List<Sales> getSalesByStoreId(String storId) {
        return salesRepository.findByStoreStorId(storId);
    }

    // Filter by Title ID
    public List<Sales> getSalesByTitleId(String titleId) {
        return salesRepository.findByTitleTitleId(titleId);
    }
}