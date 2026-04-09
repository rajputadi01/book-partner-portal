package com.capg.portal.retail.entity;

import java.time.LocalDateTime;

import com.capg.portal.catalog.entity.Title;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sales")
@IdClass(SalesId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sales {
	@Id
	@ManyToOne
	@Column(name="stor_id")
	private Store store;

    @Id
    @Column(name = "ord_num", length = 20)
    private String ordNum;
    
    @Id
    @ManyToOne
    @Column(name="title-id")
    private Title title;
    
    @Column(name = "ord_date")
    private LocalDateTime ordDate;

    @Column(name = "qty", columnDefinition = "smallint")
    private Short qty;

    @Column(name = "payterms", length = 12)
    private String payterms;

	

}
