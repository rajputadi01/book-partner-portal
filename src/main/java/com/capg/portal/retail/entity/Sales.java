package com.capg.portal.retail.entity;

import com.capg.portal.catalog.entity.Title;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sales")
@IdClass(SalesId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sales {

    @Id
    @NotNull(message = "Store selection is required")
    @ManyToOne
    @JoinColumn(name = "stor_id")
    private Store store;

    @Id
    @NotBlank(message = "Order Number is required")
    @Size(max = 20, message = "Order Number cannot exceed 20 characters")
    @Column(name = "ord_num", length = 20)
    private String ordNum;

    @Id
    @NotNull(message = "Title selection is required")
    @ManyToOne
    @JoinColumn(name = "title_id")
    private Title title;

    @Column(name = "ord_date")
    private LocalDateTime ordDate;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    @Column(name = "qty", columnDefinition = "smallint")
    private Short qty;

    @NotBlank(message = "Payment terms are required")
    @Size(max = 12, message = "Payment terms cannot exceed 12 characters")
    @Column(name = "payterms", length = 12)
    private String payterms;
}