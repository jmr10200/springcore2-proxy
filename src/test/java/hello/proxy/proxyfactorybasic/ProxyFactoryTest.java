package hello.proxy.proxyfactorybasic;

import hello.proxy.proxyfactorybasic.code.CommandService;
import hello.proxy.proxyfactorybasic.code.CommandServiceImpl;
import hello.proxy.proxyfactorybasic.code.TimeCheckAdvice;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class ProxyFactoryTest {

    @Test
    @DisplayName("인터페이스가 있으면 JDK 동적 프록시 사용")
    void interfaceProxy() {
        CommandService target = new CommandServiceImpl();

        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.addAdvice(new TimeCheckAdvice());

        CommandService proxy = (CommandService) proxyFactory.getProxy();

        log.info("targetClass = {}", target.getClass());
        log.info("proxyClass = {}", proxy.getClass());

        proxy.find();

        // 인터페이스 있는경우, AopProxy 이며, JDK 동적 프록시이다
        assertTrue(AopUtils.isAopProxy(proxy));
        assertTrue(AopUtils.isJdkDynamicProxy(proxy));
        assertFalse(AopUtils.isCglibProxy(proxy));
    }
}
