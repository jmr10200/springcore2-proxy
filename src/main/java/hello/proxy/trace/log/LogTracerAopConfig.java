package hello.proxy.trace.log;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogTracerAopConfig {

    @Bean
    public LogTracerAspect logTraceAspect(LogTracer logTrace) {
        return new LogTracerAspect(logTrace);
    }
}
