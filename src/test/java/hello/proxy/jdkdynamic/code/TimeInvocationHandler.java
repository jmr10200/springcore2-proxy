package hello.proxy.jdkdynamic.code;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@Slf4j
public class TimeInvocationHandler implements InvocationHandler {

    private final Object target;

    public TimeInvocationHandler(Object target) {
        this.target = target;
    }



    /**
     * JDK 동적 프록시가 제공하는 InvocationHandler
     * @param proxy 프록시 자신
     * @param method 호출한 메소드
     * @param args 메소드를 호출할 때 전달한 인수
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // TimeInvocationHandler 는 InvocationHandler 인터페이스 invoke() 구현
        // JDK 동적 프록시에 적용할 공통 로직 개발 가능
        log.info("TimeProxy 실행");
        Long startTime = System.currentTimeMillis();

        // 리플렉션을 사용해서 target 인스턴스의 메소드 실행
        // args 는 메소드 호출시 넘겨줄 인수
        Object result = method.invoke(target, args);

        Long endTime = System.currentTimeMillis();
        Long resultTime = endTime - startTime;
        log.info("TimeProxy 종료 resultTime={}", resultTime);

        return result;
    }
}
