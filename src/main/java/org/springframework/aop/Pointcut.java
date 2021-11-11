package org.springframework.aop;


/**
 * 切点抽象
 *
 * @author derekyi
 * @date 2020/12/5
 */
public interface Pointcut {
	/*
	* Pointcut是JoinPoint（切点）的表述方式，能捕获JoinPoint（切点）。
	* */

	ClassFilter getClassFilter();

	MethodMatcher getMethodMatcher();
}
