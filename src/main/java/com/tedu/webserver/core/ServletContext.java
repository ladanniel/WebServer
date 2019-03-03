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
 * ����˻�����Ϣ
 * 
 * @author live
 *
 */
public class ServletContext {
	/**
	 * ������Servletӳ�䡣
	 * key:����·��
	 * value����ӦServlet�����֡�
	 */
	private static Map<String,String> servletMapping = new HashMap<String,String>();
	
	static{
		initServletMapping();
	}
	
	/**
	 * ��ʼ��������Servletӳ����Ϣ
	 */
	private static void initServletMapping(){
		/*
		 * ��ȡconf/server.xml�ļ�
		 * ������<servlet>��ǩ���������������е�
		 * url����ֵ��Ϊkey��ClassName���Ե�ֵ��Ϊ
		 * value���뵽servletMapping��
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
