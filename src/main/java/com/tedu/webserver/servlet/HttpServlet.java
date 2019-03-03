package com.tedu.webserver.servlet;

import java.io.File;

import com.tedu.webserver.http.HttpRequest;
import com.tedu.webserver.http.HttpResponse;

/**
 * ����Servlet�ĳ��࣬�涨��Servlet�Ĺ��ܡ�
 * @author live
 *
 */
public abstract class HttpServlet {
	public abstract void service(HttpRequest request,HttpResponse response);
	/**
	 * ��ת��ָ��·��
	 * ��TOMCATʵ�����Ƕ�����ת�����ϵĹ��ܡ�
	 * TOMCAT����ʽ=�Ľṹ���������֮�䴮����һ�𣬽�
	 * ����ת���á�
	 * @param url
	 * @param request
	 * @param response
	 */
	public void forward(String url,HttpRequest request,HttpResponse response){
		response.setStatusCode(200);
		response.setEntity(new File("webapps"+url));
	}
}
