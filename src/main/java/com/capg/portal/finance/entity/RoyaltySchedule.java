package com.capg.portal.finance.entity;

import com.capg.portal.catalog.entity.Title;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "roysched")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoyaltySchedule 
{
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roysched_id")
    private Integer royschedId;

    @NotNull(message = "Title selection is required")
    @ManyToOne
    @JoinColumn(name = "title_id")
    private Title title;

    @NotNull(message = "Low range is required")
    @Column(name = "lorange")
    private Integer lorange;

    @NotNull(message = "High range is required")
    @Column(name = "hirange")
    private Integer hirange;

    @NotNull(message = "Royalty percentage is required")
    @Column(name = "royalty")
    private Integer royalty;
    
    public Integer getRoyschedId() {
		return royschedId;
	}

	public void setRoyschedId(Integer royschedId) {
		this.royschedId = royschedId;
	}

	public Title getTitle() {
		return title;
	}

	public void setTitle(Title title) {
		this.title = title;
	}

	public Integer getLorange() {
		return lorange;
	}

	public void setLorange(Integer lorange) {
		this.lorange = lorange;
	}

	public Integer getHirange() {
		return hirange;
	}

	public void setHirange(Integer hirange) {
		this.hirange = hirange;
	}

	public Integer getRoyalty() {
		return royalty;
	}

	public void setRoyalty(Integer royalty) {
		this.royalty = royalty;
	}


}