package hello.proxy.config.v2_dynamicproxy.handler;

import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import org.springframework.util.PatternMatchUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class LogTraceFilterHandler implements InvocationHandler {

    private final Object target;
    private final LogTrace logTrace;
    private final String[] patterns;

    public LogTraceFilterHandler(Object target, LogTrace logTrace, String... patterns) {
        this.target = target;
        this.logTrace = logTrace;
        this.patterns = patterns;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        // 메소드 이름 필터
        String methodName = method.getName();
        // 스프링이 제공하는 PatternMatchUtils
        // abc* : abc 로 시작하면 true
        if (!PatternMatchUtils.simpleMatch(patterns, methodName)) {
            // 메소드 이름이 특정 패턴에 일치하지 않으면, logTrace 실행 하지 않는다.
            return method.invoke(target, args);
        }

        // 메소드 이름이 특정 패턴에 일치하는 경우에만 LogTrace 로직을 실행
        TraceStatus status = null;
        try {
            String message = method.getDeclaringClass().getSimpleName() + "." + method.getName() + "()";
            status = logTrace.begin(message);

            // 로직 호출
            Object result = method.invoke(target, args);
            logTrace.end(status);
            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }
}
