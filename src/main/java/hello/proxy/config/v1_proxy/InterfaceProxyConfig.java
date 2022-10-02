package hello.proxy.config.v1_proxy;

import hello.proxy.app.v1.*;
import hello.proxy.config.v1_proxy.interface_proxy.OrderControllerInterfaceProxy;
import hello.proxy.config.v1_proxy.interface_proxy.OrderRepositoryInterfaceProxy;
import hello.proxy.config.v1_proxy.interface_proxy.OrderServiceInterfaceProxy;
import hello.proxy.trace.logtrace.LogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InterfaceProxyConfig {

    @Bean
    public OrderControllerV1 orderController(LogTrace logTrace) {
        OrderControllerV1Impl orderControllerImpl = new OrderControllerV1Impl(orderService(logTrace));
        // 프록시의 target 에 실제객체 Impl 을 주입시켜서 프록시객체를 빈으로 등록한다.
        // 스프링빈 (OrderControllerV1) 이 아닌 프록시객체(OrderControllerInterfaceProxy) 등록
        return new OrderControllerInterfaceProxy(orderControllerImpl, logTrace);
        // 정리하면 다음과 같은 의존관계를 가진다.
        // proxy -> target
        // OrderControllerInterfaceProxy -> orderControllerV1Impl
    }

    @Bean
    public OrderServiceV1 orderService(LogTrace logTrace) {
        OrderServiceV1Impl orderServiceImpl = new OrderServiceV1Impl(orderRepository(logTrace));
        return new OrderServiceInterfaceProxy(orderServiceImpl, logTrace);
    }

    @Bean
    public OrderRepositoryV1 orderRepository(LogTrace logTrace) {
        OrderRepositoryV1Impl repositoryImpl = new OrderRepositoryV1Impl();
        return new OrderRepositoryInterfaceProxy(repositoryImpl, logTrace);
    }
}
// InterfaceProxyConfig 를 통해 프록시를 적용한 후
// 스프링 컨테이너 프록시 객체가 등록된다. 스프링 컨테이너는 이제 실제 객체가 아니라 프록시 객체를 스프링빈으로 관리하다.
// 이제 실제 객체는 스프링 컨테이너와는 상관이 없다. 실제 객체는 프록시 객체를 통해서 참조될 뿐이다.
// 프록시 객체는 스프링 컨테이너가 관리하고 자바 힙 메모리에도 올라간다.
// 반면에 실제 객체는 자바 힙 메모리에는 올라가지만 스프링 컨테이너가 관리하지는 않는다.

// 최종적인 런타임 객체 의존 관계
// client -> OrderControllerInterfaceProxy -> OrderControllerV1Impl
// -> OrderServiceInterfaceProxy -> OrderServiceV1Impl
// -> OrderRepositoryInterfaceProxy -> OrderRepositoryV1Impl