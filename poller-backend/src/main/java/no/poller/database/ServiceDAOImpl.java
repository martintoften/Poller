package no.poller.database;

import io.vertx.core.Future;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.*;
import no.poller.model.Service;
import no.poller.model.ServiceState;

public class ServiceDAOImpl implements ServiceDAO {

    private final MySQLPool databaseClient;

    public ServiceDAOImpl(final MySQLPool database) {
        this.databaseClient = database;
    }

    public Future<RowSet<Row>> getAll() {
        return databaseClient.query("SELECT * FROM service").execute();
    }

    @Override
    public Future<RowSet<Row>> insert(final Service service) {
        return databaseClient
            .preparedQuery("INSERT INTO service (name, url, createdAt, state) VALUES(?,?,?,?)")
            .execute(Tuple.of(service.getName(), service.getUrl(), service.getTimestamp(), service.getState()));
    }

    @Override
    public Future<RowSet<Row>> updateState(final String name, final ServiceState state) {
        return databaseClient
            .preparedQuery("UPDATE service set state = ? WHERE name = ?")
            .execute(Tuple.of(state, name));
    }

    @Override
    public Future<RowSet<Row>> delete(final String name) {
        return databaseClient
            .preparedQuery("DELETE FROM service WHERE name = ?")
            .execute(Tuple.of(name));
    }
}
