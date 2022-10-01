package hello.decorator;

import hello.decorator.code.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class DecoratorPatternTest {

    @Test
    void noDecorator() {
        Component realComponent = new RealComponent();
        DecoratorPatternClient client = new DecoratorPatternClient(realComponent);

        client.execute();
    }

    /**
     * client -> messageDecorator -> realComponent
     */
    @Test
    void decorator1() {
        Component component = new RealComponent();
        Component messageComponent = new MessageDecorator(component);
        DecoratorPatternClient client = new DecoratorPatternClient(messageComponent);

        client.execute();
    }

    /**
     * client -> timeDecorator -> messageDecorator -> realComponent
     */
    @Test
    void decorator2() {
        Component component = new RealComponent();
        Component messageComponent = new MessageDecorator(component);
        Component timeComponent = new TimeDecorator(messageComponent);
        DecoratorPatternClient client = new DecoratorPatternClient(timeComponent);

        client.execute();
    }
}
