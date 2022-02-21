package hello.proxy.config.v5_autoproxy;

import hello.proxy.config.AppV1Config;
import hello.proxy.config.AppV2Config;
import hello.proxy.config.v3_proxyfactory.advice.LogTraceAdvice;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Slf4j
@Import({AppV2Config.class , AppV1Config.class})
public class AutoProxyConfig {

 //   @Bean
    public Advisor advisor1(LogTrace logTrace){
        //pointcut
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedNames("request*", "save*", "order*");
        /*
         * 첫째로, 스프링이 제공하는 빈 후처리기가 예를 들어 request, save, order 이중에 하나라도 일치하는 것이 있으면 프록시를 만들어낸다.
         *          즉 하나라도 얻어 걸리면 프록시를 만든다는 뜻이 이 뜻이다.
         * 둘째로, 스프링이 제공하는 빈 후처리기가 만든 프록시는 어드바이저가 걸려있으므로 그 어드바이저에서 포인트 컷을 찾아서 또 매칭시켜본다.
         *          그렇게 매칭이 되면 어드바이스를 실행하게 되는 것이다.
         * 이렇게 두가지 역할을 우리는 빈 후처리기를 만들고 어드바이저를 만들면서 따로 따로 만들었지만, 스프링이 제공하는 빈 후처리기는 이 것을 통합한다.
         *
         * 모든 곳에 프록시를 생성하는 것은 낭비이다. 꼭 필요한 곳에만 프록시를 생성해야하는데 그 것을 필터링 해주는 것이 포인트 컷이다.
         * 포인트 컷으로 필터링 해서 어드바이스가 사용될 가능성이 조금이라도 있는 경우에만 프록시를 생성해주는 것이 바람직하다.
         */

        LogTraceAdvice logTraceAdvice = new LogTraceAdvice(logTrace);
        return new DefaultPointcutAdvisor(pointcut,logTraceAdvice);
    }

    /*
     * 이렇게 어드바이저 하나만 등록해도 스프링이 제공하는 자동으로 처리할 수 있는 빈 후처리기가 있기 때문에
     * 가능한 일이다.
     * AnnotationAwareAspectJAutoProxyCreator.class
     */

   // @Bean
    public Advisor advisor2(LogTrace logTrace){
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* hello.proxy.app..*(..))");

        LogTraceAdvice logTraceAdvice = new LogTraceAdvice(logTrace);
        return new DefaultPointcutAdvisor(pointcut,logTraceAdvice);
    }

    @Bean
    public Advisor advisor3(LogTrace logTrace){
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* hello.proxy.app..*(..)) && !execution(* hello.proxy.app..noLog(..))");

        LogTraceAdvice logTraceAdvice = new LogTraceAdvice(logTrace);
        return new DefaultPointcutAdvisor(pointcut,logTraceAdvice);
    }
    /*
     * 만약 예를 들어서 스프링 빈이 advisor 1과 2를 다 만족하면 어떻게 되는가?
     * 프록시 팩토리에서 프록시를 단 하나만 생성한다. 그리고 프록시는 이전에 말했다시피 어드바이저를 여러개 가질 수 있다.
     * 따라서 프록시를 여러 개 생성해서 자원낭비를 할 필요가 없는 것이다.
     *
     */
}
