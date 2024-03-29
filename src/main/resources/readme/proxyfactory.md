#### 프록시 팩토리 - 소개

##### JDK 동적프록시, CGLIB 의 문제점
인터페이스가 있는 경우에는 JDK 동적 프록시를 적용하고 그렇지 않은 경우에는 CGLIB 를 적용하려면?<br>
두 기술을 함께 사용할 때 부가기능을 제공하기 위해<br>
JDK 동적 프록시가 제공하는 InvocationHandler, CGLIB 가 제공하는 MethodInterceptor 를 
각각 중복으로 만들어 관리해야 하는가? 공통으로 관리할 수는 없을까?

<br>

##### 스프링이 제공하는 Proxy Factory
스프링은 유사한 구체적인 기술들이 있을 때, 그것들을 통합해서 일관성 있게 접근할 수 있고, 더욱 편리하게 사용할 수 있는 추상화된 기술을 제공한다.<br>
스프링은 동적 프록시를 통합해서 편리하게 만들어주는 프록시 팩토리 (Proxy Factory) 라는 기능을 제공한다.<br>
이전에는 상황에 따라 JDK 동적 프록시를 사용하거나 CGLIB 를 사용해야 했다면, 이제는 프록시 팩토리 하나로 편리하게 동적 프록시를 사용할 수 있다.<br>
<br>
팩토리 하나로 편리하게 동적 프록시를 생성할 수 있다.<br>
프록시 팩토리는 인터페이스가 있으면 JDK 동적 프록시를 사용하고,<br>
구체 클래스만 있다면 CGLIB 를 사용한다. 그리고 이 설정을 변경할 수도 있다.<br>

##### 부가기능의 적용 : Advice 제공
JDK 동적 프록시 : InvocationHandler<br>
CGLIB : MethodInterceptor<br>
스프링은 부가기능을 적용할 때 Advice 라는 새로운 개념을 도입했다.<br>
개발자는 InvocationHandler 나 MethodInterceptor 를 신경쓰지 않고, Advice 만 만들면 된다.<br>
프록시 팩토리를 사용하면 Advice 를 호출하는 전용 InvocationHandler, MethodInterceptor 를 내부에서 사용한다.<br>


##### 특정 조건에 맞을 때 프록시 로직을 적용하는 기능 : Pointcut
특정 메소드 이름의 조건에 맞을때만 프록시 부가 기능이 적용되는 코드를 작성할 때,<br>
스프링은 Pointcut 이라는 개념을 도입해서 문제를 일관성있게 해결한다.<br>

<br>
(예) no-log() 가 실행되면 로그 X