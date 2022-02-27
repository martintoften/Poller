package no.poller;

import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import no.poller.database.ServiceDAO;
import no.poller.mapper.ServiceMapper;
import no.poller.model.Service;
import no.poller.model.ServiceState;

import java.util.List;

public class ServicePollerImpl implements ServicePoller {

    private final WebClient client;
    private final ServiceDAO serviceDAO;
    private final ServiceMapper serviceMapper;

    public ServicePollerImpl(final Vertx vertx, final ServiceDAO serviceDAO, final ServiceMapper serviceMapper) {
        this.client = WebClient.create(vertx);
        this.serviceDAO = serviceDAO;
        this.serviceMapper = serviceMapper;
    }

    @Override
    public void startPolling() {
        serviceDAO.getAll()
            .map(serviceMapper::toService)
            .onSuccess(serviceResult -> {
                serviceResult.forEach(service -> client.getAbs(service.getUrl()).send(requestResult -> {
                    if (requestResult.result().statusCode() == 200) {
                        serviceDAO.updateState(service.getName(), ServiceState.OK);
                    } else {
                        serviceDAO.updateState(service.getName(), ServiceState.FAIL);
                    }
                }));
            });
    }
}

