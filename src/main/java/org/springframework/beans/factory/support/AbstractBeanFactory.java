package org.springframework.beans.factory.support;

import cn.hutool.core.lang.Assert;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author derekyi
 * @date 2020/11/22
 */
public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements ConfigurableBeanFactory {

	private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

	@Override
	public Object getBean(String name) throws BeansException {
		// 11. 在使用bean时如果bean是singleton的，这里就可以直接获取到了
		Object bean = getSingleton(name);
		if (bean != null) {
			return bean;
		}

		BeanDefinition beanDefinition = getBeanDefinition(name);
		// 12. 如果bean是prototype的，第一次使用时进行实例化
		return createBean(name, beanDefinition);
	}

	@Override
	public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
		return ((T) getBean(name));
	}

	protected abstract Object createBean(String beanName, BeanDefinition beanDefinition) throws BeansException;

	protected abstract BeanDefinition getBeanDefinition(String beanName) throws BeansException;

	@Override
	public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
		//有则覆盖
		this.beanPostProcessors.remove(beanPostProcessor);
		this.beanPostProcessors.add(beanPostProcessor);
	}

	public List<BeanPostProcessor> getBeanPostProcessors() {
		return this.beanPostProcessors;
	}
}
