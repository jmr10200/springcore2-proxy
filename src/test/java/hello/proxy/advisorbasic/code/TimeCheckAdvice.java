package hello.proxy.advisorbasic.code;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

@Slf4j
public class TimeCheckAdvice implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        log.info("TimeCheckAdvice 실행");
        long startTime = System.currentTimeMillis();

        // target 클래스 호출하고 그 결과를 취득
        Object result = invocation.proceed(); // 로직

        long endTime = System.currentTimeMillis();

        long resultTime = endTime - startTime;
        log.info("TimeCheckAdvice 종료 resultTime = {}", resultTime);

        return result;
    }
}
