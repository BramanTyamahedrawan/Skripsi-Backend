package com.doyatama.university.repository;

import com.doyatama.university.helper.HBaseCustomClient;
import com.doyatama.university.model.BankSoal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BankSoalRepository {

    Configuration conf = HBaseConfiguration.create();
    String tableName = "bankSoal";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<BankSoal> findAll(int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableBankSoal = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap (kolom standar)
        columnMapping.put("idBankSoal", "idBankSoal");
        columnMapping.put("idSoalUjian", "idSoalUjian");
        columnMapping.put("namaUjian", "namaUjian");
        columnMapping.put("pertanyaan", "pertanyaan");
        columnMapping.put("bobot", "bobot");
        columnMapping.put("jenisSoal", "jenisSoal");
        columnMapping.put("toleransiTypo", "toleransiTypo");
        columnMapping.put("createdAt", "createdAt");
        columnMapping.put("soalUjian", "soalUjian");
        columnMapping.put("taksonomi", "taksonomi");
        columnMapping.put("mapel", "mapel");
        columnMapping.put("tahunAjaran", "tahunAjaran");
        columnMapping.put("semester", "semester");
        columnMapping.put("kelas", "kelas");
        columnMapping.put("elemen", "elemen");
        columnMapping.put("acp", "acp");
        columnMapping.put("atp", "atp");
        columnMapping.put("konsentrasiKeahlianSekolah", "konsentrasiKeahlianSekolah");
        columnMapping.put("school", "school");

        // Definisikan field yang menggunakan indeks
        Map<String, String> indexedFields = new HashMap<>();
        indexedFields.put("opsi", "MAP"); // opsi disimpan sebagai MAP
        indexedFields.put("pasangan", "MAP"); // pasangan disimpan sebagai MAP
        indexedFields.put("jawabanBenar", "LIST"); // jawabanBenar disimpan sebagai LIST

        return client.showListTableIndex(tableBankSoal.toString(), columnMapping, BankSoal.class, indexedFields,
                size);
    }

    public BankSoal save(BankSoal bankSoal) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        String rowKey = bankSoal.getIdBankSoal();
        TableName tableBankSoal = TableName.valueOf(tableName);

        // Save main info
        saveMainInfo(client, tableBankSoal, rowKey, bankSoal);

        // Save relationships
        saveRelationships(client, tableBankSoal, rowKey, bankSoal);

        // Save question details based on type
        switch (bankSoal.getJenisSoal().toUpperCase()) {
            case "PG":
            case "MULTI":
                savePilihanGanda(client, tableBankSoal, rowKey, bankSoal);
                break;

            case "COCOK":
                saveCocokkan(client, tableBankSoal, rowKey, bankSoal);
                break;

            case "ISIAN":
                saveIsian(client, tableBankSoal, rowKey, bankSoal);
                break;
        }

        client.insertRecord(tableBankSoal, rowKey, "detail", "created_by", "Polinema");
        return bankSoal;
    }

    private void saveMainInfo(HBaseCustomClient client, TableName table, String rowKey, BankSoal bankSoal) {
        client.insertRecord(table, rowKey, "main", "idBankSoal", bankSoal.getIdBankSoal());
        client.insertRecord(table, rowKey, "main", "idSoalUjian", bankSoal.getIdSoalUjian());
        client.insertRecord(table, rowKey, "main", "namaUjian", bankSoal.getNamaUjian());
        client.insertRecord(table, rowKey, "main", "pertanyaan", bankSoal.getPertanyaan());
        client.insertRecord(table, rowKey, "main", "bobot", bankSoal.getBobot());
        client.insertRecord(table, rowKey, "main", "jenisSoal", bankSoal.getJenisSoal());
        client.insertRecord(table, rowKey, "main", "createdAt", bankSoal.getCreatedAt().toString());
    }

    private void saveRelationships(HBaseCustomClient client, TableName table, String rowKey, BankSoal bankSoal) {
        if (bankSoal.getIdSoalUjian() != null) {
            client.insertRecord(table, rowKey, "soalUjian", "idSoalUjian", bankSoal.getSoalUjian().getIdSoalUjian());
            client.insertRecord(table, rowKey, "soalUjian", "namaUjian", bankSoal.getSoalUjian().getNamaUjian());
            client.insertRecord(table, rowKey, "soalUjian", "pertanyaan", bankSoal.getSoalUjian().getPertanyaan());
            client.insertRecord(table, rowKey, "soalUjian", "bobot", bankSoal.getSoalUjian().getBobot());
            client.insertRecord(table, rowKey, "soalUjian", "jenisSoal", bankSoal.getSoalUjian().getJenisSoal());
            client.insertRecord(table, rowKey, "soalUjian", "createdAt",
                    bankSoal.getSoalUjian().getCreatedAt().toString());
        }

        if (bankSoal.getTahunAjaran().getIdTahun() != null) {
            client.insertRecord(table, rowKey, "tahunAjaran", "idTahun", bankSoal.getTahunAjaran().getIdTahun());
            client.insertRecord(table, rowKey, "tahunAjaran", "tahunAjaran",
                    bankSoal.getTahunAjaran().getTahunAjaran());
        }

        if (bankSoal.getSemester().getIdSemester() != null) {
            client.insertRecord(table, rowKey, "semester", "idSemester", bankSoal.getSemester().getIdSemester());
            client.insertRecord(table, rowKey, "semester", "namaSemester", bankSoal.getSemester().getNamaSemester());
        }

        if (bankSoal.getKelas().getIdKelas() != null) {
            client.insertRecord(table, rowKey, "kelas", "idKelas", bankSoal.getKelas().getIdKelas());
            client.insertRecord(table, rowKey, "kelas", "namaKelas", bankSoal.getKelas().getNamaKelas());
        }

        if (bankSoal.getMapel().getIdMapel() != null) {
            client.insertRecord(table, rowKey, "mapel", "idMapel", bankSoal.getMapel().getIdMapel());
            client.insertRecord(table, rowKey, "mapel", "name", bankSoal.getMapel().getName());
        }

        if (bankSoal.getElemen().getIdElemen() != null) {
            client.insertRecord(table, rowKey, "elemen", "idElemen", bankSoal.getElemen().getIdElemen());
            client.insertRecord(table, rowKey, "elemen", "namaElemen", bankSoal.getElemen().getNamaElemen());
        }

        if (bankSoal.getAcp().getIdAcp() != null) {
            client.insertRecord(table, rowKey, "acp", "idAcp", bankSoal.getAcp().getIdAcp());
            client.insertRecord(table, rowKey, "acp", "namaAcp", bankSoal.getAcp().getNamaAcp());
        }

        if (bankSoal.getAtp().getIdAtp() != null) {
            client.insertRecord(table, rowKey, "atp", "idAtp", bankSoal.getAtp().getIdAtp());
            client.insertRecord(table, rowKey, "atp", "namaAtp", bankSoal.getAtp().getNamaAtp());
        }

        if (bankSoal.getTaksonomi().getIdTaksonomi() != null) {
            client.insertRecord(table, rowKey, "taksonomi", "idTaksonomi", bankSoal.getTaksonomi().getIdTaksonomi());
            client.insertRecord(table, rowKey, "taksonomi", "namaTaksonomi",
                    bankSoal.getTaksonomi().getNamaTaksonomi());
        }

        if (bankSoal.getKonsentrasiKeahlianSekolah().getIdKonsentrasiSekolah() != null) {
            client.insertRecord(table, rowKey, "konsentrasiKeahlianSekolah", "idKonsentrasiSekolah",
                    bankSoal.getKonsentrasiKeahlianSekolah().getIdKonsentrasiSekolah());
            client.insertRecord(table, rowKey, "konsentrasiKeahlianSekolah", "namaKonsentrasiSekolah",
                    bankSoal.getKonsentrasiKeahlianSekolah().getNamaKonsentrasiSekolah());

        }

        if (bankSoal.getSchool().getIdSchool() != null) {
            client.insertRecord(table, rowKey, "school", "idSchool", bankSoal.getSchool().getIdSchool());
            client.insertRecord(table, rowKey, "school", "nameSchool", bankSoal.getSchool().getNameSchool());
        }
    }

    private void savePilihanGanda(HBaseCustomClient client, TableName table, String rowKey, BankSoal bankSoal) {
        try {
            // Simpan opsi sebagai JSON
            String opsiJson = objectMapper.writeValueAsString(bankSoal.getOpsi());
            client.insertRecord(table, rowKey, "main", "opsi", opsiJson);

            // Simpan jawaban benar sebagai JSON
            String jawabanJson = objectMapper.writeValueAsString(bankSoal.getJawabanBenar());
            client.insertRecord(table, rowKey, "main", "jawabanBenar", jawabanJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize options or answers", e);
        }
    }

    private void saveCocokkan(HBaseCustomClient client, TableName table, String rowKey, BankSoal bankSoal) {
        try {
            // Simpan pasangan sebagai JSON
            String pasanganJson = objectMapper.writeValueAsString(bankSoal.getPasangan());
            client.insertRecord(table, rowKey, "main", "pasangan", pasanganJson);

            // Simpan jawaban benar sebagai JSON
            String jawabanJson = objectMapper.writeValueAsString(bankSoal.getJawabanBenar());
            client.insertRecord(table, rowKey, "main", "jawabanBenar", jawabanJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize matching pairs or answers", e);
        }
    }

    private void saveIsian(HBaseCustomClient client, TableName table, String rowKey, BankSoal bankSoal) {
        try {
            // Simpan jawaban benar sebagai JSON
            String jawabanJson = objectMapper.writeValueAsString(bankSoal.getJawabanBenar());
            client.insertRecord(table, rowKey, "main", "jawabanBenar", jawabanJson);

            // Simpan toleransi typo
            client.insertRecord(table, rowKey, "main", "toleransiTypo",
                    String.valueOf(bankSoal.getToleransiTypo()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize answers", e);
        }
    }

    public BankSoal findById(String bankSoalId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableBankSoal = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        columnMapping.put("idBankSoal", "idBankSoal");
        columnMapping.put("idSoalUjian", "idSoalUjian");
        columnMapping.put("namaUjian", "namaUjian");
        columnMapping.put("pertanyaan", "pertanyaan");
        columnMapping.put("bobot", "bobot");
        columnMapping.put("jenisSoal", "jenisSoal");
        columnMapping.put("opsi", "opsi");
        columnMapping.put("pasangan", "pasangan");
        columnMapping.put("jawabanBenar", "jawabanBenar");
        columnMapping.put("toleransiTypo", "toleransiTypo");
        columnMapping.put("createdAt", "createdAt");
        columnMapping.put("soalUjian", "soalUjian");
        columnMapping.put("taksonomi", "taksonomi");
        columnMapping.put("mapel", "mapel");
        columnMapping.put("tahunAjaran", "tahunAjaran");
        columnMapping.put("semester", "semester");
        columnMapping.put("kelas", "kelas");
        columnMapping.put("elemen", "elemen");
        columnMapping.put("acp", "acp");
        columnMapping.put("atp", "atp");
        columnMapping.put("konsentrasiKeahlianSekolah", "konsentrasiKeahlianSekolah");
        columnMapping.put("school", "school");

        Map<String, String> indexedFields = new HashMap<>();
        indexedFields.put("opsi", "MAP");
        indexedFields.put("pasangan", "MAP");
        indexedFields.put("jawabanBenar", "LIST");

        return client.showDataTable(tableBankSoal.toString(), columnMapping, bankSoalId, BankSoal.class);
    }

    public List<BankSoal> findBankSoalBySekolah(String schoolID, int size) throws IOException {
        TableName tableBankSoal = TableName.valueOf(tableName);
        HBaseCustomClient client = new HBaseCustomClient(conf);

        // Standard column mappings
        Map<String, String> columnMapping = new HashMap<>();
        columnMapping.put("idBankSoal", "idBankSoal");
        columnMapping.put("idSoalUjian", "idSoalUjian");
        columnMapping.put("namaUjian", "namaUjian");
        columnMapping.put("pertanyaan", "pertanyaan");
        columnMapping.put("bobot", "bobot");
        columnMapping.put("jenisSoal", "jenisSoal");
        columnMapping.put("opsi", "opsi");
        columnMapping.put("pasangan", "pasangan");
        columnMapping.put("jawabanBenar", "jawabanBenar");
        columnMapping.put("toleransiTypo", "toleransiTypo");
        columnMapping.put("createdAt", "createdAt");
        columnMapping.put("soalUjian", "soalUjian");
        columnMapping.put("taksonomi", "taksonomi");
        columnMapping.put("mapel", "mapel");
        columnMapping.put("tahunAjaran", "tahunAjaran");
        columnMapping.put("semester", "semester");
        columnMapping.put("kelas", "kelas");
        columnMapping.put("elemen", "elemen");
        columnMapping.put("acp", "acp");
        columnMapping.put("atp", "atp");
        columnMapping.put("konsentrasiKeahlianSekolah", "konsentrasiKeahlianSekolah");
        columnMapping.put("school", "school");

        Map<String, String> indexedFields = new HashMap<>();
        indexedFields.put("opsi", "MAP");
        indexedFields.put("pasangan", "MAP");
        indexedFields.put("jawabanBenar", "LIST");

        List<BankSoal> bankSoalList = client.getDataListByColumnIndeks(
                tableBankSoal.toString(),
                columnMapping,
                "school",
                "idSchool",
                schoolID,
                BankSoal.class,
                size,
                indexedFields);

        return bankSoalList;
    }

    public List<BankSoal> findAllById(List<String> bankSoalIds) throws IOException {
        if (bankSoalIds == null || bankSoalIds.isEmpty()) {
            return new ArrayList<>();
        }

        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableBankSoal = TableName.valueOf(tableName);

        // Standard column mappings
        Map<String, String> columnMapping = new HashMap<>();
        columnMapping.put("idBankSoal", "idBankSoal");
        columnMapping.put("idSoalUjian", "idSoalUjian");
        columnMapping.put("namaUjian", "namaUjian");
        columnMapping.put("pertanyaan", "pertanyaan");
        columnMapping.put("bobot", "bobot");
        columnMapping.put("jenisSoal", "jenisSoal");
        columnMapping.put("opsi", "opsi");
        columnMapping.put("pasangan", "pasangan");
        columnMapping.put("jawabanBenar", "jawabanBenar");
        columnMapping.put("toleransiTypo", "toleransiTypo");
        columnMapping.put("createdAt", "createdAt");
        columnMapping.put("soalUjian", "soalUjian");
        columnMapping.put("taksonomi", "taksonomi");
        columnMapping.put("mapel", "mapel");
        columnMapping.put("tahunAjaran", "tahunAjaran");
        columnMapping.put("semester", "semester");
        columnMapping.put("kelas", "kelas");
        columnMapping.put("elemen", "elemen");
        columnMapping.put("acp", "acp");
        columnMapping.put("atp", "atp");
        columnMapping.put("konsentrasiKeahlianSekolah", "konsentrasiKeahlianSekolah");
        columnMapping.put("school", "school");

        // Definisikan field yang menggunakan indeks
        Map<String, String> indexedFields = new HashMap<>();
        indexedFields.put("opsi", "MAP");
        indexedFields.put("pasangan", "MAP");
        indexedFields.put("jawabanBenar", "LIST");

        List<BankSoal> result = new ArrayList<>();

        // Fetch each BankSoal by ID
        for (String bankSoalId : bankSoalIds) {
            try {
                BankSoal bankSoal = client.showDataTable(
                        tableBankSoal.toString(),
                        columnMapping,
                        bankSoalId,
                        BankSoal.class);

                if (bankSoal != null) {
                    result.add(bankSoal);
                }
            } catch (Exception e) {
                // Log the error for debugging but continue processing other IDs
                System.err.println("Error fetching BankSoal with ID: " + bankSoalId + " - " + e.getMessage());
            }
        }

        return result;
    }

    public boolean deleteById(String bankSoalId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        client.deleteRecord(tableName, bankSoalId);
        return true;
    }

    public boolean existsById(String bankSoalId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableBankSoal = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();
        columnMapping.put("idBankSoal", "idBankSoal");

        BankSoal bankSoal = client.getDataByColumn(tableBankSoal.toString(), columnMapping,
                "main", "idBankSoal",
                bankSoalId, BankSoal.class);

        return bankSoal.getIdBankSoal() != null;
    }

}
