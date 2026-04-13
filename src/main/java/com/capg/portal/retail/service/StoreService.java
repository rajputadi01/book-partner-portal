package com.capg.portal.retail.service;

import com.capg.portal.retail.entity.Store;
import com.capg.portal.retail.repository.StoreRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreService {

    private final StoreRepository storeRepository;

    // Constructor Injection (Replaces Lombok's @RequiredArgsConstructor)
    public StoreService(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    public Store createStore(Store store) {
        return storeRepository.save(store);
    }

    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }

    public Store getStoreById(String id) {
        return storeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Store not found with ID: " + id));
    }

    public Store updateStore(String id, Store storeDetails) {
        Store existingStore = getStoreById(id);
        existingStore.setStorName(storeDetails.getStorName());
        existingStore.setStorAddress(storeDetails.getStorAddress());
        existingStore.setCity(storeDetails.getCity());
        existingStore.setState(storeDetails.getState());
        existingStore.setZip(storeDetails.getZip());
        return storeRepository.save(existingStore);
    }
    
    // Filter by City
    public List<Store> getStoresByCity(String city) {
        return storeRepository.findByCityIgnoreCase(city);
    }

    // New Method: Filter by State
    public List<Store> getStoresByState(String state) {
        return storeRepository.findByStateIgnoreCase(state);
    }
}