package hello.proxy.common.service;

/**
 * 인터페이스와 구현이 있는 서비스 클래스
 */
public interface ServiceInterface {

    void save();

    void find();

}
/* CGLIB (Code Generator Library) */
// CGLIB 는 바이트코드를 조작해서 동적으로 클래스를 생성하는 기술을 제공하는 라이브러리
// 인터페이스가 없어도 구체 클래스만 가지고 동적 프록시를 생성할 수 있다.
// 원래는 외부 라이브러리인데, 스프링 프레임워크가 스프링 내부 소스 코드에 포함했다.
// 따라서 스프링을 사용하면 별도의 외부 라이브러리를 추가하지 않아도 사용할 수 있다.

// 참고로 CGLIB 를 직접 사용하는 경우는 거의 없다.
// ProxyFactory 라는 것이 이 기술을 편리하게 사용하게 도와주기 때문이다.

// 인터페이스와 구현이 있는 서비스 클래스 : ServiceInterface, ServiceImpl
// 구체 클래스만 있는 서비스 클래스 : ConcreteService