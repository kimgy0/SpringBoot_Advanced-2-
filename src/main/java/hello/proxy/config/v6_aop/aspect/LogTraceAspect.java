package hello.proxy.config.v6_aop.aspect;

import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Slf4j
@Aspect
public class LogTraceAspect {
    private final LogTrace logTrace;

    public LogTraceAspect(LogTrace logTrace) {
        this.logTrace = logTrace;
    }

    //포인트 컷
    @Around("execution(* hello.proxy.app..*(..))")
    //어드바이스
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable{
        //어드바이스 로직
        TraceStatus status = null;
        try{
            String message = joinPoint.getSignature().toShortString();// 메세지로 쓰면
            logTrace.begin(message);

            Object result = joinPoint.proceed();
            //joinPoint 내에 실체 호출 대상, 전달 인자, 어떤 객체와 메서드가 포함되어 있는가?

            logTrace.end(status);
            return result;
        }catch (Exception e){
            logTrace.exception(status, e);
            throw e;
        }
    }
}
