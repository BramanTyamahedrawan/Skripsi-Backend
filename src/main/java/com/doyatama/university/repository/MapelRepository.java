/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.doyatama.university.repository;

import com.doyatama.university.helper.HBaseCustomClient;
import com.doyatama.university.model.Mapel;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.springframework.stereotype.Repository;

/**
 *
 * @author senja
 */
@Repository
public class MapelRepository {
    Configuration conf = HBaseConfiguration.create();
    String tableName = "mapels";

    public List<Mapel> findAll(int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableMapel = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("idMapel", "idMapel");
        columnMapping.put("name", "name");
        columnMapping.put("school", "school");

        return client.showListTable(tableMapel.toString(), columnMapping, Mapel.class, size);
    }

    public Mapel save(Mapel mapel) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        String rowKey = mapel.getIdMapel();
        TableName tableMapel = TableName.valueOf(tableName);
        client.insertRecord(tableMapel, rowKey, "main", "idMapel", rowKey);
        client.insertRecord(tableMapel, rowKey, "main", "name", mapel.getName());

        // Sekolah
        client.insertRecord(tableMapel, rowKey, "school", "idSchool", mapel.getSchool().getIdSchool());
        client.insertRecord(tableMapel, rowKey, "school", "nameSchool", mapel.getSchool().getNameSchool());

        client.insertRecord(tableMapel, rowKey, "detail", "created_by", "Doyatama");
        return mapel;
    }

    public Mapel findById(String mapelId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableMapel = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("idMapel", "idMapel");
        columnMapping.put("name", "name");
        columnMapping.put("school", "school");

        return client.showDataTable(tableMapel.toString(), columnMapping, mapelId, Mapel.class);
    }

    public List<Mapel> findAllById(List<String> mapelIds) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableMapel = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();
        columnMapping.put("idMapel", "idMapel");
        columnMapping.put("name", "name");

        List<Mapel> mapels = new ArrayList<>();
        for (String mapelId : mapelIds) {
            Mapel mapel = client.showDataTable(tableMapel.toString(), columnMapping, mapelId, Mapel.class);
            if (mapel != null) {
                mapels.add(mapel);
            }
        }

        return mapels;
    }

    public List<Mapel> findAllByIds(List<List<String>> mapelIdsList) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableMapel = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();
        columnMapping.put("idMapel", "idMapel");
        columnMapping.put("name", "name");

        List<Mapel> mapels = new ArrayList<>();

        // Iterate through each List<String> inside List<List<String>>
        for (List<String> mapelIds : mapelIdsList) {
            for (String mapelId : mapelIds) {
                Mapel mapel = client.showDataTable(tableMapel.toString(), columnMapping, mapelId, Mapel.class);
                if (mapel != null) {
                    mapels.add(mapel);
                }
            }
        }

        return mapels;
    }

    public List<Mapel> findMapelBySekolah(String schoolID, int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableMapel = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("idMapel", "idMapel");
        columnMapping.put("name", "name");
        columnMapping.put("school", "school");

        List<Mapel> mapels = client.getDataListByColumn(tableMapel.toString(), columnMapping, "school", "idSchool",
                schoolID, Mapel.class, size);
        return mapels;
    }

    public Mapel update(String mapelId, Mapel mapel) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableMapel = TableName.valueOf(tableName);

        if (mapel.getName() != null) {
            client.insertRecord(tableMapel, mapelId, "main", "name", mapel.getName());
        }

        if (mapel.getSchool().getIdSchool() != null) {
            client.insertRecord(tableMapel, mapelId, "school", "idSchool", mapel.getSchool().getIdSchool());
        }

        if (mapel.getSchool().getNameSchool() != null) {
            client.insertRecord(tableMapel, mapelId, "school", "nameSchool", mapel.getSchool().getNameSchool());
        }

        return mapel;
    }

    public boolean deleteById(String mapelId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        client.deleteRecord(tableName, mapelId);
        return true;
    }

    public boolean existsById(String mapelId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableMapel = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("idMapel", "idMapel");

        Mapel mapel = client.getDataByColumn(tableMapel.toString(), columnMapping, "main", "idMapel", mapelId,
                Mapel.class);
        return mapel.getIdMapel() != null;
    }
}
