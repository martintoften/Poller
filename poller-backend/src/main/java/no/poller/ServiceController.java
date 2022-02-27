package no.poller;

import io.vertx.ext.web.RoutingContext;

public interface ServiceController {
    void handleGet(final RoutingContext context);
    void handlePost(final RoutingContext context);
    void handleDelete(final RoutingContext context);
}
