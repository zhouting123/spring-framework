package com.zt.beanInit;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;

/**
 * @author zhouTing
 * @date 2021/05/14 14:38
 */
public class Test {

	public static void main(String[] args) {
		// 创建bean容器
		DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
		// 创建xml reader
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
		// class路径
		String location = "classpath:com.zt/dependency-lookup-context.xml";
		// 加载配置
		int beanDefinitionsCount = reader.loadBeanDefinitions(location);
		System.out.println("bean定义加载数量=" + beanDefinitionsCount);
		// 依赖查找
		System.out.println(factory.getBean("user"));


	}

}
