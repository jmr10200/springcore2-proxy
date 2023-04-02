package hello.proxy.app.componentscan;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ModifyController {

    private final ModifyService modifyService;

    public ModifyController(ModifyService modifyService) {
        this.modifyService = modifyService;
    }

    @GetMapping("/modify")
    public String call(String id) {
        modifyService.modifyMember(id);
        return "modify ok";
    }

    @GetMapping("/modify/no-log")
    public String noLog() {
        return "modify no-log ok";
    }
}
