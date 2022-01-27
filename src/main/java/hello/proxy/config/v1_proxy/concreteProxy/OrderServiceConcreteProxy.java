package hello.proxy.config.v1_proxy.concreteProxy;

import hello.proxy.app.v2.OrderRepositoryV2;
import hello.proxy.app.v2.OrderServiceV2;
import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;

public class OrderServiceConcreteProxy extends OrderServiceV2 {

    private final OrderServiceV2 target;
    private final LogTrace logTrace;

    public OrderServiceConcreteProxy(OrderServiceV2 target, LogTrace logTrace) {
        super(null);

        /*
         * 상위 부모 클래스인 OrderService에 가보면 final로 된 변수가 있다.
         * 이 때문에 기본생성자가 아닌 생성자를 통해 하나 주입받는 생성자가 있는데
         * 이것 때문에 부모클래스의 생성자인 super클래스를 이용해서 생성자를 호출해야한다.
         * 그리고 그 안에 구현체를 넣어줘야한다
         *  -> super(OrderRepository orderRepository) 이런 식으로 넣어주어야 한다.
         * 근데 우리는 이클래스를 프록시로만 사용할 것이기 때문에 그냥 super(null)로 넣어준다.
         * super(null) 이 없으면 자동으로 기본생성자를 호출하는데
         * final이기 때문이다.
         *
         * 나중에 프록시에 빈주입은 알아서 configuration에서 진행할 예정임.
         *
         * 이것을 단점으로 들 수 있음.
         */
        this.target = target;
        this.logTrace = logTrace;
    }

    @Override
    public void orderItem(String itemId) {
        TraceStatus status = null;
        try{
            status = logTrace.begin("OrderService.orderItem");
            //target호출
            target.orderItem(itemId);
            logTrace.end(status);
        }catch (Exception e){
            logTrace.exception(status, e);
            throw e;
        }
    }
}
