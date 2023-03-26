package hello.proxy.cglibbasic.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommandServiceImpl implements CommandService {

    @Override
    public String execute() {
        log.info("execute() 호출");
        return "execute() complete";
    }

    @Override
    public String update() {
        log.info("update() 호출");
        return "update() complete";
    }

}
