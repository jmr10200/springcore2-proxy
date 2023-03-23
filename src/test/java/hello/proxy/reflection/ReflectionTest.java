package hello.proxy.reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

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

    @Test
    void fieldTest() {
        try {
            // Team 클래스를 지칭하는 Class 인스턴스를 생성
            Class<?> teamClass = Class.forName("hello.proxy.reflection.Team");
            System.out.println("===== getFields() : public Field 만 취득 =====");
            Arrays.stream(teamClass.getFields()).forEach(System.out::println);
            System.out.println("===== getDeclaredFields() : 모든 Field 취득 =====");
            Arrays.stream(teamClass.getDeclaredFields()).forEach(System.out::println);
            System.out.println();

            // teamClass 으로 생성자 취득
            Constructor<?> constructor = teamClass.getConstructor(String.class, int.class);
            // 생성자로 인스턴스 생성
            Object teamInstance = constructor.newInstance("test name", 2);

            // public 필드 : number 취득
            Field numberField = teamClass.getDeclaredField("number");
            numberField.set(teamInstance, 999);
            System.out.println("===== public field 값 변경 확인 =====");
            System.out.println("number 변경 -> " + teamInstance);

            // private 필드 : name 취득
            Field nameField = teamClass.getDeclaredField("name");
            // private 값에 set 접근 하면 java.lang.IllegalAccessException 발생
            // setAccessible(true) 를 설정하면 private 필드도 값을 취득 및 설정할 수 있다.
            nameField.setAccessible(true);
            // name 필드 값을 취득
            Object name = nameField.get(teamInstance);
            nameField.set(teamInstance, "이름 변경");

            System.out.println("===== private field 값 변경 확인 =====");
            System.out.println("이름 변경 -> " + teamInstance);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
