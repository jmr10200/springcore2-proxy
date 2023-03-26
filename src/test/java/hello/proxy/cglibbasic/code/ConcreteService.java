package hello.proxy.cglibbasic.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConcreteService {
    public String call() {
        log.info("ConcreteService.call() 호출");
        return "call() complete";
    }
}
