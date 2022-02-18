package hello.proxy.jdkdinamic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Slf4j
public class ReflectionTest {
    @Test
    public void reflection0() {


        Hello target = new Hello();
        //공통 로직 1 시작.
        log.info("start");
        String result1 = target.callA(); //호출하는 메서드만 다름
        log.info("result = {}", result1);
        //공통 로직 1 종료.

        //공통 로직 2 시작.
        log.info("start");
        String result2 = target.callB();
        log.info("result = {}", result2);
        //공통 로직 2 종료.

        /*
         차이가 있다면 호출하는 메서드만 다르고 메서드 호출 방식은 똑같다.
         여기서 공통로직 1,2를 하나의 메서드로 뽑아서 합쳐볼 수 있을까?
         이 둘을 동적으로 생성해서 호출하자.

         이 때 사용하는 기술은 리플렉션이다. 메서드의 메타정보를 이용해서 정적이 아닌 동적으로 처리할 수 있다.
         이 외에 람다를 사용하기 어려운 상황이라고 생각을 해보자.
         */
    }

    @Test
    public void reflection1() throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, IllegalAccessException {
        //클래스의 메타정보를 가져오고
        Class classHello = Class.forName("hello.proxy.jdkdinamic.ReflectionTest$Hello");

        Hello target = new Hello();
        //callA 메서드의 메타정보를 가져온다.
        Method methodCallA = classHello.getMethod("callA");

        //메서드의 메타정보를 이용해서 호출한다. -> target에 있는 인스턴스 출력.
        Object result1 = methodCallA.invoke(target);
        log.info("result1={}",result1);

        //B호출
        Method methodCallB = classHello.getMethod("callB");
        Object result2 = methodCallB.invoke(target);
        log.info("result2={}",result2);

        //그럼 그냥 메서드만 호출하면 되는데 왜 리플렉션을 사용해서 호출할까?
        // -> 이렇게 메타정보로 메서드를 호출하면 동적으로 클래스나 메서드 정보를 변경할 수 있다는 장점이 있다.
        // Method 클래스로 추상화가 되어 실행된다. 이 덕에 공통로직으로 실행할 수 있게 된다.
    }

    @Test
    public void reflection2() throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Class classHello = Class.forName("hello.proxy.jdkdinamic.ReflectionTest$Hello");

        Hello target = new Hello();

        Method methodCallA = classHello.getMethod("callA");
        dynamicCall(methodCallA, target);

        Method methodCallB = classHello.getMethod("callB");
        dynamicCall(methodCallB, target);
    }
    /*
     dynamicCall 은 메서드로 추상화된 메타정보를 이용한 Method 정보 때문에 공통화가 가능해진다.
            Object result = method.invoke(target);

            리플렉션은 가급적 쓰지 않는게 좋다.
            동적으로 만드는게 가능하지만, 컴파일 시점에 오류를 잡을 수 없다. 일단은 메서드를 컴파일 하고 사용시점에 오류를 내기 때문이다.
            즉, 컴파일 시점에 오류를 잡을 수 있는 것을 사용하는게 좋다.
     */

    private void dynamicCall(Method method, Object target) throws InvocationTargetException, IllegalAccessException {
        log.info("start");
        Object result = method.invoke(target);
        log.info("result = {}",result);
    }



    //기준 클래스
    @Slf4j
    static class Hello{
        public String callA(){
            log.info("call A");
            return "A";
        }
        public String callB(){
            log.info("call B");
            return "B";
        }
    }
}
