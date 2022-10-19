package hello.proxy.common.advice;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * MethodInterceptor 구현클래스
 */
@Slf4j
public class TimeAdvice implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        log.info("TimeProxy 실행");
        Long startTime = System.currentTimeMillis();

        // proceed() 를 호출하면 target 클래스를 호출하고 그 결과를 리턴받는다.
        // target 클래스의 정보는 MethodInvocation invocation 에 포함되어 있다.
        Object result = invocation.proceed();

        Long endTime = System.currentTimeMillis();
        Long resultTime = endTime - startTime;
        log.info("TimeProxy 종료 resultTime={}", resultTime);

        return result;
    }
}
