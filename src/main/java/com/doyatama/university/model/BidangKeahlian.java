package com.doyatama.university.model;

public class BidangKeahlian {
    private String id;
    private String bidang;
    private School school;
    
    public BidangKeahlian() {
    }
    
    public BidangKeahlian(String id, String bidang, School school ){
        this.id = id;
        this.bidang = bidang;
        this.school = school;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBidang() {
        return bidang;
    }

    public void setBidang(String bidang) {
        this.bidang = bidang;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public boolean isValid() {
        return this.id != null &&
                this.bidang != null && 
                this.school != null ;
    }
    public void set(String fieldName, String value) {
        switch (fieldName) {
            case "id":
                this.id = value;
                break;
            case "bidang":
                this.bidang = value;  
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }
}
