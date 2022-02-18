package hello.proxy.cglib;

import hello.proxy.common.ConcreteService;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/*
 * CGlib 을 사용하기 위해서는 jdk 가 제공하는 동적 프록시 생성 방식과 비슷하게 methodInterceptor 라는 인터페이스를 구현해주어야 한다.
 */
@Slf4j
public class TimeMethodInterceptor implements MethodInterceptor {

    private final Object target;

    public TimeMethodInterceptor(ConcreteService target) {
        this.target = target;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        //1. Object o 적용될 타겟.
        //2. 호출될 메서드.
        //3. 메서드를 호출하면서 전달될 인수들.
        //4. proxy 라는 것은 메서드를 어떻게 하면 좀 더 빨리 호출할 수 있을지에 대한 객체로서 다음에서 설명한다.

        log.info("TimeProxy 실행");
        long startTime = System.currentTimeMillis();


        Object result = methodProxy.invoke(target, args);
        Object invoke = method.invoke(target, args);


        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;

        log.info("TimeProxy 종료");

        return result;
    }
}
