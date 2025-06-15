package create_structure;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;

import java.io.IOException;

public class DeleteData {

    public static void main(String[] args) throws IOException {
        Configuration conf = HBaseConfiguration.create();
        HBaseCustomClient client = new HBaseCustomClient(conf);

        // TableName tableMapels = TableName.valueOf("mapels");
        // client.truncateTable(tableMapels, conf);

        // TableName tableElemen = TableName.valueOf("elemen");
        // client.truncateTable(tableElemen, conf);
        //
        // TableName tableAcp = TableName.valueOf("acp");
        // client.truncateTable(tableAcp, conf);
        //
        // TableName tableAtp = TableName.valueOf("atp");
        // client.truncateTable(tableAtp, conf);

        // TableName tableSoalUjian = TableName.valueOf("soalUjian");
        // client.truncateTable(tableSoalUjian, conf);

        TableName tableUjian = TableName.valueOf("ujian");
        client.truncateTable(tableUjian, conf);

        TableName tableHasilUjian = TableName.valueOf("hasil_ujian");
        client.truncateTable(tableHasilUjian, conf);

        // Create Table UjianSession
        TableName tableUjianSession = TableName.valueOf("ujian_session");
        client.truncateTable(tableUjianSession, conf);

        // Create Table CheatDetection
        TableName tableCheatDetection = TableName.valueOf("cheat_detection");
        client.truncateTable(tableCheatDetection, conf);

        // Create Table UjianAnalysis
        TableName tableUjianAnalysis = TableName.valueOf("ujian_analysis");
        client.truncateTable(tableUjianAnalysis, conf);
    }
}
