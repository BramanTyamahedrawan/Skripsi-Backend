package com.doyatama.university.repository;

import com.doyatama.university.helper.HBaseCustomClient;
import com.doyatama.university.model.Acp;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;

public class AcpRepository {
    Configuration conf = HBaseConfiguration.create();
    String tableName = "acp";

    public List<Acp> findAll(int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableAcp = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("idAcp", "idAcp");
        columnMapping.put("namaAcp", "namaAcp");
        columnMapping.put("mapel", "mapel");
        columnMapping.put("tahunAjaran", "tahunAjaran");
        columnMapping.put("semester", "semester");
        columnMapping.put("kelas", "kelas");
        columnMapping.put("konsentrasiKeahlian", "konsentrasiKeahlian");
        columnMapping.put("elemen", "elemen");

        return client.showListTable(tableAcp.toString(), columnMapping, Acp.class, size);
    }

    public Acp save(Acp acp) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        String rowKey = acp.getIdAcp();
        TableName tableAcp = TableName.valueOf(tableName);
        // Map<String, String> columnMapping = new HashMap<>();

        client.insertRecord(tableAcp, rowKey, "main", "idAcp", acp.getIdAcp());
        client.insertRecord(tableAcp, rowKey, "main", "namaAcp", acp.getNamaAcp());

        // Tahun Ajaran
        client.insertRecord(tableAcp, rowKey, "tahunAjaran", "idTahun", acp.getTahunAjaran().getIdTahun());
        client.insertRecord(tableAcp, rowKey, "tahunAjaran", "tahunAjaran",
                acp.getTahunAjaran().getTahunAjaran());
        // Semester
        client.insertRecord(tableAcp, rowKey, "semester", "idSemester", acp.getSemester().getIdSemester());
        client.insertRecord(tableAcp, rowKey, "semester", "namaSemester",
                acp.getSemester().getNamaSemester());
        // Kelas
        client.insertRecord(tableAcp, rowKey, "kelas", "idKelas", acp.getKelas().getIdKelas());
        client.insertRecord(tableAcp, rowKey, "kelas", "namaKelas", acp.getKelas().getNamaKelas());
        // Mapel
        client.insertRecord(tableAcp, rowKey, "mapel", "idMapel", acp.getMapel().getIdMapel());
        client.insertRecord(tableAcp, rowKey, "mapel", "name", acp.getMapel().getName());
        // Konsentrasi Keahlian
        client.insertRecord(tableAcp, rowKey, "konsentrasiKeahlian", "id",
                acp.getKonsentrasiKeahlian().getId());
        client.insertRecord(tableAcp, rowKey, "konsentrasiKeahlian", "konsentrasi",
                acp.getKonsentrasiKeahlian().getKonsentrasi());
        // Elemen
        client.insertRecord(tableAcp, rowKey, "elemen", "idElemen", acp.getElemen().getIdElemen());
        client.insertRecord(tableAcp, rowKey, "elemen", "namaElemen", acp.getElemen().getNamaElemen());

