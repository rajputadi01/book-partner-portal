package com.capg.portal.creator.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "publishers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Publisher {

    @Id
    @NotBlank(message = "Publisher ID is required")
    @Pattern(regexp = "^(1389|0736|0877|1622|1756|99[0-9]{2})$", message = "ID must be 1389, 0736, 0877, 1622, 1756, or start with 99 followed by 2 digits")
    @Column(name = "pub_id", columnDefinition = "char(4)")
    private String pubId;

    @Size(max = 40, message = "Name cannot exceed 40 characters")
    @Column(name = "pub_name", length = 40)
    private String pubName;

    @Size(max = 20, message = "City cannot exceed 20 characters")
    @Column(name = "city", length = 20)
    private String city;

    @Size(max = 2, message = "State must be 2 characters")
    @Column(name = "state", columnDefinition = "char(2)")
    private String state;

    @Size(max = 30, message = "Country cannot exceed 30 characters")
    @Column(name = "country", length = 30)
    private String country = "USA";
}