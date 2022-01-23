package hello.proxy.pureproxy.proxy;

import hello.proxy.pureproxy.proxy.code.CacheProxy;
import hello.proxy.pureproxy.proxy.code.ProxyPatternClient;
import hello.proxy.pureproxy.proxy.code.RealSubject;
import hello.proxy.pureproxy.proxy.code.Subject;
import org.junit.jupiter.api.Test;

public class ProxyPatternTest {


    @Test
    public void noProxyTest() {
        //given
        RealSubject realSubject = new RealSubject();
        ProxyPatternClient client = new ProxyPatternClient(realSubject);
        //when
        client.execute();
        client.execute();
        client.execute();
        //then

        /*      Result.
         *   02:26:42.720 [main] INFO hello.proxy.pureproxy.proxy.code.RealSubject - 실제 객체 호출
         *   02:26:43.735 [main] INFO hello.proxy.pureproxy.proxy.code.RealSubject - 실제 객체 호출
         *   02:26:44.750 [main] INFO hello.proxy.pureproxy.proxy.code.RealSubject - 실제 객체 호출
         *      총 3 초의 시간이 걸렸음.
         *
         *   하지만 이렇게 똑같은 정보를 조회하는데 불필요하게 클라이언트가 서브젝트에 계속 요청을 던지는 것은 불필요하다. "data"
         *   성능을 더 좋게 하기 위해서는 불변하지 않는 데이터라는 가정하에 해당 처음에 조회했던 데이터를 어딘가에 보관해두고
         *   두고두고 사용합니다. 이런 것을 캐싱이라고 하는데 이번엔 캐싱으로 적용해보겠다.
         */

        //캐시가 적용된 테스트 케이스
    }

    @Test
    void cacheProxyTest() {
        Subject realSubject = new RealSubject();
        Subject cacheProxy = new CacheProxy(realSubject);
        ProxyPatternClient client = new ProxyPatternClient(cacheProxy);
        client.execute();
        client.execute();
        client.execute();
        /* 프록시가 호출한 대상을 타겟이라고 한다. 접근제어 , 캐싱
         *
         * 18:12:00.417 [main] INFO hello.proxy.pureproxy.proxy.code.CacheProxy - Call Proxy
         * 18:12:00.419 [main] INFO hello.proxy.pureproxy.proxy.code.RealSubject - 실제 객체 호출
         * 18:12:01.422 [main] INFO hello.proxy.pureproxy.proxy.code.CacheProxy - Call Proxy
         * 18:12:01.422 [main] INFO hello.proxy.pureproxy.proxy.code.CacheProxy - Call Proxy
         */
    }
}
