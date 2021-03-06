package com.tedu.webserver.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 响应对象
 * 该类的每个实例用于表示一个服务发送给客户端的
 * 响应内容。
 * @author live
 *
 */
public class HttpResponse {
	private Socket socket;
	private OutputStream out;
	
	/*
	 * 状态行相关信息定义
	 */
	//状态代码
	private int statusCode;
	
	/*
	 * 响应头相关信息
	 */
	private Map<String,String> headers = new HashMap<String,String>();
	
	/*
	 * 响应正文相关信息定义
	 */
	//要响应的实体文件
	private File entity;

	
	public HttpResponse(Socket socket) {
		this.socket = socket;
		try {
			this.out = socket.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/*
	 * 将响应内容按照HTTP协议格式发送给客户端
	 */
	public void flush(){
		/*
		 * 响应客户端做三件事
		 * 1：发送状态行
		 * 2：发送响应头
		 * 3：发送响应正文
		 */
		sendStatusLine();
		sendHeaders();
		sendContent();
	}
	/*
	 * 发送状态行
	 */
	private void sendStatusLine(){
		try {
			String line = "HTTP/1.1"+" "+statusCode+" "+HttpContext.getStatusReason(statusCode);
			println(line);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*
	 * 发送响应头
	 */
	private void sendHeaders(){
		try {
			//遍历headers,将所有消息头发送至客户端
			Set<Entry<String,String>> set = headers.entrySet();
			for(Entry<String,String> headers : set){
				//获取消息头名字
				String name = headers.getKey();
				//获取消息头对应的值
				String value = headers.getValue();
				String line = name+": "+value;
				println(line);
			}
		
//			String line = "Content-Type:text/html";
//			println(line);
//			
//			line = "Content-Length:"+entity.length();
//			println(line);
			
			
			//表示响应头部分发送完毕
			println("");
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	/*
	 * 发送响应正文
	 */
	private void sendContent(){
		if(entity!=null){
			try(
					FileInputStream fis = new FileInputStream(entity);
			) {
				byte[] data = new byte[1024*10];
				int len = -1;
				while((len = fis.read(data))!=-1){
					out.write(data, 0, len);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 将给定字符串发送给客户端（以CRLF结尾）
	 * @param line
	 */
	private void println(String line){
		try {
			out.write(line.getBytes("ISO8859-1"));
			out.write(13);
			out.write(10);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 重定向到指定路径
	 * @param url
	 */
	public void sendRedirect(String url){
		//设置状态代码
		this.statusCode = 302;
		//设置响应头
		this.headers.put("Location", url);
	}
	
	
	
	public Socket getSocket() {
		return socket;
	}
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	public OutputStream getOut() {
		return out;
	}
	public void setOut(OutputStream out) {
		this.out = out;
	}
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public File getEntity() {
		return entity;
	}
	
	/**
	 * 设置响应实体文件数据
	 * 该方法会自动添加对应的两个响应头：
	 * Content-Type与Content-Length
	 * @param entity
	 */
	public void setEntity(File entity) {
		this.entity = entity;
		/*
		 * 1:添加响应头Content-Length
		 */
		this.headers.put("Content-Length", entity.length()+"");
		/*
		 * 2：添加响应头Content-Type
		 *    2.1:先通过entity获取该文件的名字
		 *    2.2：获取该文件名的后缀名
		 *    2.3：通过HttpContext根据该后缀名获取到
		 *         对应的Content-Type的值
		 *    2.4：向headers中设置该响应头信息
		 */
		//2.1
		String name = entity.getName();
		//2.2
		String ext = name.substring(name.lastIndexOf(".")+1);
		//2.3
		String type = HttpContext.getMimeType(ext);
		this.headers.put("Content-Type", type);
	}
	
	/**
	 * 添加一个响应头
	 * @param name 响应头名字
	 * @param value 响应头对应的值
	 */
	public void putHeaders(String name,String value){
		this.headers.put(name, value);
	}
	
}
