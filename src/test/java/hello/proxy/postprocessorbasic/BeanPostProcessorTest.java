package hello.proxy.postprocessorbasic;

import hello.proxy.postprocessorbasic.code.Phone;
import hello.proxy.postprocessorbasic.code.Radio;
import hello.proxy.postprocessorbasic.postcode.BeanPostProcessorConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class BeanPostProcessorTest {

    // 스프링이 제공하는 BeanPostProcessor 인터페이스 사용하여 객체 바꿔치기
    @Test
    void postProcessor() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(BeanPostProcessorConfig.class);

        // beanPhone 이름으로 Radio 객체가 빈으로 등록된다.
        Radio radio = applicationContext.getBean("beanPhone", Radio.class);
        radio.radioCall();

        // Phone 은 빈으로 등록되지 않는다.
        Assertions.assertThrows(NoSuchBeanDefinitionException.class, () -> applicationContext.getBean(Phone.class));

    }
}
