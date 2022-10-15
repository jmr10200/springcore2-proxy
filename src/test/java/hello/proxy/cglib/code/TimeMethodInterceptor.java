package hello.proxy.cglib.code;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * CGLIB 사용
 */
@Slf4j
public class TimeMethodInterceptor implements MethodInterceptor {

    private final Object target; // 프록시가 호출할 실제 대상

    public TimeMethodInterceptor(Object target) {
        this.target = target;
    }

    // MethodInterceptor 의 구현
    // 성능상 Method 보다 MethodProxy 좋음
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        log.info("TimeProxy 실행");
        Long startTime = System.currentTimeMillis();

        Object result = methodProxy.invoke(target, args); // 실제 대상을 동적으로 호출

        Long endTime = System.currentTimeMillis();
        Long resultTime = endTime - startTime;

        log.info("TimeProxy 종료 resultTime = {}", resultTime);
        return result;
    }
}
