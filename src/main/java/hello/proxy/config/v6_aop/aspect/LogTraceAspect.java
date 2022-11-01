package hello.proxy.config.v6_aop.aspect;

import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Slf4j
@Aspect // 어노테이션 기반 프록시 적용
public class LogTraceAspect {

    private final LogTrace logTrace;

    public LogTraceAspect(LogTrace logTrace) {
        this.logTrace = logTrace;
    }

    @Around("execution(* hello.proxy.app..*(..))") // pointcut
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable { // advice
        // ProceedingJoinPoint : MethodInvocation 과 유사한 기능
        // 내부에 실제 호출대상, 전달인자, 어떤 객체와 어떤 메소드가 호출되었는지 정보가 포함되어 있음
        TraceStatus status = null;

//        log.info("target={}", joinPoint.getTarget()); // 실제 호출 대상
//        log.info("getArgs={}", joinPoint.getArgs()); // 전달인자
//        log.info("getSignature={}", joinPoint.getSignature()); // join point 시그니처

        try {
            String message = joinPoint.getSignature().toShortString();
            status = logTrace.begin(message);

            // 로직 호출

            Object result = null;
            result = joinPoint.proceed(); // 실제 호출 대상(target)을 호출
            logTrace.end(status);
            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }
}
/* 자동프록시 생성기 */
// 1. @Aspect 를 Advisor 로 변환해서 저장한다.
// (1) 실행 : 스프링 어플리케이션 로딩 시점에 자동 프록시 생성기 호출
// (2) 모든 @Aspect 빈 조회 : 자동 프록시 생성기는 스프링 컨테이너에서 @Aspect 붙은 스프링 빈 모두 조회
// (3) 어드바이저 생성 : @Aspect 어드바이저 빌더로 @Aspect 기반 어드바이저 생성
// (4) @Aspect 기반 어드바이저 저정 : 생성한 어드바이저를 @Aspect 어드바이저 빌더 내부에 저장

// @Aspect 어드바이저 빌더 : BeanFactoryAspectJAdvisorBuilder 클래스
// @Aspect 정보 기반으로 포인트컷, 어드바이스, 어드바이저를 생성하고 보관하는 것을 담당
// 어드바이저를 만들고, 어드바이저 빌더 내부 저장소에 캐시한다.
// 캐시에 어드바이저가 이미 만들어져 있는 경우 캐시에 저장된 어드바이저를 반환한다.

// 2. 어드바이저를 기반으로 프록시 생성
// (1) 생성 : 스프링 빈 대상이 되는 객체를 생성한다 (@Bean, 컴포넌트 스캔 모두 포함)
// (2) 전달 : 생성된 객체를 빈 저장소에 등록하기 직전에 빈 후처리기에 전달한다.
// (3)-1 Advisor 빈 조회 : 스프링 컨테이너에서 Advisor 빈을 모두 조회한다.
// (3)-2 @Aspect Advisor 조회 : @Aspect 어드바이저 빌더 내부에 저장된 Advisor 를 모두 조회한다.
// (4) 프록시 적용 대상 체크 : (3)-1,-2 에서 조회한 Advisor 에 포함되어 있는 포인트컷을 사용해서
// 해당 객체가 프록시를 적용할 대상인지 아닌지 판단한다. 이때 객체 클래스 정보는 물론이고,
// 해당 객체의 모든 메소드를 포인트컷에 하나하나 모두 매칭해본다.
// 그래서 조건이 하나라도 만족하면 프록시 적용 대상이 된다.
// 예를 들어 메소드 하나만 포인트컷 조건에 만족해도 프록시 적용 대상이 된다.
// (5) 프록시 생성 : 프록시 적용 대상이면 프록시를 생성하고 프록시를 반환한다.
// 그래서 프록시를 스프링빈으로 등록한다. 만약 프록시 적용 대상이 아니라면 원본 객체를 반환해서
// 원본 객체를 스프링빈으로 등록한다
// (6) 빈 등록 : 반환된 객체는 스프링빈으로 등록된다.

// 정리
// @Aspect : 어노테이션 기반 프록시를 편리하게 적용할 수 있다. (실무사용)

/* 횡단 관심사 (cross-cutting concerns) */
// 로그를 남기는 기능은 특정 기능 하나에 관심있는 기능이 아니다.
// 어플리케이션의 여러기능에 걸쳐서 들어가는 관심사이다.
// 이러한 횡단 관심사를 해결하기 위한 방법이 어노테이션 기반 프록시가 된다.
// -> 횡단 관심사를 전문으로 해결하는 스프링 AOP