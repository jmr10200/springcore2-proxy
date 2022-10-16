package hello.proxy.proxyfactory;

import hello.proxy.common.advice.TimeAdvice;
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

}
