package com.doyatama.university.payload;

public class JenisSoalRequest {
    private String idJenisSoal;
    private String pilihanGanda;
    private String isian;
    private String pilihanBanyakJawaban;
    private String mencocokkan;
    private String idSekolah;

    public JenisSoalRequest() {
    }

    public JenisSoalRequest(String idJenisSoal, String pilihanGanda, String isian, String pilihanBanyakJawaban,
            String mencocokkan, String idSekolah) {
        this.idJenisSoal = idJenisSoal;
        this.pilihanGanda = pilihanGanda;
        this.isian = isian;
        this.pilihanBanyakJawaban = pilihanBanyakJawaban;
        this.mencocokkan = mencocokkan;
        this.idSekolah = idSekolah;
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

    public String getIdSekolah() {
        return idSekolah;
    }

    public void setIdSekolah(String idSekolah) {
        this.idSekolah = idSekolah;
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
            case "idSekolah":
                this.idSekolah = value;
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }
}
