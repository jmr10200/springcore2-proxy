package hello.proxy.reflection;

import hello.proxy.reflection.jdkproxy.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;

@Slf4j
public class ProxyReflectionTest {

    @Test
    void dynamicA() {
        CommandA target = new CommandAImpl();
        CommandTimeInvocationHandler handler = new CommandTimeInvocationHandler(target);
        CommandA proxy = (CommandA) Proxy.newProxyInstance(CommandA.class.getClassLoader(), new Class[]{CommandA.class}, handler);
        proxy.execute("SOUTH");
        log.info("targetClass = {}", target.getClass());
        log.info("proxyClass = {}", proxy.getClass());
    }

    @Test
    void dynamicB() {
        CommandB target = new CommandBImpl();
        CommandTimeInvocationHandler handler = new CommandTimeInvocationHandler(target);
        CommandB proxy = (CommandB) Proxy.newProxyInstance(CommandB.class.getClassLoader(), new Class[]{CommandB.class}, handler);
        proxy.execute("WEST");
        log.info("targetClass = {}", target.getClass());
        log.info("proxyClass = {}", proxy.getClass());
    }
}
