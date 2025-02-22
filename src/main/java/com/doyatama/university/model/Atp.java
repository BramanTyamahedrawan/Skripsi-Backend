package com.doyatama.university.model;

public class Atp {
    private String idAtp;
    private String namaAtp;
    private Mapel mapel;
    private TahunAjaran tahunAjaran;
    private Semester semester;
    private Kelas kelas;
    private KonsentrasiKeahlian konsentrasiKeahlian;
    private Elemen elemen;
    private Acp acp;

    public Atp() {
    }

    public Atp(String idAtp, String namaAtp, Mapel mapel, TahunAjaran tahunAjaran, Semester semester, Kelas kelas,
            KonsentrasiKeahlian konsentrasiKeahlian, Elemen elemen, Acp acp) {
        this.idAtp = idAtp;
        this.namaAtp = namaAtp;
        this.mapel = mapel;
        this.tahunAjaran = tahunAjaran;
        this.semester = semester;
        this.kelas = kelas;
        this.konsentrasiKeahlian = konsentrasiKeahlian;
        this.elemen = elemen;
        this.acp = acp;
    }

    public String getIdAtp() {
        return idAtp;
    }

    public void setIdAtp(String idAtp) {
        this.idAtp = idAtp;
    }

    public String getNamaAtp() {
        return namaAtp;
    }

    public void setNamaAtp(String namaAtp) {
        this.namaAtp = namaAtp;
    }

    public Mapel getMapel() {
        return mapel;
    }

    public void setMapel(Mapel mapel) {
        this.mapel = mapel;
    }

    public TahunAjaran getTahunAjaran() {
        return tahunAjaran;
    }

    public void setTahunAjaran(TahunAjaran tahunAjaran) {
        this.tahunAjaran = tahunAjaran;
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public Kelas getKelas() {
        return kelas;
    }

    public void setKelas(Kelas kelas) {
        this.kelas = kelas;
    }

    public KonsentrasiKeahlian getKonsentrasiKeahlian() {
        return konsentrasiKeahlian;
    }

    public void setKonsentrasiKeahlian(KonsentrasiKeahlian konsentrasiKeahlian) {
        this.konsentrasiKeahlian = konsentrasiKeahlian;
    }

    public Elemen getElemen() {
        return elemen;
    }

    public void setElemen(Elemen elemen) {
        this.elemen = elemen;
    }

    public Acp getAcp() {
        return acp;
    }

    public void setAcp(Acp acp) {
        this.acp = acp;
    }

    public boolean isValid() {
        return this.idAtp != null && this.namaAtp != null;
    }

    public void set(String fieldName, String value) {
        switch (fieldName) {
            case "idAtp":
                this.idAtp = value;
                break;
            case "namaAtp":
                this.namaAtp = value;
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }
}
