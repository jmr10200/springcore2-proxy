package hello.proxy.trace.log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
@Slf4j
public class LogTracerAspect {

    private final LogTracer logTrace;

    public LogTracerAspect(LogTracer logTrace) {
        this.logTrace = logTrace;
    }

    @Around("execution(* hello.proxy.app.componentscan..*(..))")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        LogTracerStatus status = null;

        try {
            // 클래스.메소드() 취득
            String message = joinPoint.getSignature().toShortString();
            status = logTrace.startLog(message);

            // 로직
            Object result = joinPoint.proceed();

            logTrace.endLog(status);
            return result;
        } catch (Exception e) {
            logTrace.exceptionLog(status, e);
            throw e;
        }
    }
}
