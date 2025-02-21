package com.doyatama.university.model;

public class Acp {
    private String idAcp;
    private String namaAcp;
    private Mapel mapel;
    private TahunAjaran tahunAjaran;
    private Semester semester;
    private Kelas kelas;
    private KonsentrasiKeahlian konsentrasiKeahlian;
    private Elemen elemen;

    public Acp() {
    }

    public Acp(String idAcp, String namaAcp, Mapel mapel, TahunAjaran tahunAjaran, Semester semester, Kelas kelas,
            KonsentrasiKeahlian konsentrasiKeahlian, Elemen elemen) {
        this.idAcp = idAcp;
        this.namaAcp = namaAcp;
        this.mapel = mapel;
        this.tahunAjaran = tahunAjaran;
        this.semester = semester;
        this.kelas = kelas;
        this.konsentrasiKeahlian = konsentrasiKeahlian;
        this.elemen = elemen;
    }

    public String getIdAcp() {
        return idAcp;
    }

    public void setIdAcp(String idAcp) {
        this.idAcp = idAcp;
    }

    public String getNamaAcp() {
        return namaAcp;
    }

    public void setNamaAcp(String namaAcp) {
        this.namaAcp = namaAcp;
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

    public boolean isValid() {
        return this.idAcp != null && this.namaAcp != null;
    }

    public void set(String fieldName, String value) {
        switch (fieldName) {
            case "idAcp":
                this.idAcp = value;
                break;
            case "namaAcp":
                this.namaAcp = value;
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }
}
