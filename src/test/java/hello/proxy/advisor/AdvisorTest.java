package hello.proxy.advisor;

import hello.proxy.common.advice.TimeAdvice;
import hello.proxy.common.service.ServiceImpl;
import hello.proxy.common.service.ServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;

import java.lang.reflect.Method;

@Slf4j
public class AdvisorTest {

    @Test
    void advisorTest() {
        ServiceInterface target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        // Advisor 인터페이스의 가장 일반적인 구현체
        // 생성자를 통해 하나의 포인트컷, 하나의 어드바이스를 넣어준다.
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(Pointcut.TRUE, new TimeAdvice());
        // 프록시 팩토리에 어드바이저를 제공 (프록시 팩토리는 어드바이저가 필수이다)
        // 과거 proxyFactory.addAdvisor(new TimeAdvice()); 처럼 어드바이스를 넣었는데
        // 단순히 편의 메소드이고 내부적으로 DefaultPointcutAdvisor(Pointcut.TRUE, new TimeAdvice()); 가 생성된다.
        proxyFactory.addAdvisor(advisor);
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();

        proxy.save();
        proxy.find();
    }

    /**
     * save() 메소드는 어드바이스 로직 O
     * find() 메소드는 어드바이스 로직 X
     */
    @Test
    @DisplayName("직접 만든 포인트컷")
    void advisorTest2() {
        ServiceImpl target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(new MyPointcut(), new TimeAdvice());
        proxyFactory.addAdvisor(advisor);
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();

        proxy.save();
        proxy.find(); // 어드바이스 적용 안되므로 어드바이스로 등록한 TimeProxy 가 실행되지 않음
    }

    /**
     * 직접 만든 포인트컷
     */
    static class MyPointcut implements Pointcut {
        @Override
        public ClassFilter getClassFilter() {
            return ClassFilter.TRUE; // 클래스 필터는 항상 TRUE 리턴
        }

        @Override
        public MethodMatcher getMethodMatcher() {
            return new MyMethodMatcher();
        }
    }

    /**
     * 직접 구현한 MethodMatcher
     */
    static class MyMethodMatcher implements MethodMatcher {

        private String matchName = "save";

        // 어드바이스를 적용할지 말지 판단
        @Override
        public boolean matches(Method method, Class<?> targetClass) {
            // 메소드명이 save 인 경우에 true 리턴
            boolean result = method.getName().equals(matchName);
            log.info("포인트컷 호출 method={} targetClass={}", method.getName(), targetClass);
            log.info("포인트컷 결과 result={}", result);
            return result;
        }

        @Override
        public boolean isRuntime() {
            return false;
        }

        @Override
        public boolean matches(Method method, Class<?> targetClass, Object... args) {
            throw new UnsupportedOperationException();
        }
    }

}
