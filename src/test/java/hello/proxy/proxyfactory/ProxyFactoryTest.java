package hello.proxy.proxyfactory;

import hello.proxy.common.ConcreteService;
import hello.proxy.common.ServiceImpl;
import hello.proxy.common.ServiceInterface;
import hello.proxy.common.advice.TimeAdvice;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;

import java.security.Provider;

@Slf4j
public class ProxyFactoryTest {

    @Test
    @DisplayName("인터페이스가 있으면 JDK 동적 프록시를 사용")
    public void interfaceProxy() {
        ServiceInterface target = new ServiceImpl();

        //프록시 팩토리를 만들 때 타겟 정보를 일단 넘기기 때문에 타겟을 따로 만지지 않아도 된다.
        ProxyFactory proxyFactory = new ProxyFactory(target);

        proxyFactory.addAdvice(new TimeAdvice());
        //프록시가 제공하는 부가 기능을 어드바이스라고 한다.
        //그리고 우리가 만들어 두었던 timeAdvice 객체를 집어넣는다.

        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();

        log.info("targetClass = {}", target.getClass());
        log.info("proxyClass = {}", proxy.getClass());
        //우리가 항상 이렇게 로그를 찍어보는 방법도 있지만, AOPUtils.isAopProxy(proxy); 스프링이 지원하는 기능이 있다.
        //Assertions.assertThat(AopUtils.isAopProxy(proxy)).isFalse();
        Assertions.assertThat(AopUtils.isAopProxy(proxy)).isTrue();
        Assertions.assertThat(AopUtils.isJdkDynamicProxy(proxy)).isTrue();
        Assertions.assertThat(AopUtils.isCglibProxy(proxy)).isFalse();
        // -> 주의 할 점은 이 두개의 AopUtils 는 프록시 팩토리를 통해 직접 생성된 것들에 제한한다.
    }


    @Test
    public void concreteProxy() {
        ConcreteService target = new ConcreteService();
        ProxyFactory proxyFactory = new ProxyFactory(target);

        proxyFactory.setProxyTargetClass(true);
        //인터페이스라도 CGLIB 으로 고집하고 싶을 때 이 메서드의 인자로 TRUE 를 넘겨주게 되면
        //상속으로 CGLIB 으로 클래스 기반으로 동적 클래스를 생성한다.
        // -> 실무에서 사용 하는 겨웅가 많으므로 주의해서 보도록 하자.

        proxyFactory.addAdvice(new TimeAdvice());
        ConcreteService proxy = (ConcreteService) proxyFactory.getProxy();

        proxy.call();

        Assertions.assertThat(AopUtils.isCglibProxy(proxy)).isTrue();
        /*
         * 스프링 부트는 AOP 를 적용할 떄 기본적으로 proxyTargetClass = true 를 기본으로 가져간다.
         * 따라서 인터페이스가 있어도 항상 CGLIB 을 이용해서 구체 클래스를 기반으로 프록시를 생성한다.
         */

        /*
         * 포인트컷 : 어디에 부가 기능을 적용할지 , 어디에 부가 기능을 적용하지 않을지 판단하는 필터링 로직.
         *          정규식을 사용한 메서드와 클래스 이름으로 구분
         * 어드바이스 : 프록시가 호출하는 부가 기능을 어드바이스라고 정의.
         * 어드바이저 : 하나의 포인트컷과 하나의 어드바이스를 가지고 있으면 그 두개를 합쳐놓은 것이 어드바이저이다.
         *
         * 이제부터 구현할 AOP 구조는 클라이언트가 프록시를 호출하면 프록시는 어드바이저를 알고 있기 때문에 포인트컷
         * 필터를 타고 해당 메서드에 어드바이스를 적용해도 되는지 확인 후에 타겟 인스턴스를 호출하는 역할을 하는 코드를
         * 짜본다.
         */
    }
}
