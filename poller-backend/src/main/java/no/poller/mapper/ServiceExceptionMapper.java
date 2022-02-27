package no.poller.mapper;

import io.vertx.mysqlclient.MySQLException;
import no.poller.model.ServiceError;

public class ServiceExceptionMapper {

    public static ServiceError map(final Throwable exception) {
        if (exception instanceof MySQLException) {
            final MySQLException mappedException = ((MySQLException) exception);
            if (mappedException.getErrorCode() == 1062) {
                return new ServiceError("This service exists already!", 400);
            } else {
                return new ServiceError("Oh no!", 500);
            }
        } else {
            return new ServiceError("Oh no!", 500);
        }
    }
}
