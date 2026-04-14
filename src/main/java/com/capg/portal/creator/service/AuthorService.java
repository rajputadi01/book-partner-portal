package com.capg.portal.creator.service;

import com.capg.portal.creator.entity.Author;
import com.capg.portal.creator.entity.Publisher;
import com.capg.portal.catalog.entity.Title;
import com.capg.portal.catalog.entity.TitleAuthor;
import com.capg.portal.retail.entity.Sales;
import com.capg.portal.retail.entity.Store;
import com.capg.portal.retail.repository.SalesRepository;
import com.capg.portal.creator.repository.AuthorRepository;
import com.capg.portal.catalog.repository.TitleAuthorRepository;
import com.capg.portal.exception.ResourceAlreadyExistsException;
import com.capg.portal.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorService 
{
    private final AuthorRepository authorRepository;
    private final TitleAuthorRepository titleAuthorRepository;
    private final SalesRepository salesRepository; // Add this

    public AuthorService(AuthorRepository authorRepository, TitleAuthorRepository titleAuthorRepository, SalesRepository salesRepository)
    {
        this.authorRepository = authorRepository;
        this.titleAuthorRepository = titleAuthorRepository;
        this.salesRepository = salesRepository;
    }

    public Author createAuthor(Author author) 
    {
        if (authorRepository.existsById(author.getAuId())) 
        {
            throw new ResourceAlreadyExistsException("Author with ID " + author.getAuId() + " already exists.");
        }
        
        processDefaults(author);
        return authorRepository.save(author);
    }

    public List<Author> getAllAuthors() 
    {
        return authorRepository.findAll();
    }

    public Author getAuthorById(String id) 
    {
        return authorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Author not found with ID: " + id));
    }

    public Author updateAuthor(String id, Author details) 
    {
        Author existing = getAuthorById(id);
        
        existing.setAuLname(details.getAuLname());
        existing.setAuFname(details.getAuFname());
        existing.setPhone(details.getPhone());
        existing.setAddress(details.getAddress());
        existing.setCity(details.getCity());
        existing.setState(details.getState());
        existing.setZip(details.getZip());
        existing.setContract(details.getContract());
        
        processDefaults(existing);
        return authorRepository.save(existing);
    }

    public Author patchAuthor(String id, Author updates) 
    {
        Author existing = getAuthorById(id);

        if (updates.getAuLname() != null) existing.setAuLname(updates.getAuLname());
        if (updates.getAuFname() != null) existing.setAuFname(updates.getAuFname());
        if (updates.getPhone() != null) existing.setPhone(updates.getPhone());
        if (updates.getAddress() != null) existing.setAddress(updates.getAddress());
        if (updates.getCity() != null) existing.setCity(updates.getCity());
        if (updates.getState() != null) existing.setState(updates.getState());
        if (updates.getZip() != null) existing.setZip(updates.getZip());
        if (updates.getContract() != null) existing.setContract(updates.getContract());

        processDefaults(existing);
        return authorRepository.save(existing);
    }

    public List<Author> getAuthorsByContractStatus(Integer contract) 
    {
        return authorRepository.findByContract(contract);
    }

    public List<Author> getAuthorsByCity(String city) 
    {
        return authorRepository.findByCityIgnoreCase(city);
    }

    public List<Author> getAuthorsByState(String state) 
    {
        return authorRepository.findByStateIgnoreCase(state);
    }

    public List<TitleAuthor> getTitleAuthorsByAuthorId(String auId) 
    {
        getAuthorById(auId);
        return titleAuthorRepository.findByAuthorAuId(auId);
    }

    public List<Title> getTitlesByAuthorId(String auId) 
    {
        getAuthorById(auId);
        return titleAuthorRepository.findByAuthorAuId(auId).stream()
                .map(TitleAuthor::getTitle)
                .collect(Collectors.toList());
    }

    public List<Publisher> getPublishersByAuthorId(String auId) 
    {
        getAuthorById(auId);
        return titleAuthorRepository.findByAuthorAuId(auId).stream()
                .map(ta -> ta.getTitle().getPublisher())
                .filter(p -> p != null)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Store> getStoresByAuthorId(String auId) 
    {
        getAuthorById(auId);
        return titleAuthorRepository.findByAuthorAuId(auId).stream()
                .map(TitleAuthor::getTitle)
                .<Sales>flatMap(title -> salesRepository.findByTitleTitleId(title.getTitleId()).stream())
                .map(Sales::getStore) 
                .distinct() // Remove duplicates
                .collect(Collectors.toList());
    }

    private void processDefaults(Author author) 
    {
        if (author.getPhone() == null || author.getPhone().trim().isEmpty()) 
        {
            author.setPhone("UNKNOWN");
        }
        if (author.getZip() != null && author.getZip().trim().isEmpty()) 
        {
            author.setZip(null);
        }
        if (author.getState() != null && author.getState().trim().isEmpty()) 
        {
            author.setState(null);
        }
    }
}