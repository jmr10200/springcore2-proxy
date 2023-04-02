package hello.proxy.app.componentscan;

import org.springframework.stereotype.Repository;

@Repository
public class ModifyRepository {

    public void update(String id) {
        // 수정 로직
        if (id.equals("err")) {
            throw new ModifyFailException("등록실패");
        }
        sleep(1000);
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
