package hello.proxy;

import hello.proxy.code.ProxyPatternClient;
import hello.proxy.code.RealSubject;
import org.junit.jupiter.api.Test;

public class ProxyPatternTest {

    @Test
    void noProxyPattern() {
        RealSubject realSubject = new RealSubject();
        ProxyPatternClient client = new ProxyPatternClient(realSubject);
        client.execute();
        client.execute();
        client.execute();
        // 1초씩 걸리므로 3번 호출하면 3초가 소요된다.
        // 한번 조회후 변하지 않는 데이터라면 어딘가에 보관해뒀다가 사용하는게 성능상 좋다.
        // 이런 것을 캐시라고 한다.
        // 프록시 패턴의 주요 기능은 접근제어인데, 캐시도 접근 자체를 제어하는 기능중 하나이다.
    }
}
