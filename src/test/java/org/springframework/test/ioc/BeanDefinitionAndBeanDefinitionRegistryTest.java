package org.springframework.test.ioc;

import org.junit.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

/**
 * @author derekyi
 * @date 2020/11/24
 */
public class BeanDefinitionAndBeanDefinitionRegistryTest {

	@Test
	public void testBeanFactory() throws Exception {
		DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
		BeanDefinition beanDefinition = new BeanDefinition(HelloService.class);

		// bean注册时，只是把bean的名字和bean的class类型保存到beanDefinitionMap中
		beanFactory.registerBeanDefinition("helloService", beanDefinition);

		// bean第一次使用时，才实例化bean，保存在singletonObjects map中
		HelloService helloService = (HelloService) beanFactory.getBean("helloService");
		helloService.sayHello();

		// bean非第一次使用，可以直接从singletonObjects map中获取到单例的bean，无需实例化
		HelloService helloService1 = (HelloService) beanFactory.getBean("helloService");
		helloService1.sayHello();
	}
}
