package hello.proxy;

import hello.proxy.config.v6_aop.AopConfig;
import hello.proxy.trace.log.LogTracer;
import hello.proxy.trace.log.LogTracerAopConfig;
import hello.proxy.trace.logtrace.LogTrace;
import hello.proxy.trace.logtrace.ThreadLocalLogTrace;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

//@Import({AppV1Config.class, AppV2Config.class})
//@Import(InterfaceProxyConfig.class)
//@Import(ConcreteProxyConfig.class)
//@Import(DynamicProxyBasicConfig.class) // JDK 동적 프록시 : no-log 도 로그 출력문제
//@Import(DynamicProxyFilterConfig.class) // 메소드 로그 필터 프록시
//@Import(ProxyFactoryConfigV1.class)
//@Import(ProxyFactoryConfigV2.class)
//@Import(BeanPostProcessorConfig.class)
//@Import(AutoProxyConfig.class)
//@Import(ComponentScanConfig.class)

@Import(AopConfig.class)
//@Import(LogTracerAopConfig.class) // 학습용
@SpringBootApplication(scanBasePackages = "hello.proxy.app") //주의
public class ProxyApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProxyApplication.class, args);
	}

	@Bean
	public LogTrace logTrace() {
		return new ThreadLocalLogTrace();
	}

	// 학습용 LogTracer, LogTracerAopConfig
//	@Bean
	public LogTracer logTracer() {
		return new LogTracer();
	}
}
