/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.doyatama.university.payload;

/**
 *
 * @author Nifan
 */
public class KelasRequest {
    private String idKelas;
    private String namaKelas;

    public KelasRequest() {
    }

    public KelasRequest(String idKelas, String namaKelas) {
        this.idKelas = idKelas;
        this.namaKelas = namaKelas;
    }

    public String getIdKelas() {
        return idKelas;
    }

    public void setIdKelas(String idKelas) {
        this.idKelas = idKelas;
    }

    public String getNamaKelas() {
        return namaKelas;
    }

    public void setNamaKelas(String namaKelas) {
        this.namaKelas = namaKelas;
    }

    public void set(String fieldName, String value) {
        switch (fieldName) {
            case "idKelas":
                this.idKelas = value;
                break;
            case "namaKelas":
                this.namaKelas = value;  
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }
}
