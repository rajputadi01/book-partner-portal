package com.capg.portal.finance.entity;

import com.capg.portal.retail.entity.Store;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @NotBlank(message="Discount type is required")
    @Size(max=40,message="Discount type cannot exceed 40 character")
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

    @NotNull(message="Discount amount is required")
    @Digits(integer=2, fraction=2, message= "Must be a valid percentage e.g : 10.50")
    @Column(name = "discount")
    private Double discountAmount; 
}