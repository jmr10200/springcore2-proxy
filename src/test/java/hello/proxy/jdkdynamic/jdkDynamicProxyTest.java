package hello.proxy.jdkdynamic;

import hello.proxy.jdkdynamic.code.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;

@Slf4j
public class jdkDynamicProxyTest {

    @Test
    void dynamicA() {
        AInterface target = new AImpl();
        // 등적프록시에 적용할 핸들러 로직
        TimeInvocationHandler handler = new TimeInvocationHandler(target);
        // 동적프록시는 java.lang.reflect.Proxy 를 통해서 생성
        // 클래스 로더 정보, 인터페이스, 핸들러 로직
        AInterface proxy = (AInterface) Proxy.newProxyInstance(
                AInterface.class.getClassLoader(),
                new Class[]{AInterface.class}, handler);

        proxy.call();

        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());
    }

    @Test
    void dynamicB() {
        BInterface target = new BImpl();
        TimeInvocationHandler handler = new TimeInvocationHandler(target);
        BInterface proxy = (BInterface) Proxy.newProxyInstance(
                BInterface.class.getClassLoader(),
                new Class[] {BInterface.class}, handler);

        proxy.call();

        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());
    }
}
/* 생성된 JDK 동적 프록시 */
// proxyClass=class com.sun.proxy.$Proxy12 이부분이 동적으로 생성된 프록시 클래스 정보
// 직접 작성한 클래스가 아니라 JDK 동적 프록시가 동적으로 만들어준 프록시이다.
// 이 프록시는 TimeInvocationHandler 로직을 실행한다.

// 실행순서
// 1. 클라이언트는 JDK 동적 프록시의 call() 실행
// 2. JDK 동적 프록시는 InvocationHandler.invoke() 를 호출.
//    TimeInvocationHandler 가 구현체로 있으므로 TimeInvocationHandler.invoke() 가 호출
// 3. TimeInvocationHandler 가 내부 로직을 수행하고, method.invoke(target, args) 호출해서 target 인 실제 객체 AImpl 를 호출
// 4. AImpl 인스턴스의 call() 이 실행됨
// 5. AImpl 인스턴스의 call() 의 실행이 끝나면 TimeInvocationHandler 로 응답이 돌아옴
//    시간로그를 출력하고 결과 반환

// 정리
// 예제는 AImpl , BImpl 각각 프록시를 만들지 않았다.
// 프록시는 JDK 동적 프록시를 사용해서 동적으로 만들고 TimeInvocationHandler 는 공통으로 사용했다.
// JDK 동적 프록시 기술 덕분에 적용 대상 만큼 프록시 객체를 만들지 않아도 된다.
// 그리고 같은 부가기능 로직을 한번만 개발해서 공통으로 적용할 수 있다.
// 결과적으로 프록시 클래스를 수 없이 만들어야 하는 문제도 해결하고,
// 부가기능 로직도 하나의 클래스에 모아서 단일 책임 원칙(SRP) 도 지킬 수 있게 되었다.