package org.springframework.test.ioc;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.ioc.common.event.CustomEvent;

/**
 * @author derekyi
 * @date 2020/12/5
 */
public class EventAndEventListenerTest {
	/*
	* 1. 发布容器刷新事件
	* 2. 发布自定义事件
	* 3. 发布容器关闭事件
	* */

	@Test
	public void testEventListener() throws Exception {
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:event-and-event-listener.xml");
		// 4. 发布自定事件
		applicationContext.publishEvent(new CustomEvent(applicationContext));

		// 5.注册容器关闭事件
		applicationContext.registerShutdownHook();//或者applicationContext.close()主动关闭容器;
	}
}
