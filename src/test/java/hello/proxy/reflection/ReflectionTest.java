package hello.proxy.reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ReflectionTest {

    public class Member {
    }

    @Test
    void nameTest() {
        Class<?> objectClass = Object.class;
        String fqcn = objectClass.getName();
        System.out.println("getName() = " + fqcn);
        String simpleName = objectClass.getSimpleName();
        System.out.println("getSimpleName() = " + simpleName);

        Class<Member> memberClass = Member.class;
        String memberFqcn = memberClass.getName();
        System.out.println("getName() = " + memberFqcn);
        String memberSimpleName = memberClass.getSimpleName();
        System.out.println("getSimpleName() = " + memberSimpleName);
    }

    @Test
    void instanceTest() {
        try {
            // Team 클래스를 지칭하는 Class 인스턴스를 생성
            Class<?> teamClass = Class.forName("hello.proxy.reflection.Team");
            // teamClass 으로 생성자 취득
            Constructor<?> constructor = teamClass.getConstructor(String.class, int.class);
            // 생성자로 인스턴스 생성
            Object teamInstance = constructor.newInstance("test name", 2);
            // 인스턴스 생성 확인
            System.out.println(teamInstance);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
