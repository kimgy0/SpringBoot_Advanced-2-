package hello.proxy.config.v1_proxy.interface_proxy;

import hello.proxy.app.v1.OrderControllerV1;
import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.RequiredArgsConstructor;

import java.sql.PreparedStatement;

@RequiredArgsConstructor
public class OrderControllerInterfaceProxy implements OrderControllerV1 {

    private final OrderControllerV1 target; //실제타겟
    private final LogTrace logTrace;

    @Override
    public String request(String itemId) {
        TraceStatus status = null;
        try{
            status = logTrace.begin("OrderController.request()");
            //target호출
            //내가 호출할 대상을 타겟이라고 함
            String result = target.request(itemId);
            logTrace.end(status);

            return result;
        }catch (Exception e){
            logTrace.exception(status,e);
            throw e;
            //흐름을 바꾸면 안되기 때문에 예외를 던져줌
        }
    }

    @Override
    public String noLog() {
        return target.noLog();
    }
}
