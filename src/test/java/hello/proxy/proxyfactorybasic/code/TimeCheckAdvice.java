package hello.proxy.proxyfactorybasic.code;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

@Slf4j
public class TimeCheckAdvice implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        log.info("TimeCheckProxy 실행");
        long startTime = System.currentTimeMillis();

        // target 클래스 호출하고 그 결과를 취득
        // target 클래스 정보는 ? MethodInvocation invocation 에 들어있음
        // 프록시 팩토리로 프록시 생성 단계에서 target 정보를 파라미터로 전달하므로
        Object result = invocation.proceed(); // 로직

        long endTime = System.currentTimeMillis();

        long resultTime = endTime - startTime;
        log.info("TimeCheckProxy 종료 resultTime = {}", resultTime);

        return result;
    }
}
