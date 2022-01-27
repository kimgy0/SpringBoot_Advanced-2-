package hello.proxy.pureproxy.concreteproxy.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TimeProxy extends ConcreteLogic{

    private ConcreteLogic concreteLogic;

    public TimeProxy(ConcreteLogic concreteLogic) {
        this.concreteLogic = concreteLogic;
    }

    //부가기능 삽입
    //콘트리트 로직을 오버라이드.
    @Override
    public String operation(){
        log.info("TimeDecorator 실행");

        Long start = System.currentTimeMillis();

        String operation = concreteLogic.operation();

        Long end = System.currentTimeMillis();
        Long resultTime = end - start;
        log.info("Time decorator end resultTime = {}ms",resultTime);
        return operation;
    }




}
