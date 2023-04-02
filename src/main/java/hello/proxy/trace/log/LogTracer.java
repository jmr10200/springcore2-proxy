package hello.proxy.trace.log;

import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public class LogTracer {

    private ThreadLocal<String > traceIdHolder = new ThreadLocal<>();

    public LogTracerStatus startLog(String message) {
        createTraceId();
        String traceId = traceIdHolder.get();
        Long startTimeMs = System.currentTimeMillis();
        log.info("[{}] {}", traceId,  message);
        return new LogTracerStatus(traceId, startTimeMs, message);
    }
    private void createTraceId() {
        String traceId = traceIdHolder.get();
        if (traceId == null) {
            traceIdHolder.set(UUID.randomUUID().toString().substring(0, 8));
        }
    }

    public void endLog(LogTracerStatus status) {
        printLog(status, null);
    }

    public void exceptionLog(LogTracerStatus status, Exception e) {
        printLog(status, e);
    }

    private void printLog(LogTracerStatus status, Exception e) {
        Long stopTimeMs = System.currentTimeMillis();
        long resultTimeMs = stopTimeMs - status.getStartTimeMs();
        String traceId = status.getId();
        if (e == null) {
            log.info("[{}] {} time={}ms", traceId, status.getMessage(), resultTimeMs);
        } else {
            log.info("[{}] {} time={}ms ex={}", traceId, status.getMessage(), resultTimeMs, e.toString());
        }
    }
}
