package hello.proxy.advisorbasic;

import hello.proxy.advisorbasic.code.CommandImpl;
import hello.proxy.advisorbasic.code.CommandInterface;
import hello.proxy.advisorbasic.code.TimeCheckAdvice;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;

public class MultiAdvisorTest {

    @Test
    @DisplayName("여러 프록시 (어드바이저)")
    void multiAdvisorTest1() {
        // client -> proxy2(advice2) -> proxy1(advice1) -> target

        // proxy 1 생성
        CommandInterface target = new CommandImpl();

        ProxyFactory proxyFactory1 = new ProxyFactory(target);
        // Pointcut.True : 어드바이스 적용
        DefaultPointcutAdvisor advisor1 = new DefaultPointcutAdvisor(Pointcut.TRUE, new Advice1());
        proxyFactory1.addAdvisor(advisor1);
        CommandInterface proxy1 = (CommandInterface) proxyFactory1.getProxy();

        // proxy 2 생성, target -> proxy1 입력
        ProxyFactory proxyFactory2 = new ProxyFactory(proxy1);
        DefaultPointcutAdvisor advisor2 = new DefaultPointcutAdvisor(Pointcut.TRUE, new Advice2());
        proxyFactory2.addAdvisor(advisor2);
        CommandInterface proxy2 = (CommandInterface) proxyFactory2.getProxy();

        // 실행
        proxy2.execute1();

    }

    /** 어드바이스 1 */
    @Slf4j
    static class Advice1 implements MethodInterceptor {

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            log.info("advice1 호출");
            return invocation.proceed();
        }
    }

    /** 어드바이스 2 */
    @Slf4j
    static class Advice2 implements MethodInterceptor {

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            log.info("advice2 호출");
            return invocation.proceed();
        }
    }
}
