package com.capg.portal.finance.service;

import java.util.List;

import org.springframework.stereotype.Service;
import com.capg.portal.finance.entity.Discount;
import com.capg.portal.finance.repository.DiscountRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DiscountService {
	private DiscountRepository discountRepository;
	
	public Discount createDiscount(Discount discount) {
		return discountRepository.save(discount);
	}
	
	public List<Discount> getAllDiscounts(){
		return discountRepository.findAll();
	}
	
	public Discount getDiscountByType(String type) {
		return discountRepository.findById(type)
				.orElseThrow(()-> new RuntimeException("Discount not found with type: " + type));
	}
	
	public Discount updateDiscount(String type, Discount discountdetails) {
		Discount existing=getDiscountByType(type);
		existing.setStore(discountdetails.getStore());
		existing.setLowQty(discountdetails.getLowQty());
		existing.setHighQty(discountdetails.getHighQty());
		existing.setDiscountAmount(discountdetails.getDiscountAmount());
		return discountRepository.save(existing);
	}
}
