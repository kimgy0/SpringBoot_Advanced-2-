package hello.proxy.config.v4_postprocessor;

import hello.proxy.config.AppV1Config;
import hello.proxy.config.AppV2Config;
import hello.proxy.config.v3_proxyfactory.advice.LogTraceAdvice;
import hello.proxy.config.v4_postprocessor.postprocessor.PackageLogTracePostProcessor;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.springframework.aop.Advisor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Slf4j
@Configuration
@Import({AppV1Config.class, AppV2Config.class})
public class BeanPostProcessorConfig {
    @Bean
    public PackageLogTracePostProcessor logTracePostProcessor(LogTrace logTrace){
        /*
         * @Import({AppV1Config.class, AppV2Config.class})
         * 이걸로 등록 되어있던 config 빈들에 대해서 해당 조건이 맞으면 그 빈에 대해서 프록시를 반환한다.
         *
         * 왜 베이스 패키지 명을 지정해서 프록시를 걸러서 반환하는가?
         * 스프링 내부에서 쓰이는 빈들은 무수히 많다. 내가 모르는 것들도 정말 많이 등록되기 때문에 하위 패키지 명을 지정해서
         * 우선순위를 가지고 있는 postProcessor 를 사용할 때는는등록 해줘야 한다.
         *
         * 지금 까지 프록시 팩토리를 사용하면서 프록시 팩토리를 일일이 다 만들어줬다.
         * 하지만 PostProcessor 가 그럴 필요 없이 자동으로 찍어낸다.
         */
        return new PackageLogTracePostProcessor("hello.proxy.app", getAdvisor(logTrace));
    }

    // 포인트 컷을 만드는 방법 이전에 설명함.
    private Advisor getAdvisor(LogTrace logTrace) {
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedNames("request*", "order*","save*");
        /*
         * 포인트 컷으로 세세한 메서드를 판단하고
         * 빈 후처리기로 패키지나 그 윗 단의 경로를 확인한다.
         * 만약, 세세한 메서드의 적용이 필요가 없다면 포인트 컷 (프록시) 을 적용할 필요가 없는 것이다.
         */

        LogTraceAdvice logTraceAdvice = new LogTraceAdvice(logTrace);
        return new DefaultPointcutAdvisor(pointcut,logTraceAdvice);
    }
}
