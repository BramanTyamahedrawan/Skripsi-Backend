package com.doyatama.university.model;

public class JenisSoal {
    private String idJenisSoal;
    private String pilihanGanda;
    private String isian;
    private String pilihanBanyakJawaban;
    private String mencocokkan;
    private School school;

    public JenisSoal() {
    }

    public JenisSoal(String idJenisSoal, String pilihanGanda, String isian, String pilihanBanyakJawaban,
            String mencocokkan, School school) {
        this.idJenisSoal = idJenisSoal;
        this.pilihanGanda = pilihanGanda;
        this.isian = isian;
        this.pilihanBanyakJawaban = pilihanBanyakJawaban;
        this.mencocokkan = mencocokkan;
        this.school = school;
    }

    public String getIdJenisSoal() {
        return idJenisSoal;
    }

    public void setIdJenisSoal(String idJenisSoal) {
        this.idJenisSoal = idJenisSoal;
    }

    public String getPilihanGanda() {
        return pilihanGanda;
    }

    public void setPilihanGanda(String pilihanGanda) {
        this.pilihanGanda = pilihanGanda;
    }

    public String getIsian() {
        return isian;
    }

    public void setIsian(String isian) {
        this.isian = isian;
    }

    public String getPilihanBanyakJawaban() {
        return pilihanBanyakJawaban;
    }

    public void setPilihanBanyakJawaban(String pilihanBanyakJawaban) {
        this.pilihanBanyakJawaban = pilihanBanyakJawaban;
    }

    public String getMencocokkan() {
        return mencocokkan;
    }

    public void setMencocokkan(String mencocokkan) {
        this.mencocokkan = mencocokkan;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public boolean isValid() {
        return idJenisSoal != null &&
                pilihanGanda != null &&
                isian != null &&
                pilihanBanyakJawaban != null &&
                mencocokkan != null &&
                school != null;
    }

    public void set(String fieldName, String value) {
        switch (fieldName) {
            case "idJenisSoal":
                this.idJenisSoal = value;
                break;
            case "pilihanGanda":
                this.pilihanGanda = value;
                break;
            case "isian":
                this.isian = value;
                break;
            case "pilihanBanyakJawaban":
                this.pilihanBanyakJawaban = value;
                break;
            case "mencocokkan":
                this.mencocokkan = value;
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }
}
