/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.doyatama.university.model;

/**
 *
 * @author Nifan
 */
public class TahunAjaran {
    private String idTahun;
    private String tahunAjaran;

    public TahunAjaran() {
    }

    public TahunAjaran(String idTahun, String tahunAjaran) {
        this.idTahun = idTahun;
        this.tahunAjaran = tahunAjaran;
    }

    public String getIdTahun() {
        return idTahun;
    }

    public void setIdTahun(String idTahun) {
        this.idTahun = idTahun;
    }

    public String getTahunAjaran() {
        return tahunAjaran;
    }

    public void setTahunAjaran(String tahunAjaran) {
        this.tahunAjaran = tahunAjaran;
    }
    
    public boolean isValid() {
        return this.idTahun != null && this.tahunAjaran != null;
    }

    public void set(String fieldName, String value) {
        switch (fieldName) {
            case "idTahun":
                this.idTahun = value;
                break;
            case "tahunAjaran":
                this.tahunAjaran = value;  
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }
}
