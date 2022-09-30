package hello.proxy;

import hello.proxy.code.CacheProxy;
import hello.proxy.code.ProxyPatternClient;
import hello.proxy.code.RealSubject;
import hello.proxy.code.Subject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class ProxyPatternTest {

    @Test
    void noProxyTest() {
        RealSubject realSubject = new RealSubject();
        ProxyPatternClient client = new ProxyPatternClient(realSubject);
        Long startTime = System.currentTimeMillis();
        client.execute();
        client.execute();
        client.execute();
        Long endTime = System.currentTimeMillis();
        Long resultTime = endTime - startTime;
        log.info("resultTime={}", resultTime);
        // 1초씩 걸리므로 3번 호출하면 3초가 소요된다.
        // 한번 조회후 변하지 않는 데이터라면 어딘가에 보관해뒀다가 사용하는게 성능상 좋다.
        // 이런 것을 캐시라고 한다.
        // 프록시 패턴의 주요 기능은 접근제어인데, 캐시도 접근 자체를 제어하는 기능중 하나이다.
    }

    @Test
    void cacheProxyTest() {
        Subject realSubject = new RealSubject();
        Subject cacheProxy = new CacheProxy(realSubject);
        // client 에 realSubject 가 아닌 cacheProxy 를 주입
        ProxyPatternClient client = new ProxyPatternClient(cacheProxy);
        // client -> cacheProxy -> realSubject 런타임 객체 의존관계 완성
        Long startTime = System.currentTimeMillis();
        client.execute();
        client.execute();
        client.execute();
        Long endTime = System.currentTimeMillis();
        Long resultTime = endTime - startTime;
        log.info("resultTime={}", resultTime);
        // 두번째 부터는 realSubject 가아닌 cacheProxy 를 호출하므로 성능상 더 좋다.
    }
}
// 프록시 패턴의 핵심은 RealSubject 코드와 클라이언트 코드를 변경하지 않고,
// 프록시를 도입해서 접근제어를 했다는 점이다.
// 그리고 클라이언트 코드의 변경 없이 자유롭게 프록시를 넣고 뺄수 있다.
// 실제 클라이언트 입장에서는 프록시 객체가 주입되었는지, 실제 객체가 주입되었는지 알지 못한다.
