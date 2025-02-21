package com.doyatama.university.model;

public class Elemen {
    private String idElemen;
    private String namaElemen;
    private Mapel mapel;
    private TahunAjaran tahunAjaran;
    private Semester semester;
    private Kelas kelas;
    private KonsentrasiKeahlian konsentrasiKeahlian;

    public Elemen() {
    }

    public Elemen(String idElemen, String namaElemen, Mapel mapel, TahunAjaran tahunAjaran, Semester semester,
            Kelas kelas, KonsentrasiKeahlian konsentrasiKeahlian) {
        this.idElemen = idElemen;
        this.namaElemen = namaElemen;
        this.mapel = mapel;
        this.tahunAjaran = tahunAjaran;
        this.semester = semester;
        this.kelas = kelas;
        this.konsentrasiKeahlian = konsentrasiKeahlian;
    }

    public String getIdElemen() {
        return idElemen;
    }

    public void setIdElemen(String idElemen) {
        this.idElemen = idElemen;
    }

    public String getNamaElemen() {
        return namaElemen;
    }

    public void setNamaElemen(String namaElemen) {
        this.namaElemen = namaElemen;
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

    public boolean isValid() {
        return this.idElemen != null && this.namaElemen != null;
    }

    public void set(String fieldName, String value) {
        switch (fieldName) {
            case "idElemen":
                this.idElemen = value;
                break;
            case "namaElemen":
                this.namaElemen = value;
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }

}
