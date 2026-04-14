package com.capg.portal.catalog.entity;

import java.io.Serializable;
import java.util.Objects;

public class TitleAuthorId implements Serializable 
{
    private String author; // Matches the field name in TitleAuthor
    private String title;  // Matches the field name in TitleAuthor

    public TitleAuthorId() 
    {
    	
    }

    public TitleAuthorId(String author, String title) 
    {
        this.author = author;
        this.title = title;
    }

    public String getAuthor() 
    { 
    	return author; 
    }
    
    public void setAuthor(String author) 
    { 
    	this.author = author; 
    }

    public String getTitle() 
    { 
    	return title; 
    }
    
    public void setTitle(String title)
    { 
    	this.title = title; 
    }

    // Required for Composite Keys
    @Override
    public boolean equals(Object o) 
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TitleAuthorId that = (TitleAuthorId) o;
        return Objects.equals(author, that.author) && Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() 
    {
        return Objects.hash(author, title);
    }
}