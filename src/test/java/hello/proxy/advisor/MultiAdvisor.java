package hello.proxy.advisor;

import hello.proxy.common.ServiceImpl;
import hello.proxy.common.ServiceInterface;
import hello.proxy.common.advice.TimeAdvice;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

import java.security.Provider;

/*
 * 여기서는 하나의 타겟에 여러 어드바이저를 거는 것을 해본다.
 * client -> proxy2 (advisor2 호출) -> proxy1 (advisor1 호출) -> target
 * 이 순서로 호출하게 된다.
 * 여러개의 프록시로 어드바이저를 여러개 걸어보자.
 */
@Slf4j
public class MultiAdvisor {

    @Test
    @DisplayName("여러 프록시")
    public void multiAdvisorTest1() {
        //client -> proxy2 (advisor2 호출) -> proxy1 (advisor1 호출) -> target
        //우선 프록시 1을 생성한다.

        ServiceInterface target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.setProxyTargetClass(true);

       // NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
       // pointcut.setMappedName("save");

        proxyFactory.addAdvisor(new DefaultPointcutAdvisor(Pointcut.TRUE, new Advice1()));
        ServiceInterface proxy1 = (ServiceInterface) proxyFactory.getProxy();
        //일단 기존의 방식과 동일하게 프록시 1을 생성한다

        //다음부터는 프록시2에 대해서 advice2를 적용할 것이기 때문에 프록시에 프록시를 거는 방식으로 해야한다.
        //프록시 팩토리를 다시 만들어줘야 한다.

        ProxyFactory proxyFactory2 = new ProxyFactory(proxy1);
        proxyFactory2.addAdvisor(new DefaultPointcutAdvisor(Pointcut.TRUE, new Advice2()));
        ServiceInterface proxy2 = (ServiceInterface) proxyFactory2.getProxy();

        proxy2.find();
        proxy2.save();
        /*
         * 이 방법의 단점은 만약 어드바이스가 늘어나면 그만큼 프록시를 계속 생성해서 걸고 또 걸어야한다.
         * 그래서 스프링은 프록시에 여러개의 어드바이스를 거는 기능을 제공한다.
         *
         * 실제 스프링 AOP 에서는 타겟 마다 하나의 프록시만을 생성한다.
         */
    }

    static class Advice1 implements MethodInterceptor{

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            log.info("advice1 호출");
            return invocation.proceed();
        }
    }

    static class Advice2 implements MethodInterceptor{

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            log.info("advice2 호출");
            return invocation.proceed();
        }
    }
}
