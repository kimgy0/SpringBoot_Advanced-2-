package hello.proxy.jdkdinamic.code;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@Slf4j
public class TimeInvocationHandler implements InvocationHandler {

    private final Object target;

    public TimeInvocationHandler(Object target) {
        this.target = target;
    }

    /*
        프록시가 호출하는 메서드로서 이렇게 해야 동적으로 적용할 공통로직을 개발할 수 있다.
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("TimeProxy 실행");
        long startTime = System.currentTimeMillis();


        //1. Method method ( 어떤 메서드가 호출될지 넘어옴)
        //2. Object[] args 메서드에 파라미터로 넘어올 인자들은 이렇게 args 를 이용해 작성한다.
        //3. private final Object target; 동적 프록시가 호출할 대상.
        Object invoke = method.invoke(target, args);
        //리플렉션을 이용해서 target의 인스턴스 메서드를 실행한다.


        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;

        log.info("TimeProxy 종료");

        return null;
    }
}
