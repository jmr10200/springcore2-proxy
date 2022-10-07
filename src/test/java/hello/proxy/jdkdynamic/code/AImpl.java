package hello.proxy.jdkdynamic.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AImpl implements AInterface {
    @Override
    public String call() {
        log.info("A 호출");
        return "a";
    }
}
/* JDK 동적 프록시 */
// 동적 프록시 기술을 사용하면 개발자가 직접 프록시 클래스를 만들지 않아도 된다.
// 이름 그대로 프록시 객체를 동적으로 런타임에 개발자 대신 만들어준다.
// 그리고 동적 프록시에 원하는 실행 로직을 지정할 수 있다.

// 주의
// JDK 동적 프록시는 인터페이스를 기반으로 프록시를 동적으로 만들어준다.
// 따라서 인터페이스가 필수이다.