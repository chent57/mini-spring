package org.springframework.beans.factory.xml;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinitionReader;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;

/**
 * 读取配置在xml文件中的bean定义信息
 *
 * @author derekyi
 * @date 2020/11/26
 */
public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {

	public static final String BEAN_ELEMENT = "bean";
	public static final String PROPERTY_ELEMENT = "property";
	public static final String ID_ATTRIBUTE = "id";
	public static final String NAME_ATTRIBUTE = "name";
	public static final String CLASS_ATTRIBUTE = "class";
	public static final String VALUE_ATTRIBUTE = "value";
	public static final String REF_ATTRIBUTE = "ref";

	public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
		super(registry);
	}

	public XmlBeanDefinitionReader(BeanDefinitionRegistry registry, ResourceLoader resourceLoader) {
		super(registry, resourceLoader);
	}

	@Override
	public void loadBeanDefinitions(String location) throws BeansException {
		// 2. 通过String类型的location获取指定类型的Resource
		ResourceLoader resourceLoader = getResourceLoader();
		Resource resource = resourceLoader.getResource(location);
		loadBeanDefinitions(resource);
	}

	@Override
	public void loadBeanDefinitions(Resource resource) throws BeansException {
		try {
			// 3. 通过Resource获取InputStream
			InputStream inputStream = resource.getInputStream();
			try {
				doLoadBeanDefinitions(inputStream);
			} finally {
				inputStream.close();
			}
		} catch (IOException ex) {
			throw new BeansException("IOException parsing XML document from " + resource, ex);
		}
	}

	protected void doLoadBeanDefinitions(InputStream inputStream) {
		// 4. 通过InputStream读取XML到Document，解析XML获取到各个bean和属性信息
		Document document = XmlUtil.readXML(inputStream);
		Element root = document.getDocumentElement();
		NodeList childNodes = root.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			if (childNodes.item(i) instanceof Element) {
				if (BEAN_ELEMENT.equals(((Element) childNodes.item(i)).getNodeName())) {
					//解析bean标签
					Element bean = (Element) childNodes.item(i);
					String id = bean.getAttribute(ID_ATTRIBUTE);
					String name = bean.getAttribute(NAME_ATTRIBUTE);
					String className = bean.getAttribute(CLASS_ATTRIBUTE);

					Class<?> clazz = null;
					try {
						clazz = Class.forName(className);
					} catch (ClassNotFoundException e) {
						throw new BeansException("Cannot find class [" + className + "]");
					}
					//id优先于name
					String beanName = StrUtil.isNotEmpty(id) ? id : name;
					if (StrUtil.isEmpty(beanName)) {
						//如果id和name都为空，将类名的第一个字母转为小写后作为bean的名称
						beanName = StrUtil.lowerFirst(clazz.getSimpleName());
					}

					BeanDefinition beanDefinition = new BeanDefinition(clazz);

					for (int j = 0; j < bean.getChildNodes().getLength(); j++) {
						if (bean.getChildNodes().item(j) instanceof Element) {
							if (PROPERTY_ELEMENT.equals(((Element) bean.getChildNodes().item(j)).getNodeName())) {
								//解析property标签
								Element property = (Element) bean.getChildNodes().item(j);
								String nameAttribute = property.getAttribute(NAME_ATTRIBUTE);
								String valueAttribute = property.getAttribute(VALUE_ATTRIBUTE);
								String refAttribute = property.getAttribute(REF_ATTRIBUTE);

								if (StrUtil.isEmpty(nameAttribute)) {
									throw new BeansException("The name attribute cannot be null or empty");
								}

								Object value = valueAttribute;
								if (StrUtil.isNotEmpty(refAttribute)) {
									value = new BeanReference(refAttribute);
								}
								PropertyValue propertyValue = new PropertyValue(nameAttribute, value);
								// 5. 为bean填充属性值
								beanDefinition.getPropertyValues().addPropertyValue(propertyValue);
							}
						}
					}
					if (getRegistry().containsBeanDefinition(beanName)) {
						//beanName不能重名
						throw new BeansException("Duplicate beanName[" + beanName + "] is not allowed");
					}
					//注册BeanDefinition
					// 6. 注册bean
					getRegistry().registerBeanDefinition(beanName, beanDefinition);
				}
			}
		}
	}
}
