package hello.proxy.advisor;

import hello.proxy.common.advice.TimeAdvice;
import hello.proxy.common.service.ServiceImpl;
import hello.proxy.common.service.ServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;

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
}
