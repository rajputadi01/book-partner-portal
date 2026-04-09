package com.capg.portal.finance.entity;

import com.capg.portal.catalog.entity.Title;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "roysched")
@IdClass(RoyaltyScheduleId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoyaltySchedule {

    @Id
    @ManyToOne
    @JoinColumn(name = "title_id")
    private Title title;

    @Id
    @Column(name = "lorange")
    private Integer lorange;

    @Column(name = "hirange")
    private Integer hirange;

    @Column(name = "royalty")
    private Integer royalty;
}