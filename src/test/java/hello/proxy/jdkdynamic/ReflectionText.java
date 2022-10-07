package hello.proxy.jdkdynamic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Slf4j
public class ReflectionText {

    /**
     * 정적 코드
     */
    @Test
    void reflection0() {
        Hello target = new Hello();

        // 공통 로직1 시작
        log.info("start");
        String result1 = target.callA(); // 호출하는 메소드가 다름
        log.info("result = {}", result1);
        // 공통 로직1 종료

        // 공통 로직1 시작
        log.info("start");
        String result2 = target.callB(); // 호출하는 메소드가 다름
        log.info("result = {}", result2);
        // 공통 로직1 종료

        // 공통 로직1, 공통로직2를 하나의 메소드로 뽑아서 합칠수 있을까?
        // 동적으로 처리하면 해결할 수 있을것 같다.
    }

    /**
     * 리플랙션 기술
     * 클래스 메타정보, 메소드 정보 취득하는 법
     */
    @Test
    void reflection1() throws Exception {
        // 클래스 메타정보 획득 (내부 클래스는 $사용)
        Class classHello = Class.forName("hello.proxy.jdkdynamic.ReflectionText$Hello");

        Hello target = new Hello();

        // callA 메소드 정보
        Method methodCallA = classHello.getMethod("callA");
        Object result1 = methodCallA.invoke(target); // 해당 인스턴스의 callA() 메소드 호출
        log.info("result1 = {}", result1);

        // callA 메소드 정보
        Method methodCallB = classHello.getMethod("callB");
        Object result2 = methodCallB.invoke(target);
        log.info("result2 = {}", result2);

        // 클래스 메타정보, 메소드 정보를 취득하여 동적으로 실행할 수 있다.

    }

    /**
     * 클래스 메타정보, 메소드 정보를 취득하여
     * 동적으로 메소드 실행
     */
    @Test
    void reflection2() throws Exception {

        Class classHello = Class.forName("hello.proxy.jdkdynamic.ReflectionText$Hello");

        Hello target = new Hello();
        Method methodCallA = classHello.getMethod("callA");
        dynamicCall(methodCallA, target);

        Method methodCallB = classHello.getMethod("callB");
        dynamicCall(methodCallB, target);

        // 정적인 target.callA(), target.callB() 코드를 리플렉션을 이용하여
        // Method 라는 메타정보로 추상화 해서 공통 로직을 만들 수 있다.
    }

    private void dynamicCall(Method method, Object target) throws Exception {
        log.info("start");
        Object result = method.invoke(target);
        log.info("result = {}", result);
    }

    static class Hello {

        public String callA() {
            log.info("callA");
            return "A";
        }

        public String callB() {
            log.info("callB");
            return "B";
        }
    }

}
/* 주의 */
// 리플렉션을 사용하면 클래스와 메소드의 메타 정보를 사용해서 어플리케이션을 동적으로 유연하게 만들수 있다.
// 하지만 리플렉션 기술은 런타임에 동작하기 때문에, 컴파일시점에 에러를 잡을 수 없다.
// 예를들어 getMethod("callA") 를 getMethod("caallA") 라고 잘못 입력해도 컴파일 에러가 발생하지 않는다.
// 그러나 직접 실행되는 런타임 시점에 에러가 발생한다.

// 따라서 리플렉션은 일반적으로 사용하면 안된다.
// 프로그래밍 언어는 타입정보를 기반으로 컴파일 시점에 에러를 잡을 수 있도록 발전해왔는데,
// 리플렉션은 이를 역행하는 기술이기 때문이다.
// 즉, 매우 일반적인 공통 처리가 필요할 때 부분적으로 주의해서 사용해야한다.