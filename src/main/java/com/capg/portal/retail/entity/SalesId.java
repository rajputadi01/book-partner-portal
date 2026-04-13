package com.capg.portal.retail.entity;

import java.io.Serializable;
import java.util.Objects;

public class SalesId implements Serializable {
    
    private String store; 
    private String ordNum;
    private String title;

    public SalesId() {
    }

    public SalesId(String store, String ordNum, String title) {
        this.store = store;
        this.ordNum = ordNum;
        this.title = title;
    }

    public String getStore() { return store; }
    public void setStore(String store) { this.store = store; }

    public String getOrdNum() { return ordNum; }
    public void setOrdNum(String ordNum) { this.ordNum = ordNum; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SalesId salesId = (SalesId) o;
        return Objects.equals(store, salesId.store) &&
               Objects.equals(ordNum, salesId.ordNum) &&
               Objects.equals(title, salesId.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(store, ordNum, title);
    }
}