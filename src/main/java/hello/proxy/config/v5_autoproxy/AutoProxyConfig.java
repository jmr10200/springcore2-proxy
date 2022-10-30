package hello.proxy.config.v5_autoproxy;

import hello.proxy.config.AppV1Config;
import hello.proxy.config.AppV2Config;
import hello.proxy.config.v3_proxyfactory.advice.LogTraceAdvice;
import hello.proxy.trace.logtrace.LogTrace;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({AppV1Config.class, AppV2Config.class})
public class AutoProxyConfig {

    /**
     * NameMatchMethodPointcut : 메소드명만 지정 (request*)
     * 이름만 일치하면 프록시가 만들어지므로
     * 의도하지않은 메소드도 호출되는 문제 발생
     */
//    @Bean
    public Advisor advisor1(LogTrace logTrace) {
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();

        pointcut.setMappedNames("request*", "order*", "save*");
        // 위와 같은 셋팅으로는 아래의 로그도 출력되는 것을 확인할 수 있다.
        // EnableWebMvcConfiguration.requestMappingHandlerAdapter()
        // request* 으로 설정되어 request 만 있으면 프록시가 만들어지도 어드바이스도 적용되는 것이다.
        // 즉, 패키지에 메소드명까지 함께 지정할 수 있는 정밀한 포인트컷이 필요하다.
        LogTraceAdvice advice = new LogTraceAdvice(logTrace);

        // advisor = pointcut + advice
        return new DefaultPointcutAdvisor(pointcut, advice);
    }

    /**
     * AspectJExpressionPointcut : 패키지에 메소드명까지 함께 지정할 수 있는 포인트컷
     * AOP 에 특화된 포인트컷 표현식을 적용
     */
//    @Bean
    public Advisor advisor2(LogTrace logTrace) {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        // hello.proxy.app 패키지와 그 하위패키지 모든 메소드는 포인트컷 매칭 대상 : no-log도 대상이된다.
        pointcut.setExpression("execution(* hello.proxy.app..*(..))");
        // * : 모든 반환타입
        // hello.proxy.app.. : 해당 패키지와 그 하위 패키지
        // *(..) : * 모든 메소드명, (..) 파라미터 상관없음
        LogTraceAdvice advice = new LogTraceAdvice(logTrace);
        // advisor = pointcut + advice
        return new DefaultPointcutAdvisor(pointcut, advice);
    }

    /**
     * no-log 까지 고려한 표현식
     */
    @Bean
    public Advisor advisor3(LogTrace logTrace) {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        // hello.proxy.app 패키지와 하위패키지의 모든 메소드를 매칭하되, noLog() 제외하라
        pointcut.setExpression("execution(* hello.proxy.app..*(..)) && !execution(* hello.proxy.app..noLog(..))");
        LogTraceAdvice advice = new LogTraceAdvice(logTrace);
        // advisor = pointcut + advice
        return new DefaultPointcutAdvisor(pointcut, advice);
    }

}
// 스프링부트 라이브러리 추가로 AOP 관련 클래스를 자동으로 스프링빈에 등록한다. : AopAutoConfiguration
// 빈 후처리기는 이제 등록하지 않아도 된다.
// 스프링은 자동 프록시 생성기라는 빈 후처리기를 자동으로 등록해준다. : AnnotationAwareAspectJAutoProxyCreator

/* 중요 */
// 포인트컷은 2가지에 사용된다.
// 1. 프록시 적용 여부 판단 - 생성 단계
// 자동 프록시 생성기는 포인트컷을 사용해서 해당 빈이 프록시를 생성할 필요가있는지 여부를 체크한다.
// 클래서 + 메소드 조건을 모두 비교한다. 이때 모든 메소드를 체크하는데, 포인트컷 조건에 하나하나 매칭해본다.
// 만약 조건에 맞는 것이 하나라도 있으면 프록시를 생성한다.
// 예) orderControllerV1 은 request(), noLog() 있다. 여기서 request() 만 조건 OK 이므로 프록시를 생성한다.
// 만약 조건이 맞는 것이 하나도 없으면 프록시를 생성하지 않는다.

// 2. 어드바이스 적용 여부 판단 - 사용 단계
// 프록시가 호출되었을 때 부가기능인 어드바이스를 적용할지 말지 포인트컷을 보고 판단한다.
// 예) orderControllerV1 은 이미 프록시가 걸려있다.
// orderControllerV1 의 request() 는 현재 포인트컷 조건에 만족하므로 프록시는 어드바이스를 먼저 호출하고, target 호출한다.
// orderControllerV1 의 noLog() 는 현재 포인트컷 조건에 만족하지 않으므로 어드바이스를 호출하지 않고 바로 target 만 호출한다.

// 참고
// 프록시를 모든 곳에 생성하는 것은 비용 낭비이다. 꼭 필요한 곳에 최소한의 프록시를 적용해야한다.
// 그래서 자동 프록시 생성기는 모든 스프링빈에 적용하는 것이 아니라 포인트컷으로
// 한번 필터링해서 어드바이스가 사용될 가능성이 있는 곳에만 프록시를 생성한다.