package com.capg.portal.creator.service;

import com.capg.portal.creator.entity.Author;
import com.capg.portal.creator.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

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

    public Author updateAuthor(String id, Author authorDetails) {
        Author existingAuthor = getAuthorById(id);
        existingAuthor.setAuLname(authorDetails.getAuLname());
        existingAuthor.setAuFname(authorDetails.getAuFname());
        existingAuthor.setPhone(authorDetails.getPhone());
        existingAuthor.setAddress(authorDetails.getAddress());
        existingAuthor.setCity(authorDetails.getCity());
        existingAuthor.setState(authorDetails.getState());
        existingAuthor.setZip(authorDetails.getZip());
        existingAuthor.setContract(authorDetails.getContract());
        return authorRepository.save(existingAuthor);
    }
}