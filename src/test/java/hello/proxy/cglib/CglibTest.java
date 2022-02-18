package hello.proxy.cglib;

import hello.proxy.common.ConcreteService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.cglib.proxy.Enhancer;

@Slf4j
public class CglibTest {

    @Test
    public void cglib() {

        ConcreteService target = new ConcreteService();

        Enhancer enhancer = new Enhancer();
        //구체 클래스로 부터 상속받은 프록시를 만들어야 하기 때문에 인터페이스가 아닌 구체 클래스로 인자에 넣어준다.
        //setSuperclass 의 이름도 어쩌면 그 이유 때문일 것이라고 한다.
        enhancer.setSuperclass(target.getClass());
        enhancer.setCallback(new TimeMethodInterceptor(target));
        ConcreteService proxy = (ConcreteService) enhancer.create();
        //Object o 원래는 이렇게 나온다.

        log.info("targetClass = {}",target.getClass());
        log.info("proxyClass = {}",proxy.getClass());
        //targetClass = class hello.proxy.common.ConcreteService
        //proxyClass = class hello.proxy.common.ConcreteService$$EnhancerByCGLIB$$1c94e8ce

        proxy.call();
    }
}
