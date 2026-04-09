package com.capg.portal.catalog.entity;

import com.capg.portal.creator.entity.Publisher;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "titles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Title {

    @Id
    @Column(name = "title_id", length = 6)
    private String titleId;

    @Column(name = "title", nullable = false, length = 80)
    private String titleName; // 'titleName' to avoid clashing with class name

    @Column(name = "type", columnDefinition = "char(12)")
    private String type;

    // Foreign Key mapping to Publishers table
    @ManyToOne
    @JoinColumn(name = "pub_id")
    private Publisher publisher;

    @Column(name = "price")
    private Double price;

    @Column(name = "advance")
    private Double advance;

    @Column(name = "royalty")
    private Integer royalty;

    @Column(name = "ytd_sales")
    private Integer ytdSales;

    @Column(name = "notes", length = 200)
    private String notes;

    @Column(name = "pubdate")
    private LocalDateTime pubdate;
}