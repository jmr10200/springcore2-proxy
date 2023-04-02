package hello.proxy.app.componentscan;

import org.springframework.stereotype.Service;

@Service
public class ModifyService {

    private final ModifyRepository modifyRepository;

    public ModifyService(ModifyRepository modifyRepository) {
        this.modifyRepository = modifyRepository;
    }

    public void modifyMember(String id) {
        modifyRepository.update(id);
    }
}
