package com.doyatama.university.payload;

public class BidangKeahlianRequest {
    
    private String id;
    private String bidang; 
    private String school_id;
    
    public BidangKeahlianRequest() {
    }
    
    public BidangKeahlianRequest(String id, String bidang, String school_id) {
        this.id = id;
        this.bidang = bidang;
        this.school_id = school_id;
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

    public String getSchool_id() {
        return school_id;
    }
    public void setSchool_id(String school_id) {
        this.school_id = school_id;
    }
    public boolean isValid() {
        return this.id != null && this.bidang != null;
    }

    public void set(String fieldName, String value) {
        switch (fieldName) {
            case "id":
                this.id = value;
                break;
            case "bidang":
                this.bidang = value;  
                break;
            case "school_id":
                this.school_id = value;
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }
}
