package no.poller.model;

import java.time.LocalDateTime;

public class Service {
    private final String name;
    private final String url;
    private final LocalDateTime timestamp;
    private final ServiceState state;

    public Service(String name, String url, LocalDateTime timestamp, ServiceState state) {
        this.name = name;
        this.url = url;
        this.timestamp = timestamp;
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public ServiceState getState() {
        return state;
    }

    @Override
    public String toString() {
        return "Service{" +
            "name='" + name + '\'' +
            ", url='" + url + '\'' +
            ", timestamp=" + timestamp +
            ", state='" + state + '\'' +
            '}';
    }
}
