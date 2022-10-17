package hello.proxy.proxyfactory;

import hello.proxy.common.advice.TimeAdvice;
import hello.proxy.common.service.ConcreteService;
import hello.proxy.common.service.ServiceImpl;
import hello.proxy.common.service.ServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;

import static org.assertj.core.api.Assertions.*;

@Slf4j
public class ProxyFactoryTest {

    @Test
    @DisplayName("인터페이스가 있으면 JDK 동적 프록시 사용")
    void interfaceProxy() {
        ServiceInterface target = new ServiceImpl();
        // 프록시 팩토리 생성 : 인스턴스 정보 기반으로 프록시 생성, 인터페이스 이므로 JDK
        // 구체클래스만 존재하면 CGLIB 로 생성된다.
        ProxyFactory proxyFactory = new ProxyFactory(target);
        // 프록시 팩토리를 통해서 만든 프록시가 사용할 부가기능 로직 설정
        proxyFactory.addAdvice(new TimeAdvice()); // 부가기능 (조언한다!)
        // 프록시 객체를 생성하고 그 결과 리턴
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();

        log.info("targetClass = {}", target.getClass());
        log.info("proxyClass = {}", proxy.getClass()); // $Proxy13 으로 JDK 생성확인

        proxy.save();

        // 프록시 팩토리로 생성되었는가
        assertThat(AopUtils.isAopProxy(proxy)).isTrue();
        // JDK 동적 프록시 인가
        assertThat(AopUtils.isJdkDynamicProxy(proxy)).isTrue();
        // CGLIB 동적 프록시 인가
        assertThat(AopUtils.isCglibProxy(proxy)).isFalse();
    }

    @Test
    @DisplayName("구체 클래스만 있으면 CGLIB 사용")
    void concreteProxy() {
        ConcreteService target = new ConcreteService();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.addAdvice(new TimeAdvice());
        ConcreteService proxy = (ConcreteService) proxyFactory.getProxy();
        log.info("targetClass = {}", target.getClass());
        log.info("proxyClass = {}", proxy.getClass()); // ConcreteService$$EnhancerBySpringCGLIB$$576f0055 으로 확인 OK

        proxy.call();

        // 프록시 팩토리로 생성되었는가
        assertThat(AopUtils.isAopProxy(proxy)).isTrue();
        // JDK 동적 프록시 인가
        assertThat(AopUtils.isJdkDynamicProxy(proxy)).isFalse();
        // CGLIB 동적 프록시 인가
        assertThat(AopUtils.isCglibProxy(proxy)).isTrue();
    }

    @Test
    @DisplayName("ProxyTargetClass 옵션을 사용하면 인터페이스가 있어도 CGLIB 를 사용하고, 클래스 기반 프록시 사용")
    void proxyTargetClass() {
        ServiceInterface target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        // ProxyTargetClass 옵션 사용
        proxyFactory.setProxyTargetClass(true);
        proxyFactory.addAdvice(new TimeAdvice());
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();

        log.info("targetClass = {}", target.getClass());
        log.info("proxyClass = {}", proxy.getClass()); // $$EnhancerBySpringCGLIB$$6213e274 인터페이스 임에도 CGLIB 확인 OK

        proxy.save();

        // 프록시 팩토리로 생성되었는가
        assertThat(AopUtils.isAopProxy(proxy)).isTrue();
        // JDK 동적 프록시 인가
        assertThat(AopUtils.isJdkDynamicProxy(proxy)).isFalse();
        // CGLIB 동적 프록시 인가
        assertThat(AopUtils.isCglibProxy(proxy)).isTrue();
    }
}
/* 프록시 팩토리의 기술 선택 방법 */
// 대상에 인터페이스가 있으면 : JDK 동적 프록시, 인터페이스 기반 프록시
// 대상에 인터페이스가 없으면 : CGLIB, 구체 클래스 기반 프록시
// ProxyTargetClass(True) : CGLIB, 구체 클래스 기반 프록시, 인터페이스 여부와 무관

/* 정리 */
// 프록시 팩토리의 서비스 추상화 덕분에 구체적인 CGLIB, JDK 동적 프록시 기술에 의존하지 않고, 편리하게 동적 프록시를 생성할 수 있다.
// 프록시의 부가 기능 로직도 특정 기술에 종속적이지 않게 Advice 하나로 편리하게 사용할 수 있다.
// 이것은 프록시 팩토리가 내부에서 JDK 동적 프록시인 경우 InvocationHandler 가 Advice 를
// 호출하도록 개발해두고, CGLIB 인 경우 MethodInterceptor 가 Advice 를 호출하도록
// 기능을 개발해 두었기 때문이다.

// 참고
// 스프링 부트는 AOP 를 적용할 때 기본적으로 ProxyTargetClass(true) 로 설정해서 사용한다.
// 즉 인터페이스가 있어도 항상 CGLIB 를 사용해서 구체클래스 기반으로 프록시를 생성한다.