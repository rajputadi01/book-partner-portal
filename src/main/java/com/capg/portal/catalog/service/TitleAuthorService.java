package com.capg.portal.catalog.service;

import java.util.List;

import org.springframework.stereotype.Service;
import com.capg.portal.catalog.entity.TitleAuthor;
import com.capg.portal.catalog.entity.TitleAuthorId;
import com.capg.portal.catalog.repository.TitleAuthorRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TitleAuthorService {
	
	private final TitleAuthorRepository titleAuthorRepository;
	
	public TitleAuthor createTitleAuthor(TitleAuthor titleAuthor) {
		return titleAuthorRepository.save(titleAuthor);
	}
	
	public List<TitleAuthor> getAllTitleAuthors(){
		return titleAuthorRepository.findAll();
	}
	
	public TitleAuthor getTitleAuthorById(TitleAuthorId id) {
		return titleAuthorRepository.findById(id)
				.orElseThrow(()-> new RuntimeException("TitleAuthor mapping not found"));
	}
	
	public TitleAuthor updateTitleAuthor(TitleAuthorId id, TitleAuthor details) {
		TitleAuthor existing=getTitleAuthorById(id);
		existing.setAuOrd(details.getAuOrd());
		existing.setRoyaltyPer(details.getRoyaltyPer());
		return titleAuthorRepository.save(existing);
	}
}
