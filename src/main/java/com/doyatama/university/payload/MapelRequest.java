
package com.doyatama.university.payload;

public class MapelRequest {
    private String idMapel;
    private String name;
    private String idSekolah;

    public MapelRequest() {
    }

    public MapelRequest(String idMapel, String name, String idSekolah) {
        this.idMapel = idMapel;
        this.name = name;
        this.idSekolah = idSekolah;
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

    public String getIdSekolah() {
        return idSekolah;
    }

    public void setIdSekolah(String idSekolah) {
        this.idSekolah = idSekolah;
    }

    public void set(String fieldName, String value) {
        switch (fieldName) {
            case "idMapel":
                this.idMapel = value;
                break;
            case "name":
                this.name = value;
                break;
            case "idSekolah":
                this.idSekolah = value;
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }
}
