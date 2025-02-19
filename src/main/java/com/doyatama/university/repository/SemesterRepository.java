package com.doyatama.university.repository;

import com.doyatama.university.helper.HBaseCustomClient;
import com.doyatama.university.model.Semester;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.springframework.stereotype.Repository;

public class SemesterRepository {
    Configuration conf = HBaseConfiguration.create();
    String tableName = "semester";

    public List<Semester> findAll(int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableSemester = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("idSemester", "idSemester");
        columnMapping.put("namaSemester", "namaSemester");
        return client.showListTable(tableSemester.toString(), columnMapping, Semester.class, size);
    }

    public Semester save(Semester semester) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        String rowKey = semester.getIdSemester();
        TableName tableSemester = TableName.valueOf(tableName);
        client.insertRecord(tableSemester, rowKey, "main", "idSemester", rowKey);
        client.insertRecord(tableSemester, rowKey, "main", "namaSemester", semester.getNamaSemester());
        client.insertRecord(tableSemester, rowKey, "detail", "created_by", "Doyatama");
        return semester;
    }

    public Semester findById(String semesterId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableSemester = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("idSemester", "idSemester");
        columnMapping.put("namaSemester", "namaSemester");

        return client.showDataTable(tableSemester.toString(), columnMapping, semesterId, Semester.class);
    }

    public List<Semester> findAllById(List<String> semesterIds) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableSemester = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("idSemester", "idSemester");
        columnMapping.put("namaSemester", "namaSemester");

        List<Semester> semesterList = new ArrayList<>();
        for (String semesterId : semesterIds) {
            Semester semester = client.showDataTable(tableSemester.toString(), columnMapping, semesterId,
                    Semester.class);
            semesterList.add(semester);
        }
        return semesterList;
    }

    public Semester update(String semesterId, Semester semester) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableSemester = TableName.valueOf(tableName);
        client.insertRecord(tableSemester, semesterId, "main", "namaSemester", semester.getNamaSemester());
        return semester;
    }

    public boolean deleteById(String semesterId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        client.deleteRecord(tableName, semesterId);
        return true;
    }

    public boolean existById(String semesterId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableSemester = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();
        columnMapping.put("idSemester", "idSemester");

        Semester semester = client.getDataByColumn(tableSemester.toString(), columnMapping, "main", "idSemester",
                semesterId, Semester.class);
        return semester.getIdSemester() != null;
    }

}