        return acp;

    }

    public Acp findAcpById(String acpId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableAcp = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        columnMapping.put("idAcp", "idAcp");
        columnMapping.put("namaAcp", "namaAcp");
        columnMapping.put("mapel", "mapel");
        columnMapping.put("tahunAjaran", "tahunAjaran");
        columnMapping.put("semester", "semester");
        columnMapping.put("kelas", "kelas");
        columnMapping.put("konsentrasiKeahlian", "konsentrasiKeahlian");
        columnMapping.put("elemen", "elemen");

        return client.showDataTable(tableAcp.toString(), columnMapping, acpId, Acp.class);
    }

    public Acp findById(String acpId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableAcp = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        columnMapping.put("idAcp", "idAcp");
        columnMapping.put("namaAcp", "namaAcp");
        columnMapping.put("mapel", "mapel");
        columnMapping.put("tahunAjaran", "tahunAjaran");
        columnMapping.put("semester", "semester");
        columnMapping.put("kelas", "kelas");
        columnMapping.put("konsentrasiKeahlian", "konsentrasiKeahlian");
        columnMapping.put("elemen", "elemen");

        return client.showDataTable(tableAcp.toString(), columnMapping, acpId, Acp.class);
    }

    public List<Acp> findAcpByMapel(String mapelId, int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableAcp = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        columnMapping.put("idAcp", "idAcp");
        columnMapping.put("namaAcp", "namaAcp");
        columnMapping.put("mapel", "mapel");
        columnMapping.put("tahunAjaran", "tahunAjaran");
        columnMapping.put("semester", "semester");
        columnMapping.put("kelas", "kelas");
        columnMapping.put("konsentrasiKeahlian", "konsentrasiKeahlian");
        columnMapping.put("elemen", "elemen");

        List<Acp> acpList = client.getDataListByColumn(tableAcp.toString(), columnMapping, "mapel", "idMapel",
                mapelId, Acp.class, size);
        return acpList;
    }

    public Acp update(String acpId, Acp acp) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableAcp = TableName.valueOf(tableName);
        // Map<String, String> columnMapping = new HashMap<>();

        if (acp.getNamaAcp() != null) {
            client.insertRecord(tableAcp, acpId, "main", "namaAcp", acp.getNamaAcp());
        }

        // Tahun Ajaran
        if (acp.getTahunAjaran() != null) {
            client.insertRecord(tableAcp, acpId, "tahunAjaran", "idTahun", acp.getTahunAjaran().getIdTahun());
            client.insertRecord(tableAcp, acpId, "tahunAjaran", "tahunAjaran",
                    acp.getTahunAjaran().getTahunAjaran());
        }

        // Semester
        if (acp.getSemester() != null) {
            client.insertRecord(tableAcp, acpId, "semester", "idSemester", acp.getSemester().getIdSemester());
            client.insertRecord(tableAcp, acpId, "semester", "namaSemester",
                    acp.getSemester().getNamaSemester());
        }

        // Kelas
        if (acp.getKelas() != null) {
            client.insertRecord(tableAcp, acpId, "kelas", "idKelas", acp.getKelas().getIdKelas());
            client.insertRecord(tableAcp, acpId, "kelas", "namaKelas", acp.getKelas().getNamaKelas());
        }

        // Mapel
        if (acp.getMapel() != null) {
            client.insertRecord(tableAcp, acpId, "mapel", "idMapel", acp.getMapel().getIdMapel());
            client.insertRecord(tableAcp, acpId, "mapel", "name", acp.getMapel().getName());
        }

        // Konsentrasi Keahlian
        if (acp.getKonsentrasiKeahlian() != null) {
            client.insertRecord(tableAcp, acpId, "konsentrasiKeahlian", "id",
                    acp.getKonsentrasiKeahlian().getId());
            client.insertRecord(tableAcp, acpId, "konsentrasiKeahlian", "konsentrasi",
                    acp.getKonsentrasiKeahlian().getKonsentrasi());
        }

        // Elemen
        if (acp.getElemen() != null) {
            client.insertRecord(tableAcp, acpId, "elemen", "idElemen", acp.getElemen().getIdElemen());
            client.insertRecord(tableAcp, acpId, "elemen", "namaElemen", acp.getElemen().getNamaElemen());
        }

        client.insertRecord(tableAcp, acpId, "detail", "updated_by", "Polinema");

        return acp;
    }

    public boolean deleteById(String acpId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        client.deleteRecord(tableName, acpId);
        return true;
    }

    public boolean existsById(String acpId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableAcp = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();
        columnMapping.put("idAcp", "idAcp");

        Acp acp = client.getDataByColumn(tableAcp.toString(), columnMapping, "main", "idAcp", acpId, Acp.class);
        return acp.getIdAcp() != null;
    }
}
