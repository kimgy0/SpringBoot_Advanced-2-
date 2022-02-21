package hello.proxy;

import hello.proxy.config.AppV1Config;
import hello.proxy.config.AppV2Config;
import hello.proxy.config.v1_proxy.ConcreteProxyConfig;
import hello.proxy.config.v1_proxy.InterfaceProxyConfig;
import hello.proxy.config.v2_dynamicproxy.DynamicFilterConfig;
import hello.proxy.config.v2_dynamicproxy.DynamicProxyBasicConfig;
import hello.proxy.config.v3_proxyfactory.ProxyFactoryConfigV1;
import hello.proxy.config.v3_proxyfactory.ProxyFactoryConfigV2;
import hello.proxy.config.v4_postprocessor.BeanPostProcessorConfig;
import hello.proxy.trace.logtrace.LogTrace;
import hello.proxy.trace.logtrace.ThreadLocalLogTrace;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

//@Import(AppV2Config.class)
//@Import(InterfaceProxyConfig.class)
//@Import(ConcreteProxyConfig.class)
//@Import(DynamicProxyBasicConfig.class)
//@Import(DynamicFilterConfig.class)
//@Import(ProxyFactoryConfigV1.class)
//@Import(ProxyFactoryConfigV2.class)
@Import(BeanPostProcessorConfig.class)
@SpringBootApplication(scanBasePackages = "hello.proxy.app") //주의
/*
 * @SpringBootApplication
 * 는 기본적으로 컴포넌트스캔 어노테이션을 가지고 있기 때문에 config는 v1,v2,v3 까지 만들거라서 컴포넌트 스캔의 대상이 되지 않게 했고
 * @Import를 통해서 컨피그 파일을 빈으로 인식 시켜주었다.
 */
public class ProxyApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProxyApplication.class, args);
	}

	@Bean
	public LogTrace logTrace(){
		return new ThreadLocalLogTrace();
	}
}
