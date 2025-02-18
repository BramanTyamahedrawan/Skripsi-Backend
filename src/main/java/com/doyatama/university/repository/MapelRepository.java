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
        return client.showListTable(tableMapel.toString(), columnMapping, Mapel.class, size);
    }
     
    public Mapel save(Mapel mapel) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        String rowKey = mapel.getIdMapel();
        TableName tableMapel = TableName.valueOf(tableName);
        client.insertRecord(tableMapel, rowKey, "main", "idMapel", rowKey);
        client.insertRecord(tableMapel, rowKey, "main", "name", mapel.getName());
        client.insertRecord(tableMapel, rowKey, "detail", "created_by", "Doyatama");
        return mapel;
    } 
     
    public Mapel findById(String mplId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableMapel = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("idMapel", "idMapel");
        columnMapping.put("name", "name");

        return client.showDataTable(tableMapel.toString(), columnMapping, mplId, Mapel.class);
    }
         
    public List<Mapel> findAllById(List<String> mplIds) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableMapel = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();
        columnMapping.put("idMapel", "idMapel");
        columnMapping.put("name", "name");


        List<Mapel> mapels = new ArrayList<>();
        for (String mplId : mplIds) {
            Mapel mapel = client.showDataTable(tableMapel.toString(), columnMapping, mplId, Mapel.class);
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
            for (String mplId : mapelIds) {
                Mapel mapel = client.showDataTable(tableMapel.toString(), columnMapping, mplId, Mapel.class);
                if (mapel != null) {
                    mapels.add(mapel);
                }
            }
        }

        return mapels;
    }

             
    public Mapel update(String mplId, Mapel mapel) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableMapel = TableName.valueOf(tableName);
        client.insertRecord(tableMapel, mplId, "main", "name", mapel.getName());
        client.insertRecord(tableMapel, mplId, "detail", "created_by", "Doyatama");
        return mapel;
    }
        
        
    public boolean deleteById(String mplId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        client.deleteRecord(tableName, mplId);
        return true;
    }  
}
