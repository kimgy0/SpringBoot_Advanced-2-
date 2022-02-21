package hello.proxy.config.v4_postprocessor.postprocessor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.NameMatchMethodPointcutAdvisor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

@Slf4j
public class PackageLogTracePostProcessor implements BeanPostProcessor {

    private final String basePackage;
    private final Advisor advisor;

    public PackageLogTracePostProcessor(String basePackage, Advisor advisor) {
        this.basePackage = basePackage;
        this.advisor = advisor;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        log.info("param beanName = {}, bean = {}", beanName, bean);
        //프록시 적용 대상 여부를 체크하고,
        //프록시 적용 대상이 아니면 원본을 그대로 진행.


        String packageName = bean.getClass().getPackageName();
        if(!packageName.startsWith(basePackage)){
            return bean;
            //베이스 패키지를 String 형태로 받아서 이 패키지 하위에 있는 패키지가 아니면 일반 빈으로 진행시키고,
            //하위 패키지 이면 if 를 탈출해서 프록시를 만들어서 프록시 객체로 만들어 줄 것이다.
        }

        //프록시 대상이면 프록시를 만들어서 반환한다.
        ProxyFactory proxyFactory = new ProxyFactory(bean);
        //타겟이 들어가야하는 자리에 bean 을 넣어주면 된다.
        proxyFactory.addAdvisor(advisor);
        proxyFactory.setProxyTargetClass(true);
        Object proxy = proxyFactory.getProxy();

        log.info("create proxy : target = {} proxy = {}", bean.getClass(), proxy.getClass());
        return proxy;
    }
}
