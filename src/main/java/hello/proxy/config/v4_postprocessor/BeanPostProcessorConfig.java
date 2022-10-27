package hello.proxy.config.v4_postprocessor;

import hello.proxy.config.AppV1Config;
import hello.proxy.config.AppV2Config;
import hello.proxy.config.v3_proxyfactory.advice.LogTraceAdvice;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Slf4j
@Configuration
@Import({AppV1Config.class, AppV2Config.class})
public class BeanPostProcessorConfig {

    @Bean
    public PackageLogTraceProxyProcessor logTraceProxyProcessor(LogTrace logTrace) {
        return new PackageLogTraceProxyProcessor("hello.proxy.app", getAdvisor(logTrace));
    }

    private Advisor getAdvisor(LogTrace logTrace) {
        // pointcut
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedNames("request*", "order*", "save*");

        // advice
        LogTraceAdvice advice = new LogTraceAdvice(logTrace);

        // advisor = 1 pointcut + 1 advice
        return new DefaultPointcutAdvisor(pointcut, advice);
    }
}
// 프록시 적용 대상 여부 체크
// 어플리케이션을 실행후 로그를 확인하면, 직접 등록한 스프링 빈 뿐만아니라
// 스프링 부트가 기본으로 등록하는 수 많은 빈들이 빈 후처리기에 넘어온다.
// 따라서 어떤 빈을 프록시로 만들것인지 기준이 필요하다.
// 여기서는 간단히 basePackage 를 사용해서 특정 패키지를 기준으로 해당 패키지와 그 하위 패키지의 빈들을 프록시로 만든다.
// 스프링 부트가 기본으로 제공하는 빈 중에서는 프록시 객체를 만들 수 없는 빈들도 있다.
// 따라서 모든 객체를 프록시로 만들 경우 오류가 발생한다.