package hello.proxy.config.v3_proxyfactory;

import hello.proxy.app.v1.*;
import hello.proxy.config.v3_proxyfactory.advice.LogTraceAdvice;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.cache.interceptor.NameMatchCacheOperationSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@Slf4j
public class ProxyFactoryConfigV1 {

    @Bean
    public OrderRepositoryV1 orderRepositoryV1(LogTrace logTrace){
        OrderRepositoryV1 orderRepositoryV1 = new OrderRepositoryV1Impl();
        ProxyFactory proxyFactory = new ProxyFactory(orderRepositoryV1);
        proxyFactory.addAdvisor(getAdvisor(logTrace));
        return (OrderRepositoryV1) proxyFactory.getProxy();
    }

    @Bean
    public OrderServiceV1 orderServiceV1(LogTrace logTrace){
        OrderServiceV1Impl orderServiceV1 = new OrderServiceV1Impl(orderRepositoryV1(logTrace));
        ProxyFactory proxyFactory = new ProxyFactory(orderServiceV1);
        proxyFactory.addAdvisor(getAdvisor(logTrace));
        return (OrderServiceV1) proxyFactory.getProxy();
    }

    @Bean
    public OrderControllerV1 orderControllerV1(LogTrace logTrace){
        OrderControllerV1 orderServiceV1 = new OrderControllerV1Impl(orderServiceV1(logTrace));
        ProxyFactory proxyFactory = new ProxyFactory(orderServiceV1);
        proxyFactory.addAdvisor(getAdvisor(logTrace));
        return (OrderControllerV1) proxyFactory.getProxy();
    }

    private Advisor getAdvisor(LogTrace logTrace) {
        //pointcut
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedNames("request*", "order", "save*");
        //advice
        LogTraceAdvice advice = new LogTraceAdvice(logTrace);
        return new DefaultPointcutAdvisor(pointcut,advice);
    }
    // 다 jdk 로 만들어짐.
}
