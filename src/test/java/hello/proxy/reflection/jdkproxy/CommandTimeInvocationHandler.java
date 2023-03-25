package hello.proxy.reflection.jdkproxy;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

// JDK 동적 프록시가 제공하는 InvocationHandler
// JDK 동적 프록시에 적용할 공통 로직 개발
@Slf4j
public class CommandTimeInvocationHandler implements InvocationHandler {

    // 동적 프록시가 호출할 대상
    private final Object target;

    // 생성자
    public CommandTimeInvocationHandler(Object target) {
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

        log.info("TimeProxy 실행");
        long startTime = System.currentTimeMillis();

        // 리플렉션을 사용해서 target 인스턴스의 메소드를 실행, args 는 메소드 호출시 넘겨줄 인수
        Object result = method.invoke(target, args);

        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("execute return = {}", result);

        log.info("TimeProxy 종료 resultTime = {}", resultTime);
        return result;
    }
}
