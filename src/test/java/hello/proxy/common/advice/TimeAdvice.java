package hello.proxy.common.advice;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

@Slf4j
public class TimeAdvice implements MethodInterceptor {

    /*
     * advice 를 만들기 위해서는 우선 import org.aopalliance.intercept.MethodInvocation; 여기에 있는 인터페이스를 구현해야한다.
     * 인터페이스를 구현하게 되면 인자로 MethodInvocation invocation 가 나오는데 이 안에 args 라던지 모든 정보가 다 들어있다.
     *
     */

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        log.info("TimeProxy 실행");
        long startTime = System.currentTimeMillis();

        //method.invoke(target, args);
        //기존에는 method 를 가지고 invoke() 메서드에 타겟을 넣어서 실행해 주어야 실행이 됐었다.
        //하지만 어드바이스에서는 프록시 팩토리에서 타겟을 자동으로 진작에 생성하고 집어넣기 때문이다.
        Object result = invocation.proceed(); // 를 통해서 자동으로 타겟을 꺼낼 수 있다.

        //invocation.getMethod() 등등 모든 정보를 invocation 이 가지고 있다는 것을 명심하자.

        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("TimeProxy 종료 resultTime = {}ms",resultTime);
        return result;
    }
}
