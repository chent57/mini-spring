package org.springframework.aop;

/**
 * @author derekyi
 * @date 2020/12/5
 */
public interface ClassFilter {
	/*
	* 最常用的切点表达式是AspectJ的切点表达式。需要匹配类，定义ClassFilter接口；匹配方法，定义MethodMatcher接口。
	* */

	boolean matches(Class<?> clazz);
}
