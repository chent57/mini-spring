package org.springframework.test.ioc;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * @author derekyi
 * @date 2020/11/29
 */
public class InitAndDestoryMethodTest {

	@Test
	public void testInitAndDestroyMethod() throws Exception {
		// 1. 读取xml文件BeanDefinition
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:init-and-destroy-method.xml");

		// 17.向虚拟机中注册一个钩子方法
		applicationContext.registerShutdownHook();  //或者手动关闭 applicationContext.close();
	}
}
