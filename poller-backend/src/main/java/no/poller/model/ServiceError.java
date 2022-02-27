package no.poller.model;

public class ServiceError {
    private final String message;
    private final int code;

    public ServiceError(final String message, final int code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}
