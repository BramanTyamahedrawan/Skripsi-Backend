package com.doyatama.university.model;


public class Department {
    private String id;
    private String schoolID;
    private String name;
    private String description;

    public Department() {
    }

    public Department(String id, String schoolID, String name, String description) {
        this.id = id;
        this.schoolID = schoolID;
        this.name = name;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSchoolID() {
        return schoolID;
    }

    public void setSchoolID(String schoolID) {
        this.schoolID = schoolID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isValid() {
        return this.id != null && this.name != null && this.description != null;
    }

    public void set(String fieldName, String value) {
        switch (fieldName) {
            case "id":
                this.id = value;
                break;
            case "name":
                this.name = value;
                break;
            case "description":
                this.description = value;
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }
}