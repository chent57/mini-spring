package org.springframework.test.common;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author derekyi
 * @date 2020/12/6
 */
public class WorldServiceInterceptor implements MethodInterceptor {
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		// 3.
		System.out.println("Do something before the earth explodes");
		// 4.
		Object result = invocation.proceed();
		// 6.
		System.out.println("Do something after the earth explodes");
		return result;
	}
}
