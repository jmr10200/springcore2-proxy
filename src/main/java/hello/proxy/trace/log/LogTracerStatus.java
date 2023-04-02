package hello.proxy.trace.log;

public class LogTracerStatus {

    private String id;

    private Long startTimeMs;

    private String message;

    public LogTracerStatus(String id, Long starTimeMs, String message) {
        this.id = id;
        this.startTimeMs = starTimeMs;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public Long getStartTimeMs() {
        return startTimeMs;
    }

    public String getMessage() {
        return message;
    }
}
