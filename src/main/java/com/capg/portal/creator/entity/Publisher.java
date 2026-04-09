package com.capg.portal.creator.entity;

import jakarta.persistence.*;
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
    @Column(name = "pub_id", columnDefinition = "char(4)")
    private String pubId;

    @Column(name = "pub_name", length = 40)
    private String pubName;

    @Column(name = "city", length = 20)
    private String city;

    @Column(name = "state", columnDefinition = "char(2)")
    private String state;

    @Column(name = "country", length = 30)
    private String country;
}