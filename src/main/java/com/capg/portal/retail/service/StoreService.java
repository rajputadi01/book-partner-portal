package com.capg.portal.retail.service;

import com.capg.portal.retail.entity.Store;
import com.capg.portal.retail.entity.Sales;
import com.capg.portal.catalog.entity.Title;
import com.capg.portal.creator.entity.Publisher;
import com.capg.portal.creator.entity.Author;
import com.capg.portal.catalog.entity.TitleAuthor;
import com.capg.portal.catalog.repository.TitleAuthorRepository;
import com.capg.portal.retail.repository.StoreRepository;
import com.capg.portal.retail.repository.SalesRepository;
import com.capg.portal.exception.ResourceAlreadyExistsException;
import com.capg.portal.exception.ResourceNotFoundException;
import com.capg.portal.finance.entity.Discount;
import com.capg.portal.finance.repository.DiscountRepository;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StoreService 
{
    private final StoreRepository storeRepository;
    private final SalesRepository salesRepository;
    private final TitleAuthorRepository titleAuthorRepository;
    private final DiscountRepository discountRepository;

    public StoreService(StoreRepository storeRepository, SalesRepository salesRepository, 
    					TitleAuthorRepository titleAuthorRepository, DiscountRepository discountRepository) 
    {
        this.storeRepository = storeRepository;
        this.salesRepository = salesRepository;
        this.titleAuthorRepository = titleAuthorRepository;
        this.discountRepository = discountRepository;
    }

    public Store createStore(Store store) 
    {
        if (storeRepository.existsById(store.getStorId())) 
        {
            throw new ResourceAlreadyExistsException("Store with ID " + store.getStorId() + " already exists.");
        }
        processDefaults(store);
        return storeRepository.save(store);
    }

    public List<Store> getAllStores() 
    {
        return storeRepository.findAll();
    }

    public Store getStoreById(String id) 
    {
        return storeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Store not found with ID: " + id));
    }

    public Store updateStore(String id, Store storeDetails) 
    {
        Store existingStore = getStoreById(id);
        existingStore.setStorName(storeDetails.getStorName());
        existingStore.setStorAddress(storeDetails.getStorAddress());
        existingStore.setCity(storeDetails.getCity());
        existingStore.setState(storeDetails.getState());
        existingStore.setZip(storeDetails.getZip());
        
        processDefaults(existingStore);
        return storeRepository.save(existingStore);
    }

    public Store patchStore(String id, Store updates) 
    {
        Store existingStore = getStoreById(id);

        if (updates.getStorName() != null) existingStore.setStorName(updates.getStorName());
        if (updates.getStorAddress() != null) existingStore.setStorAddress(updates.getStorAddress());
        if (updates.getCity() != null) existingStore.setCity(updates.getCity());
        if (updates.getState() != null) existingStore.setState(updates.getState());
        if (updates.getZip() != null) existingStore.setZip(updates.getZip());

        processDefaults(existingStore);
        return storeRepository.save(existingStore);
    }

    public List<Store> getStoresByCity(String city) 
    {
        return storeRepository.findByCityIgnoreCase(city);
    }

    public List<Store> getStoresByState(String state) 
    {
        return storeRepository.findByStateIgnoreCase(state);
    }

    public List<Sales> getSalesByStoreId(String storId) 
    {
        getStoreById(storId);
        return salesRepository.findByStoreStorId(storId);
    }

    public List<Title> getTitlesByStoreId(String storId) 
    {
        getStoreById(storId);
        return salesRepository.findByStoreStorId(storId).stream()
                .map(Sales::getTitle)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Publisher> getPublishersByStoreId(String storId) 
    {
        getStoreById(storId);
        return salesRepository.findByStoreStorId(storId).stream()
                .map(sale -> sale.getTitle().getPublisher())
                .filter(p -> p != null)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Author> getAuthorsByStoreId(String storId) 
    {
        getStoreById(storId);
        
        return salesRepository.findByStoreStorId(storId).stream()
                .map(Sales::getTitle)
                .flatMap(title -> titleAuthorRepository.findByTitleTitleId(title.getTitleId()).stream())
                .map(TitleAuthor::getAuthor)
                .distinct()
                .collect(Collectors.toList());
    }

    private void processDefaults(Store store) 
    {
        if (store.getState() != null && store.getState().trim().isEmpty()) store.setState(null);
        if (store.getZip() != null && store.getZip().trim().isEmpty()) store.setZip(null);
    }
    
    public List<Discount> getDiscountsByStoreId(String storId) 
    {
        getStoreById(storId);
        return discountRepository.findByStoreStorId(storId);
    }
}