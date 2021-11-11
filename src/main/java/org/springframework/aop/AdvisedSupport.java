package org.springframework.aop;

import org.aopalliance.intercept.MethodInterceptor;

/**
 * @author derekyi
 * @date 2020/12/6
 */
public class AdvisedSupport {

	private TargetSource targetSource;

	// 方法拦截器
	private MethodInterceptor methodInterceptor;

	private MethodMatcher methodMatcher;

	public TargetSource getTargetSource() {
		return targetSource;
	}

	public void setTargetSource(TargetSource targetSource) {
		this.targetSource = targetSource;
	}

	public MethodInterceptor getMethodInterceptor() {
		return methodInterceptor;
	}

	public void setMethodInterceptor(MethodInterceptor methodInterceptor) {
		this.methodInterceptor = methodInterceptor;
	}

	public MethodMatcher getMethodMatcher() {
		return methodMatcher;
	}

	public void setMethodMatcher(MethodMatcher methodMatcher) {
		this.methodMatcher = methodMatcher;
	}
}
