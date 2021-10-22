package org.springframework.beans.factory.support;

import cn.hutool.core.bean.BeanUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.BeanReference;

/**
 * @author derekyi
 * @date 2020/11/22
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory
		implements AutowireCapableBeanFactory {

	private InstantiationStrategy instantiationStrategy = new SimpleInstantiationStrategy();

	@Override
	protected Object createBean(String beanName, BeanDefinition beanDefinition) throws BeansException {
		return doCreateBean(beanName, beanDefinition);
	}

	protected Object doCreateBean(String beanName, BeanDefinition beanDefinition) {
		Object bean = null;
		try {
			bean = createBeanInstance(beanDefinition);
			//2.5 为bean填充属性
			applyPropertyValues(beanName, bean, beanDefinition);

			//2.6 执行bean的初始化方法和BeanPostProcessor的前置和后置处理方法
			initializeBean(beanName, bean, beanDefinition); // BeanPostProcessor核心调用代码，这这里处理前置和后置处理器，以及bean初始化
		} catch (Exception e) {
			throw new BeansException("Instantiation of bean failed", e);
		}

		addSingleton(beanName, bean);
		return bean;
	}

	/**
	 * 实例化bean
	 *
	 * @param beanDefinition
	 * @return
	 */
	protected Object createBeanInstance(BeanDefinition beanDefinition) {
		return getInstantiationStrategy().instantiate(beanDefinition);
	}

	/**
	 * 为bean填充属性
	 *
	 * @param bean
	 * @param beanDefinition
	 */
	protected void applyPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition) {
		try {
			for (PropertyValue propertyValue : beanDefinition.getPropertyValues().getPropertyValues()) {
				String name = propertyValue.getName();
				Object value = propertyValue.getValue();
				if (value instanceof BeanReference) {
					// beanA依赖beanB，先实例化beanB
					BeanReference beanReference = (BeanReference) value;
					value = getBean(beanReference.getBeanName());
				}

				//通过反射设置属性
				BeanUtil.setFieldValue(bean, name, value);
			}
		} catch (Exception ex) {
			throw new BeansException("Error setting property values for bean: " + beanName, ex);
		}
	}

	protected Object initializeBean(String beanName, Object bean, BeanDefinition beanDefinition) {
		//2.7 执行BeanPostProcessor的前置处理
		Object wrappedBean = applyBeanPostProcessorsBeforeInitialization(bean, beanName);

		//2.10 TODO 后面会在此处执行bean的初始化方法
		invokeInitMethods(beanName, wrappedBean, beanDefinition);

		//2.11 执行BeanPostProcessor的后置处理
		wrappedBean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
		return wrappedBean;
	}

	@Override
	public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName)
			throws BeansException {
		Object result = existingBean;
		// 2.8 遍历所有BeanPostProcessor，对bea执行进行初始化前的处理
		for (BeanPostProcessor processor : getBeanPostProcessors()) {
			Object current = processor.postProcessBeforeInitialization(result, beanName);
			if (current == null) {
				return result;
			}
			result = current;
		}
		return result;
	}

	@Override
	public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName)
			throws BeansException {

		Object result = existingBean;
		// 2.8 遍历所有BeanPostProcessor，对bea执行进行初始化后的处理
		for (BeanPostProcessor processor : getBeanPostProcessors()) {
			Object current = processor.postProcessAfterInitialization(result, beanName);
			if (current == null) {
				return result;
			}
			result = current;
		}
		return result;
	}

	/**
	 * 执行bean的初始化方法
	 *
	 * @param beanName
	 * @param bean
	 * @param beanDefinition
	 * @throws Throwable
	 */
	protected void invokeInitMethods(String beanName, Object bean, BeanDefinition beanDefinition) {
		//TODO 后面会实现
		System.out.println("执行bean[" + beanName + "]的初始化方法");
	}

	public InstantiationStrategy getInstantiationStrategy() {
		return instantiationStrategy;
	}

	public void setInstantiationStrategy(InstantiationStrategy instantiationStrategy) {
		this.instantiationStrategy = instantiationStrategy;
	}
}
