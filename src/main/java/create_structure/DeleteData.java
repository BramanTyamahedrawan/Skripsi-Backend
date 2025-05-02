package create_structure;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;

import java.io.IOException;

public class DeleteData {

    public static void main(String[] args) throws IOException {
        Configuration conf = HBaseConfiguration.create();
        HBaseCustomClient client = new HBaseCustomClient(conf);

        // Hanya menghapus data kelahiran
        TableName tableModul = TableName.valueOf("modul");
        client.truncateTable(tableModul, conf);
    }
}
