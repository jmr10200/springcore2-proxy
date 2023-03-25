package hello.proxy.reflection.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Objects;

public class CommandInvocationHandler implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Objects.equals("execute1", method.getName())) {
            // execute1() 처리
            if ("NORTH".equals(args[0])) {
                return 0;
            } else if ("SOUTH".equals(args[0])) {
                return 1;
            } else {
                throw new IllegalArgumentException("Arg must be 'NORTH' or 'SOUTH'");
            }
        }

        if (Objects.equals("execute2", method.getName())) {
            // execute2() 처리
            if ("EAST".equals(args[0])) {
                return 2;
            } else if ("WEST".equals(args[0])) {
                return 3;
            } else {
                throw new IllegalArgumentException("Arg must be 'EAST' or 'WEST'");
            }
        }
        throw new IllegalArgumentException("Invalid method");
    }
}
