package hello.proxy.jdkdinamic.code;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;

@Slf4j
public class JdkDynamicProxyTest {

    @Test
    public void dynamicA() {
        //여기에 프록시를 넣어서 테스트 해보자.
        AInterface target = new AImpl();

        TimeInvocationHandler handler = new TimeInvocationHandler(target);

        //자바 언어 차원에서 제공하는 프록시 생성 기술.
        //1. AInterface.class.getClassLoader() -> 프록시가 어디에 생성될지 지정을 해주어야 하고
        //2. 어떤 인터페이스를 넣어주는데 어떤 인터페이스를 기반으로 프록시를 만들어줄지 정해준다. -> 인터페이스가 여러개일 수 있어서 배열로 된다.
        //3. handler 마지막으로 프록시가 호출할 로직을 넣어주면 된다.
        AInterface proxy = (AInterface)Proxy.newProxyInstance(AInterface.class.getClassLoader(), new Class[]{AInterface.class}, handler);
        //Object proxy 의 변수로 반환받아도 되지만 AInterface 기준으로 만들어지기 때문에
        //AInterface proxy = (AInterface) -- 로 받아도된다.

        proxy.call();
        log.info("targetClass = {}", target.getClass());
        log.info("proxyClass = {}", proxy.getClass());

        //targetClass = class hello.proxy.jdkdinamic.code.AImpl
        //proxyClass = class com.sun.proxy.$Proxy9
    }

    @Test
    public void dynamicB() {

        BInterface target = new BImpl();

        TimeInvocationHandler handler = new TimeInvocationHandler(target);
        BInterface proxy = (BInterface)Proxy.newProxyInstance(BInterface.class.getClassLoader(), new Class[]{BInterface.class}, handler);

        proxy.call();
        log.info("targetClass = {}", target.getClass());
        log.info("proxyClass = {}", proxy.getClass());


        /*
            TimeInvocationHandler 에 실제 실행할 로직을 하나만 작성성해 놓고 우리 프록시클래스를 생성해주지 않아도 알아서 생성했다.
         */
    }
}
