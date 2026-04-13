package com.capg.portal.catalog.entity;

import com.capg.portal.creator.entity.Publisher;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "titles")
public class Title {

    @Id
    @NotBlank(message = "Title ID is required")
    @Size(max = 10, message = "Title ID cannot exceed 10 characters")
    @Column(name = "title_id", length = 10)
    private String titleId;

    @NotBlank(message = "Title name is required")
    @Size(max = 80, message = "Title name cannot exceed 80 characters")
    @Column(name = "title", nullable = false, length = 80)
    private String titleName;

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

    // Default Constructor
    public Title() {
    }

    // Parameterized Constructor
    public Title(String titleId, String titleName, String type, Publisher publisher, Double price, Double advance, Integer royalty, Integer ytdSales, String notes, LocalDateTime pubdate) {
        this.titleId = titleId;
        this.titleName = titleName;
        this.type = type;
        this.publisher = publisher;
        this.price = price;
        this.advance = advance;
        this.royalty = royalty;
        this.ytdSales = ytdSales;
        this.notes = notes;
        this.pubdate = pubdate;
    }

    // Getters and Setters
    public String getTitleId() { return titleId; }
    public void setTitleId(String titleId) { this.titleId = titleId; }

    public String getTitleName() { return titleName; }
    public void setTitleName(String titleName) { this.titleName = titleName; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Publisher getPublisher() { return publisher; }
    public void setPublisher(Publisher publisher) { this.publisher = publisher; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Double getAdvance() { return advance; }
    public void setAdvance(Double advance) { this.advance = advance; }

    public Integer getRoyalty() { return royalty; }
    public void setRoyalty(Integer royalty) { this.royalty = royalty; }

    public Integer getYtdSales() { return ytdSales; }
    public void setYtdSales(Integer ytdSales) { this.ytdSales = ytdSales; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getPubdate() { return pubdate; }
    public void setPubdate(LocalDateTime pubdate) { this.pubdate = pubdate; }
}