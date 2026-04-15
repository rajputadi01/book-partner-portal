package com.capg.portal.retail.service;

import com.capg.portal.retail.entity.Sales;
import com.capg.portal.retail.entity.SalesId;
import com.capg.portal.retail.repository.SalesRepository;
import com.capg.portal.exception.ResourceAlreadyExistsException;
import com.capg.portal.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SalesService 
{
    private final SalesRepository salesRepository;

    public SalesService(SalesRepository salesRepository) 
    {
        this.salesRepository = salesRepository;
    }

    public Sales createSale(Sales sale) 
    {
        SalesId id = new SalesId(sale.getStore().getStorId(), sale.getOrdNum(), sale.getTitle().getTitleId());
        
        if (salesRepository.existsById(id)) 
        {
            throw new ResourceAlreadyExistsException("This Order Number already exists for this Store and Title.");
        }
        
        processDefaults(sale);
        return salesRepository.save(sale);
    }

    public List<Sales> getAllSales() 
    {
        return salesRepository.findAll();
    }

    public Sales getSaleById(SalesId id) 
    {
        return salesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale transaction not found."));
    }

    public Sales updateSale(SalesId id, Sales details) 
    {
        Sales existing = getSaleById(id);
        
        existing.setOrdDate(details.getOrdDate());
        existing.setQty(details.getQty());
        existing.setPayterms(details.getPayterms());
        
        processDefaults(existing);
        return salesRepository.save(existing);
    }

    public Sales patchSale(SalesId id, Sales updates) 
    {
        Sales existing = getSaleById(id);

        if (updates.getOrdDate() != null) existing.setOrdDate(updates.getOrdDate());
        if (updates.getQty() != null) existing.setQty(updates.getQty());
        if (updates.getPayterms() != null) existing.setPayterms(updates.getPayterms());

        processDefaults(existing);
        return salesRepository.save(existing);
    }

    public List<Sales> getSalesByStoreId(String storId) 
    {
        return salesRepository.findByStoreStorId(storId);
    }

    public List<Sales> getSalesByTitleId(String titleId) 
    {
        return salesRepository.findByTitleTitleId(titleId);
    }

    public List<Sales> getSalesByPayterms(String payterms) 
    {
        return salesRepository.findByPaytermsIgnoreCase(payterms);
    }

    // --- Aggregations ---

    public int getTotalQtyByStore(String storId) 
    {
        return salesRepository.findByStoreStorId(storId)
                .stream()
                .mapToInt(Sales::getQty)
                .sum();
    }
    
    public long getTransactionCountByStore(String storId) 
    {
        return salesRepository.findByStoreStorId(storId).size();
    }
    
    public int getTotalQtyByTitle(String titleId) 
    {
        return salesRepository.findByTitleTitleId(titleId)
                .stream()
                .mapToInt(Sales::getQty)
                .sum();
    }

    private void processDefaults(Sales sale) 
    {
        if (sale.getOrdDate() == null) 
        {
            sale.setOrdDate(LocalDateTime.now());
        }
    }
}
