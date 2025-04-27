
package com.doyatama.university.payload;

public class MapelRequest {
    private String idMapel;
    private String name;
    private String idSekolah;
    private String idKelas;
    private String idSemester;

    public MapelRequest() {
    }

    public MapelRequest(String idMapel, String name, String idSekolah, String idKelas, String idSemester) {
        this.idMapel = idMapel;
        this.name = name;
        this.idSekolah = idSekolah;
        this.idKelas = idKelas;
        this.idSemester = idSemester;
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

    public String getIdKelas() {
        return idKelas;
    }

    public void setIdKelas(String idKelas) {
        this.idKelas = idKelas;
    }

    public String getIdSemester() {
        return idSemester;
    }

    public void setIdSemester(String idSemester) {
        this.idSemester = idSemester;
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
            case "idKelas":
                this.idKelas = value;
                break;
            case "idSemester":
                this.idSemester = value;
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }
}
