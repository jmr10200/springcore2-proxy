package hello.proxy.advisorbasic.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Command implements CommandInterface {

    @Override
    public String execute1() {
        log.info("execute1() 호출");
        return "execute1() complete";
    }

    @Override
    public String execute2() {
        log.info("execute2() 호출");
        return "execute2() complete";
    }

}
