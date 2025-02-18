package create_structure;

import com.github.javafaker.Faker;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;


public class HBaseClientStructure1 {

    public static void main(String[] args) throws IOException {

        Configuration conf = HBaseConfiguration.create();
        HBaseCustomClient client = new HBaseCustomClient(conf);

        // ==============================================================================================
        // CREATE COLLECTION
        // ==============================================================================================
        
       
   // Create Tabel Season
        TableName tableSeason = TableName.valueOf("seasons");
        String[] season = { "main", "bidangKeahlian", "programKeahlian", "konsentrasiKeahlian", "kelas", "tahunAjaran", "lecture", "student", "jadwalPelajaran", "detail" };
        client.deleteTable(tableSeason);
        client.createTable(tableSeason, season);

//        
//        // Create Table Bidang Keahlian
//        TableName tableBidangKeahlian = TableName.valueOf("bidangKeahlians");
//        String[] bidangKeahlian = { "main", "school", "detail" };
//        client.deleteTable(tableBidangKeahlian);
//        client.createTable(tableBidangKeahlian, bidangKeahlian);
        
        
//         TableName tableProgramKeahlian = TableName.valueOf("programKeahlians");
//        String[] programKeahlian = { "main","bidangKeahlian", "detail" };
//        client.deleteTable(tableProgramKeahlian);
//        client.createTable(tableProgramKeahlian, programKeahlian);

// TableName tableJadwal = TableName.valueOf("jadwalPelajarans");
//        String[] jadwal = { "main", "lecture", "mapel", "detail" };
//        client.deleteTable(tableJadwal);
//        client.createTable(tableJadwal, jadwal);
        
    }
}