package hello.proxy.proxyfactorybasic;

import hello.proxy.proxyfactorybasic.code.CommandService;
import hello.proxy.proxyfactorybasic.code.CommandServiceImpl;
import hello.proxy.proxyfactorybasic.code.ConcreteService;
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
    @DisplayName("인터페이스가 있는 경우 : JDK 동적 프록시")
    void interfaceProxy() {
        CommandService target = new CommandServiceImpl();

        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.addAdvice(new TimeCheckAdvice());

        CommandService proxy = (CommandService) proxyFactory.getProxy();

        log.info("targetClass = {}", target.getClass());
        log.info("proxyClass = {}", proxy.getClass());

        proxy.find();

        // 인터페이스 있는경우, AopProxy 이며, JDK 동적 프록시
        assertTrue(AopUtils.isAopProxy(proxy));
        assertTrue(AopUtils.isJdkDynamicProxy(proxy));
        assertFalse(AopUtils.isCglibProxy(proxy));
    }

    @Test
    @DisplayName("구체 클래스만 있는 경우: CGLIB")
    void concreteProxy() {
        ConcreteService target = new ConcreteService();

        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.addAdvice(new TimeCheckAdvice());

        ConcreteService proxy = (ConcreteService) proxyFactory.getProxy();

        log.info("targetClass = {}", target.getClass());
        log.info("proxyClass = {}", proxy.getClass());

        proxy.call();

        // 구체 클래스만 있으면 AopProxy 이며, CGLIB
        assertTrue(AopUtils.isAopProxy(proxy));
        assertFalse(AopUtils.isJdkDynamicProxy(proxy));
        assertTrue(AopUtils.isCglibProxy(proxy));
    }

    @Test
    @DisplayName("proxyTargetClass(true) 설정시 인터페이스가 있어도 CGLIB, 클래스 기반 프록시 사용")
    void proxyTargetClass() {
        CommandService target = new CommandServiceImpl();

        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.addAdvice(new TimeCheckAdvice());
        // 스프링부트는 항상 true 로 설정해서 항상 CGLIB 로 생성한다.
        proxyFactory.setProxyTargetClass(true); // 중요

        CommandService proxy = (CommandService) proxyFactory.getProxy();

        log.info("targetClass = {}", target.getClass());
        log.info("proxyClass = {}", proxy.getClass());

        proxy.modify();

        // setProxyTargetClass(true) 이면 인터페이스도 CGLIB 사용
        assertTrue(AopUtils.isAopProxy(proxy));
        assertFalse(AopUtils.isJdkDynamicProxy(proxy));
        assertTrue(AopUtils.isCglibProxy(proxy));
    }
}
