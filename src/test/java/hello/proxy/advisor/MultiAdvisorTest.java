package hello.proxy.advisor;

import hello.proxy.common.service.ServiceImpl;
import hello.proxy.common.service.ServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;

@Slf4j
public class MultiAdvisorTest {

    /**
     * 하나의 target 에 여러 어드바이저를 적용하려면?
     * 다시 말하면 어드바이저는 포인트컷1, 어드바이스1 이므로,
     * target 에 여러 어드바이스를 적용하려면?
     */
    @Test
    @DisplayName("여러 프록시")
    void multiAdvisorTest1() {
        // client -> proxy2(advisor2) -> proxy1(advisor1) -> target

        // 프록시1 생성
        ServiceInterface target = new ServiceImpl();
        ProxyFactory proxyFactory1 = new ProxyFactory(target);
        DefaultPointcutAdvisor advisor1 = new DefaultPointcutAdvisor(Pointcut.TRUE, new Advice1());
        proxyFactory1.addAdvisor(advisor1);
        ServiceInterface proxy1 = (ServiceInterface) proxyFactory1.getProxy();

        // 프록시2 생성, target -> proxy1 입력
        ProxyFactory proxyFactory2 = new ProxyFactory(proxy1);
        DefaultPointcutAdvisor advisor2 = new DefaultPointcutAdvisor(Pointcut.TRUE, new Advice2());
        proxyFactory1.addAdvisor(advisor2);
        ServiceInterface proxy2 = (ServiceInterface) proxyFactory2.getProxy();

        // 실행
        proxy2.save(); // 포인트컷은 항상 TRUE 이므로 둘다 어드바이스 적용됨
        // 이와 같은 방법의 문제는 프록시를 2번 생성해주어야 한다는 것이다.
        // 하나의 프록시로 여러개의 어드바이저를 이용할 수 있는 방법은 없을까?
        // 스프링은 프록시 팩토리를 통해 이를 적용할 수 있게 만들어두었다.
    }

    static class Advice1 implements MethodInterceptor {
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            log.info("advice1 호출");
            return invocation.proceed();
        }
    }

    static class Advice2 implements MethodInterceptor {
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            log.info("advice2 호출");
            return invocation.proceed();
        }
    }

    /**
     * 프록시 팩토리를 적용하여 하나의 프록시로 여러 어드바이저 사용하기
     */
    @Test
    @DisplayName("하나의 프록시, 여러 어드바이저")
    void multiAdvisorTest2() {
        // proxy -> advisor2 -> advisor1 -> target

        DefaultPointcutAdvisor advisor2 = new DefaultPointcutAdvisor(Pointcut.TRUE, new Advice2());
        DefaultPointcutAdvisor advisor1 = new DefaultPointcutAdvisor(Pointcut.TRUE, new Advice1());

        ServiceInterface target = new ServiceImpl();
        ProxyFactory proxyFactory1 = new ProxyFactory(target);
        proxyFactory1.addAdvisor(advisor2); // advisor 는 동작 순서대로 넣어야 한다.
        proxyFactory1.addAdvisor(advisor1);
        ServiceInterface proxy = (ServiceInterface) proxyFactory1.getProxy();

        // 실행
        proxy.save();
        // 결과적으로 여러개의 프록시를 생성해서 사용할 때보다 성능적으로 더 좋다.
        // 중요한 것은 프록시가 여러개 생성되지 않는다는 것이다.
        // 스프링은 AOP 를 적용할때, 최적화를 진행해서 프록시는 하나만 생성한다.
        // 하나의 프록시에 여러 어드바이저를 적용하는 방식이다.

        // 정리하면 하나의 target 에 여러 AOP 가 동시에 적용되어도,
        // 스프링의 AOP 는 target 마다 하나의 프록시만 생성한다.
    }


}
