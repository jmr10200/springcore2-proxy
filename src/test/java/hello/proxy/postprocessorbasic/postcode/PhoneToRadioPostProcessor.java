package hello.proxy.postprocessorbasic.postcode;

import hello.proxy.postprocessorbasic.code.Phone;
import hello.proxy.postprocessorbasic.code.Radio;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * 스프링 제공 BeanPostProcessor
 */
@Slf4j
public class PhoneToRadioPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        log.info("print beanName, bean \n => beanName = {} \n => bean = {}", beanName, bean);

        if (bean instanceof Phone) {
            return new Radio();
        }

        return bean;
    }
}
