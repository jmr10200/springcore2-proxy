package hello.proxy.reflection;

import org.junit.jupiter.api.Test;

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

}
