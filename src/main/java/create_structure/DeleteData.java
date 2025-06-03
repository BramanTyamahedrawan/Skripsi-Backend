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
        TableName tableMapels = TableName.valueOf("mapels");
        client.truncateTable(tableMapels, conf);

        TableName tableElemen = TableName.valueOf("elemen");
        client.truncateTable(tableElemen, conf);

        TableName tableAcp = TableName.valueOf("acp");
        client.truncateTable(tableAcp, conf);

        TableName tableAtp = TableName.valueOf("atp");
        client.truncateTable(tableAtp, conf);
    }
}
