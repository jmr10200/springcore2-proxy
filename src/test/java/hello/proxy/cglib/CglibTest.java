package hello.proxy.cglib;

import hello.proxy.cglib.code.TimeMethodInterceptor;
import hello.proxy.common.service.ConcreteService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.cglib.proxy.Enhancer;

@Slf4j
public class CglibTest {

    @Test
    void cglib() {

        // ConcreteService : 인터페이스가 없는 구체 클래스
        ConcreteService target = new ConcreteService();

        // Enhancer : CGLIB 는 Enhancer 를 사용해서 프록시를 생성함
        Enhancer enhancer = new Enhancer();
        // 구체 클래스를 상속받아서 프록시 생성, 어떤 구체클래스를 상속받을지 지정
        enhancer.setSuperclass(ConcreteService.class);
        // 프록시에 적용할 실행 로직 할당
        enhancer.setCallback(new TimeMethodInterceptor(target));

        ConcreteService proxy = (ConcreteService) enhancer.create();
        log.info("targetClass = {}", target.getClass());
        log.info("proxyClass = {}", proxy.getClass());

        proxy.call();
    }

    // CGLIB 가 생성한 프록시 클래스 이름
    // ConcreteService$$EnhancerByCGLIB$$25d6b0e3
    // 대상클래스$$EnhancerByCGLIB$$임의코드

}
/* CGLIB 제약 */
// 클래스 기반 프록시는 상속을 사용하기 때문에 몇가지 제약이 있다.
// ・부모 클래스의 생성자를 체크해야 한다. : CGLIB 는 자식 클래스를 동적으로 생성하기 때문에 기본 생성자가 필요하다.
// ・클래스에 final 키워드가 붙으면 상속이 불가능하다 : CGLIB 에서는 예외가 발생
// ・메소드에 final 키워드가 붙으면 해당 메소드를 오버라이딩 할 수 없다. : CGLIB 에서는 프록시 로직이 동작하지 않는다.

// 참고
// CGLIB 를 사용하면 인터페이스가 없는 V2 에 동적 프록시를 적용할 수 있다.
// 그런데 지금 당장 적용하기에는 몇가지 제약이 있다.
// V2 에 기본 생성자를 추가하고 의존관계를 setter 를 사용해서 주입하면 CGLIB 를 적용할 수 있다.
// 하지만 ProxyFactory 를 통해서 CGLIB 를 적용하면 이런 단점을 해결하고 더 편리하다.

// 문제
// 인터페이스가 있는 경우에는 JDK 동적 프록시를 적용하고,
// 그렇지 않은 경우에는 CGLIB 를 적용하려면 어떻게 해야 하는가?
// 두 기술을 함께 사용할 때 부가기능을 제공하기위해서
// JDK 동적 프록시가 제공하는 InvocationHandler 와
// CGLIB 가 제공하는 MethodInterceptor 를 각각 중복으로 만들어서 관리해야 할까?
// 특정 조건에 맞을 때 프록시 로직을 적용하는 기능도 공통으로 제공되면?
