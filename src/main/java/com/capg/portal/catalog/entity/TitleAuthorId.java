package com.capg.portal.catalog.entity;

import java.io.Serializable;
import lombok.Data;

@Data
public class TitleAuthorId implements Serializable 
{
    private String author; // Matches the field name in TitleAuthor
    private String title;  // Matches the field name in TitleAuthor
}