package hello.proxy.advisorbasic;

import hello.proxy.advisorbasic.code.CommandImpl;
import hello.proxy.advisorbasic.code.CommandInterface;
import hello.proxy.advisorbasic.code.TimeCheckAdvice;
import hello.proxy.advisorbasic.custom.MyPointcut;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;

public class AdvisorBasicTest {

    @Test
    void advisorBasic() {
        CommandInterface target = new CommandImpl();

        ProxyFactory proxyFactory = new ProxyFactory(target);
        // Advisor 인터페이스 기본 구현체 : 생성자를 통해 PointCut 1 + Advice 1 넣어준다.
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(Pointcut.TRUE, new TimeCheckAdvice());
        // 프록시 팩토리에 어드바이저 지정
        proxyFactory.addAdvisor(advisor);
        // 아래의 메소드는 어드바이저가 아니라, 어드바이스를 넣는다. 내부적으로 위 코드와 동일하다.
        // proxyFactory.addAdvice(new TimeCheckAdvice());

        CommandInterface proxy = (CommandInterface) proxyFactory.getProxy();

        proxy.execute1();
        proxy.execute2();
    }

    @Test
    @DisplayName("포인트컷 생성")
    void advisorPointcutBasic() {
        CommandInterface target = new CommandImpl();

        ProxyFactory proxyFactory = new ProxyFactory(target);
        // Advisor 인터페이스 기본 구현체 : 생성자를 통해 PointCut 1 + Advice 1 넣어준다.
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(new MyPointcut(), new TimeCheckAdvice());
        // 프록시 팩토리에 어드바이저 지정
        proxyFactory.addAdvisor(advisor);
        // 아래의 메소드는 어드바이저가 아니라, 어드바이스를 넣는다. 내부적으로 위 코드와 동일하다.
        // proxyFactory.addAdvice(new TimeCheckAdvice());

        CommandInterface proxy = (CommandInterface) proxyFactory.getProxy();

        proxy.execute1();
        proxy.execute2();
    }
}
