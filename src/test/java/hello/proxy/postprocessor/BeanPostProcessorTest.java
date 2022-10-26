package hello.proxy.postprocessor;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
public class BeanPostProcessorTest {

    @Test
    void postProcessor() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(BeanPostProcessorConfig.class);

        // beanA 라는 이름으로 B 객체 등록 OK
        B b = applicationContext.getBean("beanA", B.class);
        b.helloB();

        // A 는 빈 등록 X
        Assertions.assertThatThrownBy(() -> applicationContext.getBean(A.class)).isInstanceOf(NoSuchBeanDefinitionException.class);

    }

    @Configuration
    static class BeanPostProcessorConfig {

        @Bean(name = "beanA")
        public A a() {
            return new A();
        }

        // 빈 후처리기
        @Bean
        public AToBPostProcessor helloPostProcessor() {
            return new AToBPostProcessor();
        }
    }

    static class A {
        public void helloA() {
            log.info("hello A");
        }
    }

    static class B {
        public void helloB() {
            log.info("hello B");
        }
    }

    /**
     * 빈 후처리기
     * 인터페이스인 BeanPostProcessor 를 구현하고,
     * 스프링빈으로 등록하면 스프링컨테이너가 빈 후처리기로 인식하고 동작한다.
     */
    static class AToBPostProcessor implements BeanPostProcessor {
        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

            log.info("beanName={} bean={}", beanName, bean);
            // 빈이 A 객체이면 B 객체로 바꿔치기 하는 로직
            if (bean instanceof A) {
                return new B();
            }
            return bean;
        }

    }

}
/* 정리 */
// 빈 후처리기는 빈을 조작하고 변경할 수 있는 후킬 포인트이다.
// 빈 객체를 조작하거나 다른 객체로 바꾸어 버릴 수 있을 정도로 막강하다.
// 여기서 조작이라는 것은 해당 객체의 특정 메소드를 호출하는 것을 뜻한다.
// 일반적으로 스프링 컨테이너가 등록하는, 특히 컴포넌트 스캔의 대상이 되는 빈들은 중간에 조작할 방법이 없는데,
// 빈 후처리기를 사용하면 개발자가 등록하는 모든 빈을 중간에 조작할 수 있다.
// 즉 빈 객체를 프록시로 교체하는 것도 가능하다는 의미이다.

// 참고 : @PostConstruct 의 비밀
// @PostConstruct 는 스프링 빈 생성 이후에 빈을 초기화 하는 역할을 한다.
// 빈의 초기화 라는 것이 단순히 @PostConstruct 어노테이션이 붙은 초기화 메소드를 한번 호출만 하면 된다.
// 쉽게 얘기해서 생성된 빈을 한번 조작하는 것이다.
// 따라서 빈을 조작하는 행위를 하는 적절한 빈 후처리기가 있으면 될 것 같다.
// 스프링은 CommonAnnotationBeanPostProcessor 라는 빈 후처리기를 자동으로 등록하는데,
// 여기에서 @PostConstruct 어노테이션이 붙은 메소드를 호출한다.
// 따라서 스프링 스스로도 스프링 내부의 기능을 확장하기 위해 빈 후처리기를 사용한다.