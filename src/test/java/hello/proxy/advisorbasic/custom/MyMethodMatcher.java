package hello.proxy.advisorbasic.custom;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.MethodMatcher;

import java.lang.reflect.Method;

@Slf4j
public class MyMethodMatcher implements MethodMatcher {

    private String matchName = "execute1";

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        boolean result = method.getName().equals(matchName);
        log.info("포인트컷 호출 method = {}, targetClass = {}", method.getName(), targetClass);
        log.info("포인트컷 결과 result = {}", result);
        return result;
    }

    @Override
    public boolean isRuntime() {
        return false;
    }

    @Override
    public boolean matches(Method method, Class<?> targetClass, Object... args) {
        throw  new UnsupportedOperationException();
    }
}
