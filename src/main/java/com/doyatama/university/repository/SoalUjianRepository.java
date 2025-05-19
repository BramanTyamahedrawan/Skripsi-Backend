package com.doyatama.university.repository;

import com.doyatama.university.helper.HBaseCustomClient;
import com.doyatama.university.model.SoalUjian;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SoalUjianRepository {

    Configuration conf = HBaseConfiguration.create();
    String tableName = "soalUjian";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<SoalUjian> findAll(int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableSoalUjian = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap (kolom standar)
        columnMapping.put("idSoalUjian", "idSoalUjian");
        columnMapping.put("namaUjian", "namaUjian");
        columnMapping.put("pertanyaan", "pertanyaan");
        columnMapping.put("bobot", "bobot");
        columnMapping.put("jenisSoal", "jenisSoal");
        columnMapping.put("toleransiTypo", "toleransiTypo");
        columnMapping.put("createdAt", "createdAt");
        columnMapping.put("user", "user");
        columnMapping.put("taksonomi", "taksonomi");
        columnMapping.put("atp", "atp");
        columnMapping.put("school", "school");

        // Definisikan field yang menggunakan indeks
        Map<String, String> indexedFields = new HashMap<>();
        indexedFields.put("opsi", "MAP"); // opsi disimpan sebagai MAP
        indexedFields.put("pasangan", "MAP"); // pasangan disimpan sebagai MAP
        indexedFields.put("jawabanBenar", "LIST"); // jawabanBenar disimpan sebagai LIST

        return client.showListTableIndex(tableSoalUjian.toString(), columnMapping, SoalUjian.class, indexedFields,
                size);
    }

    public SoalUjian save(SoalUjian soalUjian) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        String rowKey = soalUjian.getIdSoalUjian();
        TableName tableSoalUjian = TableName.valueOf(tableName);

        // Save main info
        saveMainInfo(client, tableSoalUjian, rowKey, soalUjian);

        // Save relationships
        saveRelationships(client, tableSoalUjian, rowKey, soalUjian);

        // Save question details based on type
        switch (soalUjian.getJenisSoal().toUpperCase()) {
            case "PG":
            case "MULTI":
                savePilihanGanda(client, tableSoalUjian, rowKey, soalUjian);
                break;

            case "COCOK":
                saveCocokkan(client, tableSoalUjian, rowKey, soalUjian);
                break;

            case "ISIAN":
                saveIsian(client, tableSoalUjian, rowKey, soalUjian);
                break;
        }

        client.insertRecord(tableSoalUjian, rowKey, "detail", "created_by", "Polinema");
        return soalUjian;
    }

    private void saveMainInfo(HBaseCustomClient client, TableName table, String rowKey, SoalUjian soal) {
        client.insertRecord(table, rowKey, "main", "idSoalUjian", soal.getIdSoalUjian());
        client.insertRecord(table, rowKey, "main", "namaUjian", soal.getNamaUjian());
        client.insertRecord(table, rowKey, "main", "pertanyaan", soal.getPertanyaan());
        client.insertRecord(table, rowKey, "main", "bobot", soal.getBobot());
        client.insertRecord(table, rowKey, "main", "jenisSoal", soal.getJenisSoal());
        client.insertRecord(table, rowKey, "main", "createdAt", soal.getCreatedAt().toString());
    }

    private void saveRelationships(HBaseCustomClient client, TableName table, String rowKey, SoalUjian soal) {
        if (soal.getUser() != null) {
            client.insertRecord(table, rowKey, "user", "id", soal.getUser().getId());
            client.insertRecord(table, rowKey, "user", "name", soal.getUser().getName());
        }

        if (soal.getTaksonomi() != null) {
            client.insertRecord(table, rowKey, "taksonomi", "idTaksonomi", soal.getTaksonomi().getIdTaksonomi());
            client.insertRecord(table, rowKey, "taksonomi", "namaTaksonomi", soal.getTaksonomi().getNamaTaksonomi());
        }

        if (soal.getAtp() != null) {
            client.insertRecord(table, rowKey, "atp", "idAtp", soal.getAtp().getIdAtp());
            client.insertRecord(table, rowKey, "atp", "namaAtp", soal.getAtp().getNamaAtp());
        }

        if (soal.getSchool() != null) {
            client.insertRecord(table, rowKey, "school", "idSchool", soal.getSchool().getIdSchool());
            client.insertRecord(table, rowKey, "school", "nameSchool", soal.getSchool().getNameSchool());
        }
    }

    private void savePilihanGanda(HBaseCustomClient client, TableName table, String rowKey, SoalUjian soal) {
        try {
            // Simpan opsi sebagai JSON
            String opsiJson = objectMapper.writeValueAsString(soal.getOpsi());
            client.insertRecord(table, rowKey, "main", "opsi", opsiJson);

            // Simpan jawaban benar sebagai JSON
            String jawabanJson = objectMapper.writeValueAsString(soal.getJawabanBenar());
            client.insertRecord(table, rowKey, "main", "jawabanBenar", jawabanJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize options or answers", e);
        }
    }

    private void saveCocokkan(HBaseCustomClient client, TableName table, String rowKey, SoalUjian soal) {
        try {
            // Simpan pasangan sebagai JSON
            String pasanganJson = objectMapper.writeValueAsString(soal.getPasangan());
            client.insertRecord(table, rowKey, "main", "pasangan", pasanganJson);

            // Simpan jawaban benar sebagai JSON
            String jawabanJson = objectMapper.writeValueAsString(soal.getJawabanBenar());
            client.insertRecord(table, rowKey, "main", "jawabanBenar", jawabanJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize matching pairs or answers", e);
        }
    }

    private void saveIsian(HBaseCustomClient client, TableName table, String rowKey, SoalUjian soal) {
        try {
            // Simpan jawaban benar sebagai JSON
            String jawabanJson = objectMapper.writeValueAsString(soal.getJawabanBenar());
            client.insertRecord(table, rowKey, "main", "jawabanBenar", jawabanJson);

            // Simpan toleransi typo
            client.insertRecord(table, rowKey, "main", "toleransiTypo",
                    String.valueOf(soal.getToleransiTypo()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize answers", e);
        }
    }

    public SoalUjian findById(String soalUjianId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableSoalUjian = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("idSoalUjian", "idSoalUjian");
        columnMapping.put("namaUjian", "namaUjian");
        columnMapping.put("pertanyaan", "pertanyaan");
        columnMapping.put("bobot", "bobot");
        columnMapping.put("jenisSoal", "jenisSoal");
        columnMapping.put("opsi", "opsi");
        columnMapping.put("pasangan", "pasangan");
        columnMapping.put("toleransiTypo", "toleransiTypo");
        columnMapping.put("jawabanBenar", "jawabanBenar");
        columnMapping.put("createdAt", "createdAt");
        columnMapping.put("user", "user");
        columnMapping.put("taksonomi", "taksonomi");
        columnMapping.put("atp", "atp");
        columnMapping.put("school", "school");

        return client.showDataTable(tableSoalUjian.toString(), columnMapping, soalUjianId, SoalUjian.class);
    }

    public List<SoalUjian> findSoalUjianBySekolah(String schoolID, int size) throws IOException {
        TableName tableSoalUjian = TableName.valueOf(tableName);
        HBaseCustomClient client = new HBaseCustomClient(conf);

        // Standard column mappings
        Map<String, String> columnMapping = new HashMap<>();
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
        columnMapping.put("user", "user");
        columnMapping.put("taksonomi", "taksonomi");
        columnMapping.put("atp", "atp");
        columnMapping.put("school", "school");

        // Define indexed fields with their types
        Map<String, String> indexedFields = new HashMap<>();
        indexedFields.put("opsi", "MAP");
        indexedFields.put("pasangan", "MAP");
        indexedFields.put("jawabanBenar", "LIST");

        List<SoalUjian> soalUjianList = client.getDataListByColumnIndeks(
                tableSoalUjian.toString(),
                columnMapping,
                "school",
                "idSchool",
                schoolID,
                SoalUjian.class,
                size,
                indexedFields);

        return soalUjianList;
    }

    public SoalUjian update(String soalUjianId, SoalUjian soalUjian) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableSoalUjian = TableName.valueOf(tableName);
        String tableNameStr = tableSoalUjian.toString();

        // First delete all existing detail records to avoid orphaned data
        deleteExistingDetails(client, tableNameStr, soalUjianId);

        // Update main info
        updateMainInfo(client, tableSoalUjian, soalUjianId, soalUjian);

        // Update relationships
        updateRelationships(client, tableSoalUjian, soalUjianId, soalUjian);

        // Save question details based on type
        switch (soalUjian.getJenisSoal().toUpperCase()) {
            case "PG":
            case "MULTI":
                savePilihanGanda(client, tableSoalUjian, soalUjianId, soalUjian);
                break;

            case "COCOK":
                saveCocokkan(client, tableSoalUjian, soalUjianId, soalUjian);
                break;

            case "ISIAN":
                saveIsian(client, tableSoalUjian, soalUjianId, soalUjian);
                break;
        }

        return soalUjian;
    }

    private void deleteExistingDetails(HBaseCustomClient client, String tableName, String rowKey) throws IOException {
        // Get all existing detail columns
        List<String> detailColumns = client.getColumns(tableName, rowKey, "detail");

        // Delete each detail column
        for (String column : detailColumns) {
            client.deleteRecordByColumn(tableName, rowKey, "detail", column);
        }
    }

    private void updateMainInfo(HBaseCustomClient client, TableName table, String rowKey, SoalUjian soal)
            throws IOException {
        if (soal.getNamaUjian() != null) {
            client.insertRecord(table, rowKey, "main", "namaUjian", soal.getNamaUjian());
        }
        if (soal.getPertanyaan() != null) {
            client.insertRecord(table, rowKey, "main", "pertanyaan", soal.getPertanyaan());
        }
        if (soal.getBobot() != null) {
            client.insertRecord(table, rowKey, "main", "bobot", soal.getBobot());
        }
        if (soal.getJenisSoal() != null) {
            client.insertRecord(table, rowKey, "main", "jenisSoal", soal.getJenisSoal());
        }
    }

    private void updateRelationships(HBaseCustomClient client, TableName table, String rowKey, SoalUjian soal)
            throws IOException {
        if (soal.getUser() != null) {
            client.insertRecord(table, rowKey, "user", "id", soal.getUser().getId());
            client.insertRecord(table, rowKey, "user", "name", soal.getUser().getName());
        }

        if (soal.getTaksonomi() != null) {
            client.insertRecord(table, rowKey, "taksonomi", "idTaksonomi", soal.getTaksonomi().getIdTaksonomi());
            client.insertRecord(table, rowKey, "taksonomi", "namaTaksonomi", soal.getTaksonomi().getNamaTaksonomi());
        }

        if (soal.getAtp() != null) {
            client.insertRecord(table, rowKey, "atp", "idAtp", soal.getAtp().getIdAtp());
            client.insertRecord(table, rowKey, "atp", "namaAtp", soal.getAtp().getNamaAtp());
            if (soal.getAtp().getElemen() != null) {
                client.insertRecord(table, rowKey, "atp", "idElemen", soal.getAtp().getElemen().getIdElemen());
                client.insertRecord(table, rowKey, "atp", "namaElemen", soal.getAtp().getElemen().getNamaElemen());
            }
        }

        if (soal.getSchool() != null) {
            client.insertRecord(table, rowKey, "school", "idSchool", soal.getSchool().getIdSchool());
            client.insertRecord(table, rowKey, "school", "nameSchool", soal.getSchool().getNameSchool());
        }
    }

    public boolean deleteById(String soalUjianId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        client.deleteRecord(tableName, soalUjianId);
        return true;
    }

    public boolean existsById(String soalUjianId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableSoalUjian = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();
        columnMapping.put("idSoalUjian", "idSoalUjian");

        SoalUjian soalUjian = client.getDataByColumn(tableSoalUjian.toString(), columnMapping,
                "main", "idSoalUjian",
                soalUjianId, SoalUjian.class);

        return soalUjian.getIdSoalUjian() != null;
    }
}
