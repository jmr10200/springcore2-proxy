package hello.proxy.postprocessorbasic;

import hello.proxy.postprocessorbasic.code.BasicConfig;
import hello.proxy.postprocessorbasic.code.Phone;
import hello.proxy.postprocessorbasic.code.Radio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class BasicTest {

    @Test
    void basicConfig() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(BasicConfig.class);

        // Phone 빈 등록 OK
        Phone phone = applicationContext.getBean("beanPhone", Phone.class);
        phone.phoneCall();

        // Radio 빈 등록 NG
        Assertions.assertThrows(NoSuchBeanDefinitionException.class, () -> applicationContext.getBean(Radio.class));

    }

}
