
package com.doyatama.university.payload;

public class MapelRequest {
    private String idMapel;
    private String name;

    public MapelRequest() {
    }

    public MapelRequest(String idMapel, String name) {
        this.idMapel = idMapel;
        this.name = name;
    }

    public String getIdMapel() {
        return idMapel;
    }

    public void setIdMapel(String idMapel) {
        this.idMapel = idMapel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

 
    
     public void set(String fieldName, String value) {
        switch (fieldName) {
            case "idMapel":
                this.idMapel = value;
                break;
            case "name":
                this.name = value;  
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }
}
