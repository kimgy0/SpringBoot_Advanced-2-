package hello.proxy.config.v2_dynamicproxy.handler;

import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class LogTraceBasicHandler implements InvocationHandler {

    private final Object target; //프록시가 호출할 대상이다.
    private final LogTrace logTrace;

    public LogTraceBasicHandler(Object target, LogTrace logTrace) {
        this.target = target;
        this.logTrace = logTrace;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        TraceStatus status = null;
        try{
            String message = method.getDeclaringClass().getSimpleName() + "." + method.getName() + "()";
            //method 로 추상화된 객체는 모든 메타정보를 다 가지고 있기 때문에 메서드의 이름 뿐만이 아니라 클래스 이름도 호출이 가능하다.

            status = logTrace.begin(message);

            Object result = method.invoke(target, args);

            logTrace.end(status);
            return result;

        }catch (Exception e){
            logTrace.exception(status, e);
            throw e;
        }

    }
}
