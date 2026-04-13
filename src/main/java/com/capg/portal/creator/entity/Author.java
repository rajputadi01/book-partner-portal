package com.capg.portal.creator.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "authors")
public class Author {

    @Id
    @NotBlank(message = "Author ID is required")
    @Pattern(regexp = "^[0-9]{3}-[0-9]{2}-[0-9]{4}$", message = "Format must be: XXX-XX-XXXX")
    @Column(name = "au_id", length = 11)
    private String auId;

    @NotBlank(message = "Last name is required")
    @Size(max = 40, message = "Last name cannot exceed 40 characters")
    @Column(name = "au_lname", nullable = false, length = 40)
    private String auLname;

    @NotBlank(message = "First name is required")
    @Size(max = 20, message = "First name cannot exceed 20 characters")
    @Column(name = "au_fname", nullable = false, length = 20)
    private String auFname;

    @Pattern(regexp = "^([0-9]{3} [0-9]{3}-[0-9]{4}|UNKNOWN)$", message = "Format: XXX XXX-XXXX or UNKNOWN")
    @Column(name = "phone", columnDefinition = "char(12)")
    private String phone = "UNKNOWN";

    @Column(name = "address", length = 40)
    private String address;

    @Column(name = "city", length = 20)
    private String city;

    @Size(max = 2, message = "State must be exactly 2 characters")
    @Column(name = "state", columnDefinition = "char(2)")
    private String state;

    @Pattern(regexp = "^([0-9]{5})?$", message = "Zip must be exactly 5 digits")
    @Column(name = "zip", columnDefinition = "char(5)")
    private String zip;

    @NotNull(message = "Contract status is required")
    @Column(name = "contract", nullable = false)
    private Integer contract = 1;

    // Default Constructor
    public Author() {
    }

    // Parameterized Constructor
    public Author(String auId, String auLname, String auFname, String phone, String address, String city, String state, String zip, Integer contract) {
        this.auId = auId;
        this.auLname = auLname;
        this.auFname = auFname;
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.contract = contract;
    }

    // Getters and Setters
    public String getAuId() { return auId; }
    public void setAuId(String auId) { this.auId = auId; }

    public String getAuLname() { return auLname; }
    public void setAuLname(String auLname) { this.auLname = auLname; }

    public String getAuFname() { return auFname; }
    public void setAuFname(String auFname) { this.auFname = auFname; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getZip() { return zip; }
    public void setZip(String zip) { this.zip = zip; }

    public Integer getContract() { return contract; }
    public void setContract(Integer contract) { this.contract = contract; }
}