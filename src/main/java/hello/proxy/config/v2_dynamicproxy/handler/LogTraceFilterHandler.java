package hello.proxy.config.v2_dynamicproxy.handler;

import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import org.springframework.util.PatternMatchUtils;

import javax.xml.stream.events.StartDocument;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class LogTraceFilterHandler implements InvocationHandler {

    private final Object target;
    private final LogTrace logTrace;
    private final String[] pattern;

    public LogTraceFilterHandler(Object object, LogTrace logTrace, String[] pattern) {
        this.target = object;
        this.logTrace = logTrace;
        this.pattern = pattern;
    }

    //private final String[] pattern; 패턴을 추가해준 이유는 메서드 이름이 이런 패턴에 대해서는 실행하지 않겠다는 정보를 가졌음.

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        /*
         * 메서드 이름 필터
        */
        String methodName = method.getName();
        //save, reqeust, reque*, *est 등등
        if(!PatternMatchUtils.simpleMatch(pattern, methodName)){
            return method.invoke(target,args);
        }



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
