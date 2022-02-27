package no.poller;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import no.poller.database.ServiceDAO;
import no.poller.mapper.ServiceExceptionMapper;
import no.poller.mapper.ServiceMapper;
import no.poller.model.Service;
import no.poller.model.ServiceError;

import java.net.MalformedURLException;

public class ServiceControllerImpl implements ServiceController {

    private final ServiceDAO serviceDAO;
    private final ServiceMapper serviceMapper;

    public ServiceControllerImpl(final ServiceDAO serviceDAO, final ServiceMapper serviceMapper) {
        this.serviceDAO = serviceDAO;
        this.serviceMapper = serviceMapper;
    }

    @Override
    public void handleGet(final RoutingContext context) {
        serviceDAO.getAll()
            .map(serviceMapper::toJson)
            .onSuccess(servicesAsJson -> {
                context.response()
                    .putHeader("content-type", "application/json")
                    .setStatusCode(200)
                    .end(servicesAsJson.toBuffer());
            })
            .onFailure(cause -> {
                final ServiceError error = ServiceExceptionMapper.map(cause);
                context.response()
                    .setStatusCode(error.getCode())
                    .end(error.getMessage());
            });
    }

    @Override
    public void handlePost(final RoutingContext context) {
        final JsonObject body = context.getBodyAsJson();
        try {
            final Service service = serviceMapper.toService(body);
            serviceDAO.insert(service)
                .onSuccess(result -> {
                    context.response()
                        .setStatusCode(204).end();
                })
                .onFailure(cause -> {
                    final ServiceError error = ServiceExceptionMapper.map(cause);
                    context.response()
                        .setStatusCode(error.getCode())
                        .end(error.getMessage());
                });
        } catch (IllegalArgumentException | MalformedURLException error) {
            context.response()
                .setStatusCode(400)
                .end();
        }
    }

    @Override
    public void handleDelete(final RoutingContext context) {
        final String serviceName = context.queryParam("name").get(0);
        serviceDAO.delete(serviceName)
            .onSuccess(result -> {
                context.response()
                    .setStatusCode(204)
                    .end();
            })
            .onFailure(cause -> {
                final ServiceError error = ServiceExceptionMapper.map(cause);
                context.response()
                    .setStatusCode(error.getCode())
                    .end(error.getMessage());
            });
    }
}
