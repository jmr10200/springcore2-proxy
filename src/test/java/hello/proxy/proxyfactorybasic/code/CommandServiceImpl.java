package hello.proxy.proxyfactorybasic.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommandServiceImpl implements CommandService {

    @Override
    public String find() {
        log.info("find() 호출");
        return "find() complete";
    }

    @Override
    public String modify() {
        log.info("modify() 호출");
        return "modify() complete";
    }
}
