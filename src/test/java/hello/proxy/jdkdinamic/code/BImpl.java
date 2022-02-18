package hello.proxy.jdkdinamic.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BImpl implements BInterface{
    @Override
    public String call() {
        log.info("A 호출");
        return "A";
    }
    /*
     * 동적 프록시 JDK 기술은 리플렉션보다 자신이 객체를 직접 생성해주지 않아도
     * 객체를 자동으로 만들어내는 기술을 가지고 있다.
     *
     * 인터페이스로 기반하여 만들어지는 동적프록시의 사용 방법에 대해 알아보자.
     * 따라서 인터페이스는 필수라고 할 수 있다.
     *
     * 처음 프록시 패턴에서는 인터페이스를 기반으로 클라이언트가 어떤 프록시를 호출하는지 모르게끔 하여 호출했던 기억이 있다.
     * 이 객체를 직접 생성하지 않고 프레임워크에 맡기는 기술을 코드로 설명한다.
     *
     * JDK 동적 프록시에 적용할 로직은 자바가 제공하는 InvocationHandler 라는 인터페이스를 구현해주면 된다.
     */
}
