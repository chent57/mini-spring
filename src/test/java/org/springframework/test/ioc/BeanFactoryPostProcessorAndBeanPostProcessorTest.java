package org.springframework.test.ioc;

import org.junit.Test;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.test.ioc.bean.Car;
import org.springframework.test.ioc.bean.Person;
import org.springframework.test.ioc.common.CustomBeanFactoryPostProcessor;
import org.springframework.test.ioc.common.CustomerBeanPostProcessor;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author derekyi
 * @date 2020/11/28
 */
public class BeanFactoryPostProcessorAndBeanPostProcessorTest {
	/*关于BeanFactoryPostProcessor和BeanPostProcessor
	* 作用概述：都是spring初始化bean的扩展点
	* 区别：
	* 		1. BeanFactoryPostProcessor是针对于beanFactory的扩展点，可以修改、增加beanDefinition（bean的定义）
	* 		2. BeanPostProcessor是针对bean的扩展点，即spring会在bean初始化前后调用方法对bean进行处理
	 * 执行时机：
	 * 		1. BeanFactoryPostProcessor：spring会在beanFactory初始化之后，beanDefinition都已经loaded，但是bean还未创建前进行调用
	 * 		2. BeanPostProcessor：实例化前后
	*
	* */

	@Test
	public void testBeanFactoryPostProcessor() throws Exception {
		DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
		XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
		// 1.1 从xml文件读取并注册bean
		beanDefinitionReader.loadBeanDefinitions("classpath:spring.xml");

		//在所有BeanDefintion加载完成后，但在bean实例化之前，修改BeanDefinition的属性值
		// 1.2 为bean工厂注册BeanFactoryPostProcessor，这个BeanFactoryPostProcessor是自定义的，仅包含后置处理器
		CustomBeanFactoryPostProcessor beanFactoryPostProcessor = new CustomBeanFactoryPostProcessor();

		// 1.3 对bean工厂的某个bean做修改
		beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);

		Person person = (Person) beanFactory.getBean("person");
		System.out.println(person);
		//name属性在CustomBeanFactoryPostProcessor中被修改为ivy
		assertThat(person.getName()).isEqualTo("ivy");
	}

	@Test
	public void testBeanPostProcessor() throws Exception {
		DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
		XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
		// 2.1 从xml文件读取并注册bean
		beanDefinitionReader.loadBeanDefinitions("classpath:spring.xml");

		//添加bean实例化后的处理器
		// 2.2 为bean工厂注册BeanPostProcessor，这个BeanPostProcessor是自定义的，包含前置和后置处理器
		CustomerBeanPostProcessor customerBeanPostProcessor = new CustomerBeanPostProcessor();
		beanFactory.addBeanPostProcessor(customerBeanPostProcessor);

		// 2.3 实例化并获取bean
		Car car = (Car) beanFactory.getBean("car");
		System.out.println(car);
		//brand属性在CustomerBeanPostProcessor中被修改为lamborghini
		assertThat(car.getBrand()).isEqualTo("lamborghini");
	}
}
