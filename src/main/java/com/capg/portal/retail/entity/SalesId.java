package com.capg.portal.retail.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class SalesId implements Serializable{
	private String Store;
	private String ordNum;
	private String title;
}
