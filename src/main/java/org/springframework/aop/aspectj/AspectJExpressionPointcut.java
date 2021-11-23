package org.springframework.aop.aspectj;

import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.PointcutParser;
import org.aspectj.weaver.tools.PointcutPrimitive;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * @author derekyi
 * @date 2020/12/5
 */
public class AspectJExpressionPointcut implements Pointcut, ClassFilter, MethodMatcher {
	/*
	* AspectJExpressionPointcut是支持AspectJ切点表达式的PointCut实现，简单实现仅支持execution函数。
	* */

	// 支持原语
	private static final Set<PointcutPrimitive> SUPPORTED_PRIMITIVES = new HashSet<PointcutPrimitive>();

	static {
		SUPPORTED_PRIMITIVES.add(PointcutPrimitive.EXECUTION);
	}

	private final PointcutExpression pointcutExpression;

	public AspectJExpressionPointcut(String expression) {
		PointcutParser pointcutParser = PointcutParser.getPointcutParserSupportingSpecifiedPrimitivesAndUsingSpecifiedClassLoaderForResolution(SUPPORTED_PRIMITIVES, this.getClass().getClassLoader());
		pointcutExpression = pointcutParser.parsePointcutExpression(expression);
	}

	/**
	 * 是否匹配这个类
	 * @param clazz 类
	 * @return 返回是否匹配类
	 */
	@Override
	public boolean matches(Class<?> clazz) {
		return pointcutExpression.couldMatchJoinPointsInType(clazz);
	}

	/**
	 * 是否匹配类中的方法
	 * @param method 方法
	 * @param targetClass 类
	 * @return 是否匹配类中的方法
	 */
	@Override
	public boolean matches(Method method, Class<?> targetClass) {
		return pointcutExpression.matchesMethodExecution(method).alwaysMatches();
	}

	@Override
	public ClassFilter getClassFilter() {
		return this;
	}

	@Override
	public MethodMatcher getMethodMatcher() {
		return this;
	}
}
