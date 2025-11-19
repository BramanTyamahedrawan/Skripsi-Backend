package create_structure;

import java.time.Instant;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class SeederUser {

        public static void main(String[] args) throws IOException {

                Configuration conf = HBaseConfiguration.create();
                HBaseCustomClient client = new HBaseCustomClient(conf);

                // Waktu sekarang
                ZoneId zoneId = ZoneId.of("Asia/Jakarta");
                ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId);
                Instant instant = zonedDateTime.toInstant();

                // Tabel Elemen
                TableName tableUser = TableName.valueOf("users");
                // Insert Users
                // Insert Users
                client.insertRecord(tableUser, "DEV001", "main", "id", "DEV001");
                client.insertRecord(tableUser, "DEV001", "main", "email", "developer_1@gmail.com");
                client.insertRecord(tableUser, "DEV001", "main", "name", "Developer 1");
                client.insertRecord(tableUser, "DEV001", "main", "username", "developer_hadoop");
                client.insertRecord(tableUser, "DEV001", "main", "password",
                                "$2a$10$SDRWMUk.2fnli0GTmqodJexjRksTw0En98dU8fdKsw7nTbZzMrj.2"); // password
                client.insertRecord(tableUser, "DEV001", "main", "roles", "1");
                client.insertRecord(tableUser, "DEV001", "main", "created_at", instant.toString());
                client.insertRecord(tableUser, "DEV001", "detail", "created_by", "Braman");

                client.insertRecord(tableUser, "DEV002", "main", "id", "DEV002");
                client.insertRecord(tableUser, "DEV002", "main", "email", "developer_2@gmail.com");
                client.insertRecord(tableUser, "DEV002", "main", "name", "Developer 2");
                client.insertRecord(tableUser, "DEV002", "main", "username", "developer_hbase");
                client.insertRecord(tableUser, "DEV002", "main", "password",
                                "$2a$10$SDRWMUk.2fnli0GTmqodJexjRksTw0En98dU8fdKsw7nTbZzMrj.2"); // password
                client.insertRecord(tableUser, "DEV002", "main", "roles", "1");
                client.insertRecord(tableUser, "DEV002", "main", "created_at", instant.toString());
                client.insertRecord(tableUser, "DEV002", "detail", "created_by", "Braman");

                client.insertRecord(tableUser, "DEV003", "main", "id", "DEV003");
                client.insertRecord(tableUser, "DEV003", "main", "email", "developer_3@gmail.com");
                client.insertRecord(tableUser, "DEV003", "main", "name", "Developer 3");
                client.insertRecord(tableUser, "DEV003", "main", "username", "developer_zookeeper");
                client.insertRecord(tableUser, "DEV003", "main", "password",
                                "$2a$10$SDRWMUk.2fnli0GTmqodJexjRksTw0En98dU8fdKsw7nTbZzMrj.2"); // password
                client.insertRecord(tableUser, "DEV003", "main", "roles", "1");
                client.insertRecord(tableUser, "DEV003", "main", "created_at", instant.toString());
                client.insertRecord(tableUser, "DEV003", "detail", "created_by", "Braman");

        }
}
