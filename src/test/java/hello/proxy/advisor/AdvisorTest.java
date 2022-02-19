package hello.proxy.advisor;

import hello.proxy.common.ServiceImpl;
import hello.proxy.common.ServiceInterface;
import hello.proxy.common.advice.TimeAdvice;
import hello.proxy.proxyfactory.ProxyFactoryTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.security.Provider;

@Slf4j
public class AdvisorTest {
    @Test
    public void advisorTest1() {
        ServiceInterface target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);

        proxyFactory.setProxyTargetClass(true);
        proxyFactory.addAdvisor(new DefaultPointcutAdvisor(Pointcut.TRUE, new TimeAdvice()));
        //proxyFactory.addAdvice(--);
        /*
         * 여기서 addAdvice 는 편의성 메서드이다. 타고 들어가다보면
         * addAdvisor(pos, new DefaultPointcutAdvisor(advice));
         * 라는 구문이 있는데 addAdvisor 안에
         * new DefaultPointcutAdvisor(advice)
         * 이 곳에 들어가면
         * public DefaultPointcutAdvisor(Advice advice) {this(Pointcut.TRUE, advice);}
	* 이런식으로 메서드가 짜여있다 결국엔 TRUE 값으로 대신하고 어드바이스만 받을 수 있는 것이다.
	* 근본은 addAdvice 인 것이다.
         */
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();
        //Pointcut.TRUE 를 사용하면 항상 참인 포인트컷을 사용할 수 있다.

        proxy.save();
        proxy.find();

    }


    /*
     * 여기서는 포인트 컷을 직접 만들어본다.
     * if else 분기 문으로 어떤 것을 포인트 컷으로 지정할지 결정을 할 수도 있지만,
     * 이 기능을 특화시켜 놓은 것을 포인트 컷이라고 한다.
     *
     * 스프링이 제공하는 PointCut 인터페이스가 있는데데
     *
     * getClassFilter 라는 클래스 기반으로 필터링하는 메서드가 있고,
     * getMethodMatcher 이라는 메서드 기반으로 필터링하는 메서드가 있다.
     *
     * 이 두개의 메서드가 true 를 반환해야 어드바이스를 적용이 가능하다.
     *  */
    @Test
    @DisplayName("직접 만든 포인트컷")
    public void advisorTest2() {
        ServiceInterface target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);

        //proxyFactory.setProxyTargetClass(true);
        proxyFactory.addAdvisor(new DefaultPointcutAdvisor(new MyPointCut(), new TimeAdvice()));

        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();

        proxy.save();
        proxy.find();

       //포인트컷 호출 method = save, targetClass = class hello.proxy.common.ServiceImpl
       // 포인트컷 결과 result = true
        // TimeProxy 실행
        //save 호출
       // TimeProxy 종료 resultTime = 0ms
        //포인트컷 호출 method = find, targetClass = class hello.proxy.common.ServiceImpl
       // 포인트컷 결과 result = false
       // find 호출

    }

    //포인트 컷을 사용하려면 PointCut 인터페이스를 구현해야 한다.
    static class MyPointCut implements Pointcut{

        @Override
        public ClassFilter getClassFilter() {
            return ClassFilter.TRUE;
            //일단 클래스에 대해서 다 true를 반환하도록 한다.
        }

        @Override
        public MethodMatcher getMethodMatcher() {
            return new MyMethodMatcher();
        }
    }

    //메소드는 이렇게 구현해야한다.
    static class MyMethodMatcher implements MethodMatcher{

        @Override
        public boolean matches(Method method, Class<?> targetClass) {
            //메서드가 넘어오고 targetClass 가 넘어오면
            boolean result = method.getName().equals("save");
            //메서드 이름이 save 인 경우 true 를 반환하도록 한다.
            log.info("포인트컷 호출 method = {}, targetClass = {}",method.getName(), targetClass);
            log.info("포인트컷 결과 result = {}", result);
            return result;
        }

        @Override
        public boolean isRuntime() {
            return false;
        }

        @Override
        public boolean matches(Method method, Class<?> targetClass, Object... args) {
            return false;
        }

        /*
         * 여기서 주의할 점은 isRunTime 메서드가 처음으로 실행되는데 이 값이 TRUE 이면 하단의 args 가 인자로 있는
         * 메서드가 호출이 되어진다.
         * 왜냐면 성능상 동적인 args 를 가지고 있는 메서드는 넘어오면 무수히 많은 인자와 메서드와 클래스에 대한 캐시를
         * 하지 않는다.
         * 반면에 false 로 반환이 되면 상단의 정적인 메서드를 호출하는데 이는 성능상 클래스가 해봤자 얼마나 많겠는가,
         * 그래서 캐시를 하기 위해서 이렇게 둘로 나누어져있다.
         */
    }





    @Test
    @DisplayName("스프링이 제공하는 포인트컷")
    //포인트 컷을 스프링이 제공하는 것을 써보자.
    public void advisorTest3() {

        ServiceInterface target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        /*****************************
         *  새로 추가된 코드
         ****************************/
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.addMethodName("save");
        //이런식으로 포인트 컷을 구현해놓은 것을 사용해도 된다.
        //이 외에도 스프링은 무수히 많은 포인트컷을 제공한다.
        //pdf 확인.


        proxyFactory.setProxyTargetClass(true);
        proxyFactory.addAdvisor(new DefaultPointcutAdvisor(pointcut, new TimeAdvice()));
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();

        proxy.save();
        proxy.find();

    }

}
