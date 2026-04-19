package com.capg.portal.auth.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // e.g., "ROLE_ADMIN" or "ROLE_STAFF"
    @Column(unique = true, nullable = false)
    private String name; 

    // Constructors, Getters, and Setters
    public Role() {}
    public Role(String name) { this.name = name; }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}