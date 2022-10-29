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

/* 정리 */
// 문제1. 너무 많은 설정
// 프록시를 직접 스프링 빈으로 등록하는 ProxyFactoryConfigV1, ProxyFactoryConfigV2 와 같은
// 설정 파일은 프록시 관련 설정이 지나치게 많다는 문제가 있다.
// 어플리케이션에 스프링빈이 필요한 만큼 설정코드가 필요해진다.
// 스프링 빈을 편리하게 등록하려고 컴포넌트 스캔을 사용하는데, 프록시 적용 코드를 작성해야한다.

// 문제2. 컴포넌트 스캔
// 어플리케이션 V3 처럼 컴포넌트 스캔을 사용하는 경우 직접 등록코드로 프록시 적용이 불가능했다.
// 컴포넌트 스캔으로 이미 스프링 컨테이너에 실제 객체를 스프링 빈으로 등록을 다 해버린 상태이기 때문이다.
// ProxyFactoryConfigV1 에서 한 것 처럼, 프록시를 원본객체 대신 스프링 컨테이너에 빈으로 등록해야한다.
// 그런데 컴포넌트 스캔은 원본 객체를 스프링 빈으로 자동으로 등록하기 때문에 프록시 적용이 불가능하다.

// 문제해결
// 빈 후처리기 때문에 프록시를 생성하는 부분을 하나로 집중할 수 있다.
// 그리고 컴포넌트 스캔처럼 스프링이 직접 대상을 빈으로 등록하는 경우에도 중간에 빈 등록과정을
// 가로채서 원본 대신에 프록시를 스프링빈으로 등록할 수 있다.
// 덕분에 어플리케이션에 수 많은 스프링 빈이 추가되어도 프록시와 관련된 코드는
// 전혀 변경하지 않아도 된다. 컴포넌트 스캔을 사용해도 프록시가 적용된다.
// 스프링은 프록시를 생성하기 위한 빈 후처리기를 이미 만들어서 제공한다.

/* 중요 */
// 프록시의 적용 대상 여부를 여기서는 간단히 패키지 기준으로 설정했다.
// 포인트컷은 이미 클래스, 메소드 단위의 필터 기능을 가지고 있기 떄문에,
// 프록시 적용 대상 여부를 정밀하게 설정할 수 있다.
// 참고로 어드바이저는 포인트컷을 가지고 있다. 따라서 어드바이저를 통해 포인트컷을 확인할 수 있다.
// 스프링 AOP 는 포인트컷을 사용해서 프록시 적용 대상 여부를 체크한다.

// 결과적으로 포인트컷은 다음 두 곳에 사용된다.
// 1. 프록시 적용 대상 여부를 체크해서 꼭 필요한 곳에만 프록시를 적용 (빈후처리기, 자동프록시생성)
// 2. 프록시의 어떤 메소드가 호출되었을 때 어드바이스를 적용할지 판단 (프록시 내부)
