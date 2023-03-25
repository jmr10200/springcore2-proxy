package hello.proxy.reflection.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Objects;

public class CommandInvocationHandler implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Objects.equals("execute1", method.getName())) {
            // execute1() 처리
            if ("UP".equals(args[0])) {
                return 1;
            } else if ("DOWN".equals(args[0])) {
                return 0;
            } else {
                throw new IllegalArgumentException("Arg must be 'UP' or 'DOWN'");
            }
        }

        if (Objects.equals("execute2", method.getName())) {
            // execute2() 처리
            if ("LEFT".equals(args[0])) {
                return 2;
            } else if ("RIGHT".equals(args[0])) {
                return 3;
            } else {
                throw new IllegalArgumentException("Arg must be 'LEFT' or 'RIGHT'");
            }
        }
        throw new IllegalArgumentException("Invalid method");
    }
}
