package no.poller.database;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;

public class DatabaseHelper {

    public static MySQLPool setupDatabase(final Vertx vertx) {
        final MySQLConnectOptions connectOptions = new MySQLConnectOptions()
            .setPort(3306)
            .setHost("127.0.0.1")
            .setDatabase("poller")
            .setUser("dev")
            .setPassword("secret");
        final PoolOptions poolOptions = new PoolOptions();
        return MySQLPool.pool(vertx, connectOptions, poolOptions);
    }

    public static Future<Object> createServiceTabletIfNotCreated(final MySQLPool databaseClient) {
        return databaseClient.query(
            "CREATE TABLE IF NOT EXISTS service " +
                "(" +
                "name VARCHAR(40) NOT NULL PRIMARY KEY ," +
                "url VARCHAR(40) NOT NULL," +
                "createdAt DATETIME," +
                "state ENUM('OK', 'FAIL', 'UNKNOWN') NOT NULL" +
                ")"
        ).execute().mapEmpty();
    }
}
