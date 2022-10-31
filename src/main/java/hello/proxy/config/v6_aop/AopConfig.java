package hello.proxy.config.v6_aop;

import hello.proxy.config.AppV1Config;
import hello.proxy.config.AppV2Config;
import hello.proxy.config.v6_aop.aspect.LogTraceAspect;
import hello.proxy.trace.logtrace.LogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({AppV1Config.class, AppV2Config.class}) // V1, V2 는 수동등록해야함
public class AopConfig {

    @Bean  // @Aspect 가 있어도 스프링빈 등록 해줘야함
    public LogTraceAspect logTraceAspect(LogTrace logTrace) {
        // LogTraceAspect 에 @Component 를 붙여서 스프링빈으로 등록하는 방법도 가능
        return new LogTraceAspect(logTrace);
    }
}
