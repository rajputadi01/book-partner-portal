package com.capg.portal.finance.entity;

import com.capg.portal.retail.entity.Store;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Entity
@Table(name = "discounts")
public class Discount {

    @Id
    @NotBlank(message = "Discount type is required")
    @Size(max = 40, message = "Discount type cannot exceed 40 characters")
    @Column(name = "discounttype", length = 40)
    private String discountType;

    // Foreign Key mapping to Stores table. The DB allows this to be NULL.
    @ManyToOne
    @JoinColumn(name = "stor_id")
    private Store store;

    @Column(name = "lowqty")
    private Integer lowQty;

    @Column(name = "highqty")
    private Integer highQty;

    @NotNull(message = "Discount amount is required")
    @Digits(integer = 2, fraction = 2, message = "Must be a valid percentage (e.g., 10.50)")
    @Column(name = "discount", columnDefinition = "decimal(4,2)")
    private BigDecimal discountAmount;

    // Default Constructor
    public Discount() {
    }

    // Parameterized Constructor
    public Discount(String discountType, Store store, Integer lowQty, Integer highQty, BigDecimal discountAmount) {
        this.discountType = discountType;
        this.store = store;
        this.lowQty = lowQty;
        this.highQty = highQty;
        this.discountAmount = discountAmount;
    }

    // Getters and Setters
    public String getDiscountType() { return discountType; }
    public void setDiscountType(String discountType) { this.discountType = discountType; }

    public Store getStore() { return store; }
    public void setStore(Store store) { this.store = store; }

    public Integer getLowQty() { return lowQty; }
    public void setLowQty(Integer lowQty) { this.lowQty = lowQty; }

    public Integer getHighQty() { return highQty; }
    public void setHighQty(Integer highQty) { this.highQty = highQty; }

    public BigDecimal getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }
}