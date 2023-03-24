package hello.proxy.reflection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// 어노테이션 보유 : RUNTIME 은 실행시에도 어노테이션 정보를 읽어올 수 있음
@Retention(RetentionPolicy.RUNTIME) // 디폴트 : CLASS
// 허용하는 타입 지정 : 클래스, 인터페이스를 지정
@Target(ElementType.TYPE)
public @interface MyAnnotation {

    String name() default "default";

    int value();
}
