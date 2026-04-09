package com.capg.portal.creator.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "authors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Author {

	@Id
    @Pattern(regexp = "^[0-9]{3}-[0-9]{2}-[0-9]{4}$", message = "Author ID must follow SSN format: XXX-XX-XXXX")
    @Column(name = "au_id", length = 11)
    private String auId;

    @NotBlank(message = "Author last name is required")
    @Size(max = 40, message = "Last name cannot exceed 40 characters")
    @Column(name = "au_lname", nullable = false, length = 40)
    private String auLname;

    @NotBlank(message = "Author first name is required")
    @Size(max = 20, message = "First name cannot exceed 20 characters")
    @Column(name = "au_fname", nullable = false, length = 20)
    private String auFname;

    @Pattern(regexp = "^[0-9]{3} [0-9]{3}-[0-9]{4}$", message = "Phone must be in format: XXX XXX-XXXX")
    @Column(name = "phone", columnDefinition = "char(12)")
    private String phone;

    @Column(name = "address", length = 40)
    private String address;

    @Column(name = "city", length = 20)
    private String city;

    @Size(min = 2, max = 2, message = "State must be exactly 2 characters")
    @Column(name = "state", columnDefinition = "char(2)")
    private String state;

    @Pattern(regexp = "^[0-9]{5}$", message = "Zip must be exactly 5 digits")
    @Column(name = "zip", columnDefinition = "char(5)")
    private String zip;

    @NotNull(message = "Contract status is required")
    @Column(name = "contract", nullable = false)
    private Integer contract;
}