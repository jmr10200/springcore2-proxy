package hello.proxy.postprocessor;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
public class BasicTest {

    @Test
    void basicConfig2() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(BasicConfig.class);

        // A 는 빈으로 등록 됨
        A a = applicationContext.getBean("beanA", A.class);
        a.helloA();

        // B 는 빈 등록 X
        Assertions.assertThatThrownBy(() -> applicationContext.getBean(B.class)).isInstanceOf(NoSuchBeanDefinitionException.class);
    }

    @Configuration
    static class BasicConfig {
        @Bean(name = "beanA") // beanA 라는 이름으로 A 객체 빈등록
        public A a() {
            return new A();
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
}
