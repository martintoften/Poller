package no.poller.mapper;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import no.poller.model.Service;
import no.poller.model.ServiceState;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ServiceMapper {

    private static final String NAME = "name";
    private static final String URL = "url";
    private static final String CREATED_AT = "createdAt";
    private static final String STATE = "state";

    public Service toService(final JsonObject body) throws MalformedURLException, IllegalArgumentException {
        final String name = body.getString(NAME);
        final String url = body.getString(URL);
        if (name == null || url == null) throw new IllegalArgumentException("Missing parameters");
        final String parsedUrl = new URL(url).toString();
        return new Service(name, parsedUrl, LocalDateTime.now(), ServiceState.UNKNOWN);
    }

    public List<Service> toService(final RowSet<Row> databaseResult) {
        final List<Service> services = new ArrayList<>();
        for (Row row : databaseResult) {
            final String name = row.getString(NAME);
            final String url = row.getString(URL);
            final LocalDateTime createdAt = row.getLocalDateTime(CREATED_AT);
            final String state = row.getString(STATE);
            services.add(new Service(name, url, createdAt, mapState(state)));
        }
        return services;
    }

    public JsonArray toJson(final RowSet<Row> databaseResult) {
        final JsonArray services = new JsonArray();
        for (Row row : databaseResult) {
            final String name = row.getString(NAME);
            final String url = row.getString(URL);
            final LocalDateTime createdAt = row.getLocalDateTime(CREATED_AT);
            final String state = row.getString(STATE);
            final JsonObject service = new JsonObject()
                .put(NAME, name)
                .put(URL, url)
                .put(CREATED_AT, createdAt.toString())
                .put(STATE, state);
            services.add(service);
        }
        return services;
    }

    private ServiceState mapState(final String value) {
        try {
            return ServiceState.valueOf(value);
        } catch (IllegalArgumentException error) {
            return ServiceState.UNKNOWN;
        }
    }
}
