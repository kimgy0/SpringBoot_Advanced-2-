package hello.proxy.pureproxy.decorator.code;

/*
프록시는 체인이 될 수 있다는 점을 기억하자.
데코레이터의 패턴은 핵심기능에
 */

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TimeDecorator implements Component{

    private Component component;

    public TimeDecorator(Component component) {
        this.component = component;
    }
    /*
     * 중복되는 코드는 추상클래스로 올릴 수 있음.
     */

    @Override
    public String operation() {
        log.info("timedecorator start");
        Long start = System.currentTimeMillis();
        String operation = component.operation();
        Long end = System.currentTimeMillis();
        Long resultTime = end - start;
        log.info("timedecorator end resultTime = {}ms",resultTime);
        return operation;
        /*
        19:25:05.012 [main] INFO hello.proxy.pureproxy.decorator.code.TimeDecorator - timedecorator start
19:25:05.013 [main] INFO hello.proxy.pureproxy.decorator.code.MessageDecorator - message decorator start
19:25:05.013 [main] INFO hello.proxy.pureproxy.decorator.code.RealComponent - RealComponent 실행
19:25:05.017 [main] INFO hello.proxy.pureproxy.decorator.code.MessageDecorator - message decorator end
19:25:05.017 [main] INFO hello.proxy.pureproxy.decorator.code.TimeDecorator - timedecorator end resultTime = 4ms
19:25:05.019 [main] INFO hello.proxy.pureproxy.decorator.code.DecoratorPatternClient - result = ******data******
체인 적용의 결과



            사실 프록시패턴과 데코레이터 패턴은 거의 같은 모양새를 유지하고 있다.
            여기서 둘을 구분하는 방법은 접근제어를 하냐 추가적인 기능을 동적으로 추가하는지 (기능확장에 대한 대안)
            즉, 의도에 따라서 구분을 하고 어떤 경우에는 프록시패턴과 데코레이터가 비슷한 경우가 많다.
            또 프록시와 데코레이터 패턴을 짬뽕해서 쓸 수도 있다.
         */
    }
}
