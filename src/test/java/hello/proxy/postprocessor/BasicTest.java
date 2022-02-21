package hello.proxy.postprocessor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;

import javax.websocket.RemoteEndpoint;

public class BasicTest {

    @Test
    public void basicConfig() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(BasicConfig.class);

        //A는 빈으로 등록이 되어서 찾아보자.
        A a = (A) applicationContext.getBean("beanA",A.class);
        a.helloA();

        //hello.proxy.postprocessor.BasicTest$A - helloA

        //B는 빈으로 등록되지 않는다.
        Assertions.assertThrows(NoSuchBeanDefinitionException.class, () -> applicationContext.getBean(B.class));

        //테스트 변경
        B b = applicationContext.getBean("beanA", B.class);
        b.helloB();
        Assertions.assertThrows(NoSuchBeanDefinitionException.class, () -> applicationContext.getBean(A.class));


        /*
         * 일단 이번 내용은 기본적인 빈 등록에 대한 예제이다.
         * 하지만 빈 후처리기 등록을 하기 위해서는 BeanPostProcessor 라는 인터페이스가 존재하는데
         * 이 인터페이스를 구현하고 로직을 적어줘야 인터페이스 구현이 가능하다.
         *
         * 인터페이스에서 구현해야하는 메서드가 두가지 있는데
         *  postProcessBeforeInitialization 는 @PostConstruct 가 실행되기 이전에 수행되어야하는가를 나타낸다.
         *  postProcessAfterInitialization 는 반대로 이 후에 실행되어야하는가를 나타낸다.
         *
         */

//        @Nullable
//        default Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
//            return bean;
//        }
//        @Nullable
//        default Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
//            return bean;
//        }
        // default 문법은 자바 8 이후에 추가되었는데 인터페이스에서 default 가 붙어있으면 로직을 구현하고 필수로 오버라이드를 안해도 된다고 한다.
    }

    @Slf4j
    @Configuration
    static class BasicConfig{
        @Bean(name = "beanA")
        public A a(){
            return new A();
        }

        public AToBPostProcessor helloPostProcessor(){
            return new AToBPostProcessor();
        }
    }

    @Slf4j
    static class A{
        public static void helloA(){
            log.info("helloA");
        }
    }

    @Slf4j
    static class B{
        public static void helloB(){
            log.info("helloA");
        }
    }

    @Slf4j
    static class AToBPostProcessor implements BeanPostProcessor{
        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

            log.info("beanName = {} bean = {}",beanName, bean);

            if(bean instanceof A){
                return new B();
            }
            /*
             * 빈이 넘어올 때, 빈이 A의 객체이면 return 을 B 객체로 하고 아닐 경우 일반 기존에 등록했던 A 객체를 반환하라는 뜻이다.
             * 우선권을 이 인터페이스가 가지기 때문에 먼저 필터링 된다.
             *
             * 이렇게 반환 된 B 객체는 반환이 되지만 빈 이름은 우리가 초기에 등록했던 BeanA 라는 이름으로 등록이 되게 된다.
             * 이름은 그대로이면서 객체만 바뀐 격이다.
             */
            return bean;
        }

        /*
         * 빈 후처리기를 사용하면 setter 를 이용해서 중간에 어떤 조작이나 변경을 해버리거나
         * 특히 컴포넌트 스캔으로 떠버린 빈들은 어떻게 변경하거나 조작할 방법이 없다.
         * 빈 후처리기를 사용하면서 가장 좋은 점은 모든 빈을 중간에 조작한다는 말은 빈을 프록시로 교체하는 것도 가능하다는 뜻이다.
         */
    }
}
