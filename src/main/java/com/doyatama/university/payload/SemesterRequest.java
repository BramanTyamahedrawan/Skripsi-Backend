package com.doyatama.university.payload;

public class SemesterRequest {
    private String idSemester;
    private String namaSemester;

    public SemesterRequest() {
    }

    public SemesterRequest(String idSemester, String namaSemester) {
        this.idSemester = idSemester;
        this.namaSemester = namaSemester;
    }

    public String getIdSemester() {
        return idSemester;
    }

    public void setIdSemester(String idSemester) {
        this.idSemester = idSemester;
    }

    public String getNamaSemester() {
        return namaSemester;
    }

    public void setNamaSemester(String namaSemester) {
        this.namaSemester = namaSemester;
    }

    public void set(String fieldName, String value) {
        switch (fieldName) {
            case "idSemester":
                this.idSemester = value;
                break;
            case "namaSemester":
                this.namaSemester = value;
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }
}
