package org.springframework.test.ioc.common;

import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;

/**
 * @author derekyi
 * @date 2020/11/28
 */
public class CustomBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		// 1.4 获取指定BeanDefinition
		BeanDefinition personBeanDefiniton = beanFactory.getBeanDefinition("person");

		// 1.5 获取BeanDefinition的属性值
		PropertyValues propertyValues = personBeanDefiniton.getPropertyValues();

		// 1.6 将person的name属性改为ivy
		propertyValues.addPropertyValue(new PropertyValue("name", "ivy"));

		// 1.7 添加属性值
		propertyValues.addPropertyValue(new PropertyValue("age", "18"));
	}
}
