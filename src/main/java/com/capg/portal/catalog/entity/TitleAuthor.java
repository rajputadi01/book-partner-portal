package com.capg.portal.catalog.entity;

import com.capg.portal.creator.entity.Author;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "titleauthor")
@IdClass(TitleAuthorId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TitleAuthor {

    @Id
    @ManyToOne
    @JoinColumn(name = "au_id")
    private Author author;

    @Id
    @ManyToOne
    @JoinColumn(name = "title_id")
    private Title title;

    @Column(name = "au_ord", columnDefinition = "tinyint")
    private Integer auOrd;

    @Column(name = "royaltyper")
    private Integer royaltyPer;
}