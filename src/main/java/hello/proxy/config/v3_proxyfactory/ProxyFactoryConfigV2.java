package hello.proxy.config.v3_proxyfactory;

import hello.proxy.app.v1.*;
import hello.proxy.app.v2.OrderControllerV2;
import hello.proxy.app.v2.OrderRepositoryV2;
import hello.proxy.app.v2.OrderServiceV2;
import hello.proxy.config.v3_proxyfactory.advice.LogTraceAdvice;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class ProxyFactoryConfigV2 {

    @Bean
    public OrderRepositoryV2 orderRepositoryV2(LogTrace logTrace){
        OrderRepositoryV2 orderRepositoryV2 = new OrderRepositoryV2();
        ProxyFactory proxyFactory = new ProxyFactory(orderRepositoryV2);
        proxyFactory.addAdvisor(getAdvisor(logTrace));
        return (OrderRepositoryV2) proxyFactory.getProxy();
    }

    @Bean
    public OrderServiceV2 orderServiceV2(LogTrace logTrace){
        OrderServiceV2 orderServiceV2 = new OrderServiceV2(orderRepositoryV2(logTrace));
        ProxyFactory proxyFactory = new ProxyFactory(orderServiceV2);
        proxyFactory.addAdvisor(getAdvisor(logTrace));
        return (OrderServiceV2) proxyFactory.getProxy();
    }

    @Bean
    public OrderControllerV2 orderControllerV2(LogTrace logTrace){
        OrderControllerV2 orderServiceV2 = new OrderControllerV2(orderServiceV2(logTrace));
        ProxyFactory proxyFactory = new ProxyFactory(orderServiceV2);
        proxyFactory.addAdvisor(getAdvisor(logTrace));
        return (OrderControllerV2) proxyFactory.getProxy();
    }

    private Advisor getAdvisor(LogTrace logTrace) {
        //pointcut
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedNames("request*", "order", "save*");
        //advice
        LogTraceAdvice advice = new LogTraceAdvice(logTrace);
        return new DefaultPointcutAdvisor(pointcut,advice);
    }
    // 다 cglib으로 만들어짐.
}
