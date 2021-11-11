package org.springframework.test.aop;

import org.junit.Before;
import org.junit.Test;
import org.springframework.aop.AdvisedSupport;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.TargetSource;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.framework.CglibAopProxy;
import org.springframework.aop.framework.JdkDynamicAopProxy;
import org.springframework.test.common.WorldServiceInterceptor;
import org.springframework.test.service.WorldService;
import org.springframework.test.service.WorldServiceImpl;

/**
 * @author derekyi
 * @date 2020/12/6
 */
public class DynamicProxyTest {

	private AdvisedSupport advisedSupport;

	@Before
	public void setup() {
		WorldService worldService = new WorldServiceImpl();

		// 1. 设置被代理对象（Joinpoint)
		TargetSource targetSource = new TargetSource(worldService);
		advisedSupport = new AdvisedSupport();
		advisedSupport.setTargetSource(targetSource);

		// 2.设置拦截器(Advice)
		WorldServiceInterceptor methodInterceptor = new WorldServiceInterceptor();
		advisedSupport.setMethodInterceptor(methodInterceptor);

		MethodMatcher methodMatcher = new AspectJExpressionPointcut("execution(* org.springframework.test.service.WorldService.explode(..))").getMethodMatcher();
		advisedSupport.setMethodMatcher(methodMatcher);
	}

	@Test
	public void testJdkDynamicProxy() throws Exception {
		// 3. 创建代理(Proxy)
		WorldService proxy = (WorldService) new JdkDynamicAopProxy(advisedSupport).getProxy();

		// 4.基于AOP的调用
		proxy.explode();
	}

	@Test
	public void testCglibDynamicProxy() throws Exception {
		// 3. 创建代理(Proxy)
		WorldService proxy = (WorldService) new CglibAopProxy(advisedSupport).getProxy();

		// 4.基于AOP的调用
		proxy.explode();
	}
}
