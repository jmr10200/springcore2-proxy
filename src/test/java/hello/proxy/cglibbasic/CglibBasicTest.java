package hello.proxy.cglibbasic;

import hello.proxy.cglibbasic.code.ConcreteService;
import hello.proxy.cglibbasic.code.TimeCheckMethodInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.cglib.proxy.Enhancer;

@Slf4j
public class CglibBasicTest {

    @Test
    void concreteCGLIB() {
        ConcreteService target = new ConcreteService();

        // CGLIB 는 Enhancer 를 사용해서 프록시를 생성한다
        Enhancer enhancer = new Enhancer();
        // CGLIB 는 구체 클래스를 상속 받아서 프록시를 생성, 어떤 구체 클래스를 상속받을지 지정
        enhancer.setSuperclass(ConcreteService.class);
        // 지정한 클래스를 상속 받아서 프록시가 만들어진다.
        enhancer.setCallback(new TimeCheckMethodInterceptor(target));

        ConcreteService proxy = (ConcreteService) enhancer.create();
        log.info("targetClass = {}", target.getClass());
        log.info("proxyClass = {}", proxy.getClass());

        String callResult = proxy.call();
        log.info("proxy.call() result = {}", callResult);
    }
}
