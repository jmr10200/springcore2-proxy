package hello.proxy.cglibbasic.code;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

// JDK 동적 프록시 :  InvocationHandler
// CGLIB : MethodInterceptor
@Slf4j
public class TimeCheckMethodInterceptor implements MethodInterceptor {

    // 호출할 대상
    private final Object target;

    // 생성자
    public TimeCheckMethodInterceptor(Object target) {
        this.target = target;
    }

    /**
     * CGLIB 가 제공하는 MethodInterceptor
     *
     * @param obj         CGLIB 가 적용된 객체
     * @param method      호출된 메소드
     * @param args        메소드를 호출하면서 전달된 인수
     * @param methodProxy 메소드 호출에 사용
     */
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {

        log.info("TimeProxy 실행");
        long startTime = System.currentTimeMillis();

        // target : 호출할 대상, args : 넘길 인수 를 넘겨 동적으로 호출한다
        Object result = methodProxy.invoke(target, args); // Method 보다 MethodProxy 가 성능상 좋다.

        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("TimeProxy 종료 resultTime = {}", resultTime);
        return result;
    }
}
