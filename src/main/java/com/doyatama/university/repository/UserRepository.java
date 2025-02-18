package com.doyatama.university.repository;

import com.doyatama.university.helper.HBaseCustomClient;
import com.doyatama.university.model.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.*;

@Repository
public class UserRepository {
    Configuration conf = HBaseConfiguration.create();
    String tableName = "users";

    public List<User> findAll(int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableUsers = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("name", "name");
        columnMapping.put("username", "username");
        columnMapping.put("email", "email");
        columnMapping.put("password", "password");
        columnMapping.put("school", "school");
        columnMapping.put("roles", "roles");
        return client.showListTable(tableUsers.toString(), columnMapping, User.class, size);
    }

    public List<User> findUsersNotUsedInLectures(int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableUsers = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("name", "name");
        columnMapping.put("username", "username");
        columnMapping.put("email", "email");
        columnMapping.put("password", "password");
        columnMapping.put("school", "school");
        columnMapping.put("roles", "roles");

        // Get the list of all users
        List<User> allUsers = client.showListTable(tableUsers.toString(), columnMapping, User.class, size);

        // Get the list of all user IDs that have been used in lectures
        Set<String> userIdsInLectures = new HashSet<>();
        Scan scan = new Scan();
        ResultScanner scanner = client.getTable("lectures").getScanner(scan);
        for (Result result : scanner) {
            byte[] userIdBytes = result.getValue(Bytes.toBytes("user"), Bytes.toBytes("id"));
            if (userIdBytes != null) {
                String userId = Bytes.toString(userIdBytes);
                userIdsInLectures.add(userId);
            }
        }
        scanner.close();

        // Find all users that have not been used in any lectures
        List<User> unusedUsers = new ArrayList<>();
        for (User user : allUsers) {
            if (!userIdsInLectures.contains(user.getId())) {
                unusedUsers.add(user);
            }
        }

        return unusedUsers;
    }

    public User findByUsername(String username) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableUsers = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("name", "name");
        columnMapping.put("username", "username");
        columnMapping.put("email", "email");
        columnMapping.put("password", "password");
        columnMapping.put("school", "school");
        columnMapping.put("roles", "roles");

        return client.getDataByColumn(tableUsers.toString(), columnMapping, "main", "username", username, User.class);
    }

    public User findById(String id) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableUsers = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("name", "name");
        columnMapping.put("username", "username");
        columnMapping.put("email", "email");
        columnMapping.put("password", "password");
        columnMapping.put("school", "school");
        columnMapping.put("roles", "roles");

        return client.showDataTable(tableUsers.toString(), columnMapping, id, User.class);
    }

    public boolean existsByUsername(String username) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableUsers = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("name", "name");
        columnMapping.put("username", "username");
        columnMapping.put("email", "email");
        columnMapping.put("password", "password");
        columnMapping.put("school", "school");
        columnMapping.put("roles", "roles");

        User user = client.getDataByColumn(tableUsers.toString(), columnMapping, "main", "username", username, User.class);
        if(user.getUsername() != null){
            return true;
        }else{
            return false;
        }
    }

    public boolean existsByEmail(String email) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableUsers = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("name", "name");
        columnMapping.put("username", "username");
        columnMapping.put("email", "email");
        columnMapping.put("password", "password");
        columnMapping.put("school", "school");
        columnMapping.put("roles", "roles");

        User user = client.getDataByColumn(tableUsers.toString(), columnMapping, "main", "email", email, User.class);
        if(user.getEmail() != null){
            return true;
        }else{
            return false;
        }
    }

    public User save(User user) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        String rowKey = UUID.randomUUID().toString();

        TableName tableUsers = TableName.valueOf(tableName);
        client.insertRecord(tableUsers, rowKey, "main", "id", rowKey);
        client.insertRecord(tableUsers, rowKey, "main", "name", user.getName());
        client.insertRecord(tableUsers, rowKey, "main", "username", user.getUsername());
        client.insertRecord(tableUsers, rowKey, "main", "email", user.getEmail());
        client.insertRecord(tableUsers, rowKey, "main", "password", user.getPassword());
        if (user.getSchool() != null && user.getSchool().getId() != null) {
        client.insertRecord(tableUsers, rowKey, "school", "id", user.getSchool().getId());
        client.insertRecord(tableUsers, rowKey, "school", "name", user.getSchool().getName());
        } else {
            System.out.println("School is null or has no ID");
        }
        client.insertRecord(tableUsers, rowKey, "main", "roles", user.getRoles());
        client.insertRecord(tableUsers, rowKey, "main", "created_at", user.getCreatedAt().toString());
        return user;
    }
}