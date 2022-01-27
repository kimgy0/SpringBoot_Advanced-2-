package hello.proxy.config.v1_proxy.concreteProxy;

import hello.proxy.app.v2.OrderControllerV2;
import hello.proxy.app.v2.OrderRepositoryV2;
import hello.proxy.app.v2.OrderServiceV2;
import hello.proxy.app.v3.OrderControllerV3;
import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;

public class OrderControllerConcreteProxy extends OrderControllerV2 {

    private final OrderControllerV2 target;
    private final LogTrace logTrace;

    public OrderControllerConcreteProxy(OrderControllerV2 target, LogTrace logTrace) {
        super(null);
        this.target = target;
        this.logTrace = logTrace;
    }

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
