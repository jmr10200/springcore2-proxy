package hello.proxy.postprocessorbasic.code;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class BasicConfig {

    @Bean(name = "beanPhone")
    public Phone phone() {
        return new Phone();
    }

}
