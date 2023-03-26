package hello.proxy.proxyfactorybasic.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConcreteService {

    public String call() {
        log.info("call() 호출");
        return "call() complete";
    }
}
