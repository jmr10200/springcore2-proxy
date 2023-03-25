package hello.proxy.reflection;

import hello.proxy.reflection.proxy.Command;
import hello.proxy.reflection.proxy.CommandInvocationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.*;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void methodTest() {
        try {
            System.out.println("===== 메소드 목록 =====");
            Arrays.stream(Team.class.getDeclaredMethods()).forEach(System.out::println);

            // Team 클래스의 transferNumber 메소드 취득
            Method numberMethod = Team.class.getMethod("transferNumber", int.class);
            // Team 데이터
            Team team = new Team("빌리", 10);
            System.out.println("===== 메소드 실행 전 =====");
            System.out.println(team);
            // transferNumber 메소드 실행 : 10 + 5 = 15 리턴
            Object obj = numberMethod.invoke(team, 5);
            System.out.println("===== invoke() 메소드 실행 후 =====");
            System.out.println("number = " + obj);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Test
    void annotation() {
        try {
            // Guild.Class 와 동일
            Class<?> guildClass = Class.forName("hello.proxy.reflection.Guild");
            // 클래스에 붙어있는 어노테이션 취득
            MyAnnotation guildClassAnnotation = guildClass.getAnnotation(MyAnnotation.class);
            // 어노테이션 값 취득
            String name = guildClassAnnotation.name();
            int value = guildClassAnnotation.value();
            // 출력
            System.out.println("name = " + name + " , value = " + value);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    void proxyTest() {
        Command c = (Command) Proxy.newProxyInstance(
                Command.class.getClassLoader(),
                new Class<?>[]{Command.class},
                new CommandInvocationHandler());

        System.out.println(c.getClass().getName());

        // execute1() 호출
        int execute1 = c.execute1("UP");
        System.out.println("execute1 = " + execute1);
        assertEquals(1, execute1);
        // execute2() 호출
        int execute2 = c.execute2("RIGHT");
        System.out.println("execute2 = " + execute2);
        assertEquals(3, execute2);
        // execute1() 호출
        IllegalArgumentException assertThrows = assertThrows(IllegalArgumentException.class, () -> c.execute1("LEFT"));
        System.out.println("execute3 = " + assertThrows.getMessage());
        assertEquals("Arg must be 'UP' or 'DOWN'", assertThrows.getMessage());
    }
}
