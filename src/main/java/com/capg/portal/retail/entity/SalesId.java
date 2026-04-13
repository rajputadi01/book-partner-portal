package com.capg.portal.retail.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesId implements Serializable {

    // These names MUST exactly match the lowercase variable names in Sales.java!
    private String store;  // Matches 'private Store store;'
    private String ordNum; // Matches 'private String ordNum;'
    private String title;  // Matches 'private Title title;'
    
}