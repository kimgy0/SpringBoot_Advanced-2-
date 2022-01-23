package hello.proxy.pureproxy.decorator;

import hello.proxy.pureproxy.decorator.code.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.sql.Time;

@Slf4j
public class DecoratorPatternTest {
    @Test
    void noDecorator(){
        Component realComponent = new RealComponent();
        DecoratorPatternClient decoratorPatternClient = new DecoratorPatternClient(realComponent);
        decoratorPatternClient.execute();
        /*
         * 이번에는 프록시에 부가기능을 추가할 것이다. 이렇게 추가하는 패턴을 가지고 우리는 데코레이터 패턴이라고 한다.
         * 원래 서버가 제공하는 기능에 더해서 부가 기능을 수행할 때, 요청값이나 응답값을 중간에 변형, 실행시간 예측 로그
         *
         * 응답값을 꾸며주는 데코레이터를 만든다.
         *
         * 18:54:19.746 [main] INFO hello.proxy.pureproxy.decorator.code.RealComponent - RealComponent 실행
            18:54:19.748 [main] INFO hello.proxy.pureproxy.decorator.code.DecoratorPatternClient - result = data
         *
         * 클라이언트 -> 메세지 데코레이터 -> 리얼컴포넌트 의 구조를 통해서
         * 메세지 데코레이터가 프록시 중간매개체 역할을 할 수 있도록 하면서
         * 중간에 operation 메서드를 실행한다.
         *
         * 즉, 프록시와 컴포넌트는 하나의 인터페이스만을 가지고 있어야 한다.
         */
    }

    @Test
    void decorator1(){
        Component realComponent = new RealComponent();
        Component messageDecorator = new MessageDecorator(realComponent);
        DecoratorPatternClient client = new DecoratorPatternClient(messageDecorator);
        client.execute();

        //result = ******data******
        //중간에 프록시를 가지고 있는 구조.

        /*
         * 우리의 요구사항은 코드 수정이 최소화될 수 있어야 될 것 같다.
         */
    }

    @Test
    public void decorator2() {
        Component realComponent = new RealComponent();
        Component messageDecorator = new MessageDecorator(realComponent);
        Component timeDecorator = new TimeDecorator(messageDecorator);
        DecoratorPatternClient client = new DecoratorPatternClient(timeDecorator);
        client.execute();
    }
}
