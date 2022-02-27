package no.poller;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.mysqlclient.MySQLPool;
import no.poller.database.DatabaseHelper;
import no.poller.database.ServiceDAO;
import no.poller.database.ServiceDAOImpl;
import no.poller.mapper.ServiceMapper;

public class MainVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        final MySQLPool databaseClient = DatabaseHelper.setupDatabase(vertx);
        final ServiceDAO serviceDAO = new ServiceDAOImpl(databaseClient);
        final ServiceController serviceController = new ServiceControllerImpl(serviceDAO, new ServiceMapper());
        final ServicePoller poller = new ServicePollerImpl(vertx, serviceDAO, new ServiceMapper());

        DatabaseHelper
            .createServiceTabletIfNotCreated(databaseClient)
            .onSuccess(v -> {
                final HttpServer server = vertx.createHttpServer();
                final Router router = Router.router(vertx);
                router.route()
                    .handler(CorsHandler.create("*")
                        .allowedMethod(HttpMethod.OPTIONS)
                        .allowedMethod(HttpMethod.GET)
                        .allowedMethod(HttpMethod.POST)
                        .allowedMethod(HttpMethod.DELETE)
                        .allowedHeader("Access-Control-Request-Method")
                        .allowedHeader("Access-Control-Allow-Origin")
                        .allowedHeader("Access-Control-Allow-Headers")
                        .allowedHeader("Content-Type")
                    )
                    .handler(BodyHandler.create());
                router.get("/service").handler(serviceController::handleGet);
                router.post("/service").handler(serviceController::handlePost);
                router.delete("/service").handler(serviceController::handleDelete);
                vertx.setPeriodic(5000, timerId -> poller.startPolling());
                server.requestHandler(router).listen(8888);
            });
    }
}
