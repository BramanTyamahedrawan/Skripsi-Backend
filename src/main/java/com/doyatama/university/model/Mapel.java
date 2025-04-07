package com.doyatama.university.model;

public class Mapel {
    private String idMapel;
    private String name;
    private School school;

    public Mapel() {
    }

    public Mapel(String idMapel, String name, School school) {
        this.idMapel = idMapel;
        this.name = name;
        this.school = school;
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

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public boolean isValid() {
        return this.idMapel != null &&
                this.name != null &&
                this.school != null;
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
