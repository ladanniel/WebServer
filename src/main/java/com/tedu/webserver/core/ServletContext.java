package com.tedu.webserver.core;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 服务端环境信息
 * 
 * @author live
 *
 */
public class ServletContext {
	/**
	 * 请求与Servlet映射。
	 * key:请求路径
	 * value：对应Servlet的名字。
	 */
	private static Map<String,String> servletMapping = new HashMap<String,String>();
	
	static{
		initServletMapping();
	}
	
	/**
	 * 初始化请求与Servlet映射信息
	 */
	private static void initServletMapping(){
		/*
		 * 读取conf/server.xml文件
		 * 将所有<servlet>标签解析出来，用其中的
		 * url属性值作为key，ClassName属性的值最为
		 * value存入到servletMapping中
		 */
		try {
			SAXReader reader = new SAXReader();
			Document doc = reader.read(new File("conf/server.xml"));
//			System.out.println(doc);
			Element root = doc.getRootElement();
			Element servlets = root.element("servlets");
//			System.out.println(servlets);
			List<Element> list = servlets.elements("servlet");
			for(Element e:list){
				String key = e.attributeValue("url");
				String value = e.attributeValue("ClassName");
				servletMapping.put(key, value);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
//		servletMapping.put("/myweb/reg", "com.tedu.webserver.core.servlet.ReServlet");
//		servletMapping.put("/myweb/login", "com.tedu.webserver.core.servlet.LoginServlet");
	}
	
	public static String getServletName(String url){
		return servletMapping.get(url);
	}
	public static void main(String[] args){
		String servletName = getServletName("/myweb/login");
		System.out.println(servletName);
	}

	
}
