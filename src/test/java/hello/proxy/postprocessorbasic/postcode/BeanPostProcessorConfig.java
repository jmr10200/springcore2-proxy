package hello.proxy.postprocessorbasic.postcode;

import hello.proxy.postprocessorbasic.code.Phone;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class BeanPostProcessorConfig {

    @Bean(name = "beanPhone")
    public Phone phone() {
        return new Phone();
    }

    /** 빈 후처리기 */
    @Bean
    public PhoneToRadioPostProcessor toRadioProcessor() {
        return new PhoneToRadioPostProcessor();
    }
}
