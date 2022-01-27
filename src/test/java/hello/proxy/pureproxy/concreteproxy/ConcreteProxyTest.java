package hello.proxy.pureproxy.concreteproxy;

import hello.proxy.pureproxy.concreteproxy.code.ConcreteClient;
import hello.proxy.pureproxy.concreteproxy.code.ConcreteLogic;
import hello.proxy.pureproxy.concreteproxy.code.TimeProxy;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class ConcreteProxyTest {

    @Test
    void noProxy(){
        ConcreteLogic concreteLogic = new ConcreteLogic();
        ConcreteClient concreteClient = new ConcreteClient(concreteLogic);
        concreteClient.execute();
    }

    @Test
    void addProxy(){
        ConcreteLogic concreteLogic = new ConcreteLogic();
        TimeProxy timeProxy = new TimeProxy(concreteLogic);
        ConcreteClient concreteClient = new ConcreteClient(timeProxy);
        concreteClient.execute();
        // ConcreteClient concreteClient = new ConcreteClient(timeProxy);
        // 이 부분에서 타임프록시가 콘트리트 로직을 상속받았기 때문에 timeProxy가 인자로 들어갈 수도 있으며
        // 이 부분에서 타임프록시 대신에 콘크리트 로직을 넣어도 된다.
        // 인터페이스가 없어도 이런식으로 다형성인 상속 오버라이드를 이용해서 넣어줄 수 있다.
        // 기본적인 내용을 이야기 했지만 인터페이스가 없어도 오버라이드라는 다형성으로 프록시패턴을 이용할 수 있다는 것을 말하고 싶엇다.

        //시간을 측정하는 부가 기능을 제공하는데 인터페이스가 아닌 클래스로 이루어진다. 프록시를 중간에 둠으로써 대체가능한 프록시를 만들었다.
//        23:05:10.193 [main] INFO hello.proxy.pureproxy.concreteproxy.code.TimeProxy - TimeDecorator 실행
//        23:05:10.193 [main] INFO hello.proxy.pureproxy.concreteproxy.code.ConcreteLogic - ConcreteLogic 실행
//        23:05:10.193 [main] INFO hello.proxy.pureproxy.concreteproxy.code.TimeProxy - Time decorator end resultTime = 0ms
    }
}
