
package com.doyatama.university.repository;

import com.doyatama.university.helper.HBaseCustomClient;
import com.doyatama.university.model.Kelas;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.springframework.stereotype.Repository;


@Repository
public class KelasRepository {
     Configuration conf = HBaseConfiguration.create();
    String tableName = "kelas";
    
    public List<Kelas> findAll(int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableKelas = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("idKelas", "idKelas");
        columnMapping.put("namaKelas", "namaKelas");
        return client.showListTable(tableKelas.toString(), columnMapping, Kelas.class, size);
    }
     
    public Kelas save(Kelas kelas) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        String rowKey = kelas.getIdKelas();
        TableName tableKelas = TableName.valueOf(tableName);
        client.insertRecord(tableKelas, rowKey, "main", "idKelas", rowKey);
        client.insertRecord(tableKelas, rowKey, "main", "namaKelas", kelas.getNamaKelas());
        client.insertRecord(tableKelas, rowKey, "detail", "created_by", "Doyatama");
        return kelas;
    } 
     
    public Kelas findById(String kelasId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableKelas = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("idKelas", "idKelas");
        columnMapping.put("namaKelas", "namaKelas");

        return client.showDataTable(tableKelas.toString(), columnMapping, kelasId, Kelas.class);
    }
         
    public List<Kelas> findAllById(List<String> kelasIds) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableKelas = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();
        columnMapping.put("idKelas", "idKelas");
        columnMapping.put("namaKelas", "namaKelas");


        List<Kelas> kelass = new ArrayList<>();
        for (String kelasId : kelasIds) {
            Kelas kelas = client.showDataTable(tableKelas.toString(), columnMapping, kelasId, Kelas.class);
            if (kelas != null) {
                kelass.add(kelas);
            }
        }

        return kelass;
    }
             
    public Kelas update(String kelasId, Kelas kelas) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableKelas = TableName.valueOf(tableName);
        client.insertRecord(tableKelas, kelasId, "main", "namaKelas", kelas.getNamaKelas());
        client.insertRecord(tableKelas, kelasId, "detail", "created_by", "Doyatama");
        return kelas;
    }
        
        
    public boolean deleteById(String kelasId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        client.deleteRecord(tableName, kelasId);
        return true;
    }  
}
