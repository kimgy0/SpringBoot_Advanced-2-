package hello.proxy.pureproxy.decorator.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageDecorator implements Component{

    private Component component;

    public MessageDecorator(Component component) {
        this.component = component;
    }

    @Override
    public String operation() {
        log.info("message decorator start");
        String operation = component.operation(); //실제 객체
        String decoResult = "******" + operation + "******";//프록시가 데코레이션으로 꾸며줌.
        log.info("message decorator end");
        return decoResult;
    }
}
