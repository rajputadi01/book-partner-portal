package com.capg.portal.catalog.entity;

import com.capg.portal.creator.entity.Publisher;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
    @NotBlank(message = "Title ID is required")
    @Size(max = 10, message = "Title ID cannot exceed 10 characters")
    @Column(name = "title_id", length = 10)
    private String titleId;

    @NotBlank(message = "Title name is required")
    @Size(max = 80, message = "Title name cannot exceed 80 characters")
    @Column(name = "title", nullable = false, length = 80)
    private String titleName; // 'titleName' to avoid clashing with class name

    @Size(max = 12, message = "Type cannot exceed 12 characters")
    @Column(name = "type", columnDefinition = "char(12)")
    private String type = "UNDECIDED";

    // Foreign Key mapping to Publishers table (Allowed to be NULL per schema)
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

    @Size(max = 200, message = "Notes cannot exceed 200 characters")
    @Column(name = "notes", length = 200)
    private String notes;

    @Column(name = "pubdate")
    private LocalDateTime pubdate;
}