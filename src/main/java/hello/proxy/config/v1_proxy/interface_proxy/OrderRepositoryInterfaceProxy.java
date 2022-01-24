package hello.proxy.config.v1_proxy.interface_proxy;

import hello.proxy.app.v1.OrderRepositoryV1;
import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderRepositoryInterfaceProxy implements OrderRepositoryV1 {

    private final OrderRepositoryV1 target;
    private final LogTrace logTrace;

    @Override
    public void save(String itemId) {
        TraceStatus status = null;
        try{
            status = logTrace.begin("OrderRepository.request()");
            //target호출
            //내가 호출할 대상을 타겟이라고 함
            target.save(itemId);
            logTrace.end(status);


        }catch (Exception e){
            logTrace.exception(status,e);
            throw e;
            //흐름을 바꾸면 안되기 때문에 예외를 던져줌
        }
    }
}
