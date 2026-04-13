package com.capg.portal.creator.service;

import com.capg.portal.creator.entity.Author;
import com.capg.portal.creator.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Author createAuthor(Author author) {
        return authorRepository.save(author);
    }

    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    public Author getAuthorById(String id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Author not found with ID: " + id));
    }

    public Author updateAuthor(String id, Author details) {
        Author existing = getAuthorById(id);
        existing.setAuLname(details.getAuLname());
        existing.setAuFname(details.getAuFname());
        existing.setPhone(details.getPhone());
        existing.setAddress(details.getAddress());
        existing.setCity(details.getCity());
        existing.setState(details.getState());
        existing.setZip(details.getZip());
        existing.setContract(details.getContract());
        return authorRepository.save(existing);
    }

    public List<Author> getAuthorsByContractStatus(Integer contract) {
        return authorRepository.findByContract(contract);
    }

    public List<Author> getAuthorsByCity(String city) {
        return authorRepository.findByCityIgnoreCase(city);
    }

    public List<Author> getAuthorsByState(String state) {
        return authorRepository.findByStateIgnoreCase(state);
    }
}