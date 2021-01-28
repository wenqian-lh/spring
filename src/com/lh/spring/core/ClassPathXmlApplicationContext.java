/**
 * 
 */
package com.lh.spring.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;
/**
 * 解析spring-bean.xml文件并实例化对象
 * @author lh
 * @data 2021年1月17日
 * Email 2944862497@qq.com
 */
public class ClassPathXmlApplicationContext {
	
	private List<Bean> beans = new ArrayList<Bean>();
	private Map<String, Object> singtons = new HashMap<String, Object>(); // 存放实例化出来的对象

	public ClassPathXmlApplicationContext(String path) throws Exception {
		if (path == null || "".equals(path)) {
			return;
		}	
		// 解析xml文件
		parseXml(path);
		
		// 实例化对象
		makeInstance();
	}
	
	/**
	 * 实例化对象
	 */
	@SuppressWarnings({ "unused", "rawtypes" })
	private void makeInstance() throws Exception {
		if (beans == null || beans.isEmpty()) {
			return;
		}
		
		String methodName = null;
		String name = null;
		Method method = null;
		Type type = null;
		Class c;
		Object obj;
		Method[] methods = null;
		Type[] types = null;
		Map<String, Method> setters = null;
		List<Property> properties = null;	
		
		for (Bean bean : beans) {
			c = Class.forName(bean.getClassName());
			properties = bean.getProperties();
			if (properties == null || properties.isEmpty()) {
				continue;
			}
			
			methods = c.getDeclaredMethods();
			setters = new HashMap<String, Method>();
			for (Method md : methods) {
				methodName = md.getName();
				if (methodName.startsWith("set")) {
					setters.put(methodName, md);
				}
			}
			
			obj = c.newInstance();
			
			for (Property prop : properties) {
				name = prop.getName();
				methodName = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
				
				method = setters.getOrDefault(methodName, null);
				if (method == null) {
					continue;
				}
				
				// 获取方法的所有形参
				types = method.getParameterTypes();
				if (types == null || types.length <= 0) {
					continue;
				}
				
				type = types[0]; // 因为set方法只有一个形参
				
				if (Integer.TYPE == type || Integer.class == type) {
					method.invoke(obj, Integer.parseInt(prop.getValue()));
				} else if (Float.TYPE == type || Float.class == type) {
					method.invoke(obj, Float.parseFloat(prop.getValue()));
				} else if (Double.TYPE == type || Double.class == type) {
					method.invoke(obj, Double.parseDouble(prop.getValue()));
				} else {
					method.invoke(obj, prop.getValue());
				}
			}
			singtons.put(bean.getId(), obj);
		}
		singtons.forEach((key, val) -> {
			System.out.println(key + ": " + val);
		});
		
	}

	/**
	 * 解析xml文件
	 */
	private void parseXml(String path) {
		
		
		SAXReader read = new SAXReader();
		Document doc = null;
		try(InputStream is = this.getClass().getClassLoader().getResourceAsStream(path)) {		
			doc = read.read(is);		
			
			Map<String, String> namescope = new HashMap<String, String>();
			namescope.put("lh", "http://www.springframework.org/schema/beans");
			XPath xpath = doc.createXPath("//lh:beans//lh:bean");
			xpath.setNamespaceURIs(namescope);
			List<Element> list = xpath.selectNodes(doc);
			
			if (list == null || list.isEmpty()) {
				return;
			}
			
			String id = null;
			String className = null;
			Bean bean = null;
			List<Property> properties = null;
			List<Element> children = null;
			for (Element el : list) {
				id = el.attributeValue("id");
				className = el.attributeValue("class");
				
				children = el.elements(); // 获取当前节点下的子节点
				if (children == null || children.isEmpty()) {
					return;
				}
				properties = new ArrayList<Property>();
				for (Element e : children) {
					properties.add(new Property(e.attributeValue("name"), e.attributeValue("value"), e.attributeValue("ref")));
				}
				
				bean = new Bean();
				bean.setId(id);
				bean.setClassName(className);
				bean.setProperties(properties);
				System.out.println(bean);
				beans.add(bean);
			}
						
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

}
