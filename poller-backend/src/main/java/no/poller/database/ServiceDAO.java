package no.poller.database;

import io.vertx.core.Future;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import no.poller.model.Service;
import no.poller.model.ServiceState;

public interface ServiceDAO {
    Future<RowSet<Row>> getAll();
    Future<RowSet<Row>> insert(final Service service);
    Future<RowSet<Row>> updateState(final String name, final ServiceState state);
    Future<RowSet<Row>> delete(final String name);
}
