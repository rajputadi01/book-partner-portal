package com.capg.portal.catalog.entity;

import com.capg.portal.creator.entity.Author;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "titleauthor")
@IdClass(TitleAuthorId.class)
public class TitleAuthor {

    @Id
    @NotNull(message = "Author selection is required")
    @ManyToOne
    @JoinColumn(name = "au_id")
    private Author author;

    @Id
    @NotNull(message = "Title selection is required")
    @ManyToOne
    @JoinColumn(name = "title_id")
    private Title title;

    @Column(name = "au_ord", columnDefinition = "tinyint")
    private Integer auOrd;

    @Column(name = "royaltyper")
    private Integer royaltyPer;

    // Default Constructor
    public TitleAuthor() {
    }

    // Parameterized Constructor
    public TitleAuthor(Author author, Title title, Integer auOrd, Integer royaltyPer) {
        this.author = author;
        this.title = title;
        this.auOrd = auOrd;
        this.royaltyPer = royaltyPer;
    }

    // Getters and Setters
    public Author getAuthor() { return author; }
    public void setAuthor(Author author) { this.author = author; }

    public Title getTitle() { return title; }
    public void setTitle(Title title) { this.title = title; }

    public Integer getAuOrd() { return auOrd; }
    public void setAuOrd(Integer auOrd) { this.auOrd = auOrd; }

    public Integer getRoyaltyPer() { return royaltyPer; }
    public void setRoyaltyPer(Integer royaltyPer) { this.royaltyPer = royaltyPer; }
}