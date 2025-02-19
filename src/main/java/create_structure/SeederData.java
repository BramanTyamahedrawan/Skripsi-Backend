package create_structure;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;

import java.io.IOException;
import java.net.URI;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

import com.github.javafaker.Faker;

public class SeederData {
    public static void main(String[] args) throws IOException {

        Configuration conf = HBaseConfiguration.create();
        HBaseCustomClient client = new HBaseCustomClient(conf);

        // Waktu sekarang
        ZoneId zoneId = ZoneId.of("Asia/Jakarta");
        ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId);
        Instant instant = zonedDateTime.toInstant();

        // Tabel bidang keahlian
        TableName tableBidangKeahlian = TableName.valueOf("bidangKeahlians");

        // ==============================================================================================
        // INSERT DATA
        // ==============================================================================================

        // Insert Data Table Bidang Keahlian
        client.insertRecord(tableBidangKeahlian, "BK001", "main", "id", "BK001");
        client.insertRecord(tableBidangKeahlian, "BK001", "main", "bidang", "Teknik Informatika");
        client.insertRecord(tableBidangKeahlian, "BK001", "school", "school_id", "RWK001");
        client.insertRecord(tableBidangKeahlian, "BK001", "detail", "created_at", instant.toString());
        client.insertRecord(tableBidangKeahlian, "BK001", "detail", "created_by", "Doyatama");
    }
}
