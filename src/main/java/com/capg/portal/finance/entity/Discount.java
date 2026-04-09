package com.capg.portal.finance.entity;

import com.capg.portal.retail.entity.Store;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "discounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Discount {

    @Id
    @Column(name = "discounttype", length = 40)
    private String discountType;

    // Foreign Key mapping to Stores table
    @ManyToOne
    @JoinColumn(name = "stor_id")
    private Store store;

    @Column(name = "lowqty")
    private Integer lowQty;

    @Column(name = "highqty")
    private Integer highQty;

    @Column(name = "discount")
    private Double discountAmount; 
}