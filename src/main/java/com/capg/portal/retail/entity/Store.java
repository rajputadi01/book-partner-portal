package com.capg.portal.retail.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "stores")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Store {

    @Id
    @NotBlank(message = "Store ID is required")
    @Size(min = 4, max = 4, message = "Store ID must be exactly 4 characters")
    @Column(name = "stor_id", columnDefinition = "char(4)")
    private String storId;

    @NotBlank(message = "Store name is required")
    @Size(max = 40, message = "Name cannot exceed 40 characters")
    @Column(name = "stor_name", length = 40)
    private String storName;

    @Size(max = 40, message = "Address cannot exceed 40 characters")
    @Column(name = "stor_address", length = 40)
    private String storAddress;

    @Size(max = 20, message = "City cannot exceed 20 characters")
    @Column(name = "city", length = 20)
    private String city;

    // Allows exactly 2 characters OR empty
    @Size(max = 2, message = "State must be exactly 2 characters")
    @Column(name = "state", columnDefinition = "char(2)")
    private String state;

    // Allows exactly 5 digits OR an empty string
    @Pattern(regexp = "^([0-9]{5})?$", message = "Zip must be exactly 5 digits")
    @Column(name = "zip", columnDefinition = "char(5)")
    private String zip;
}