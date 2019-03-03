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
 * ��Ӧ����
 * �����ÿ��ʵ�����ڱ�ʾһ�������͸��ͻ��˵�
 * ��Ӧ���ݡ�
 * @author live
 *
 */
public class HttpResponse {
	private Socket socket;
	private OutputStream out;
	
	/*
	 * ״̬�������Ϣ����
	 */
	//״̬����
	private int statusCode;
	
	/*
	 * ��Ӧͷ�����Ϣ
	 */
	private Map<String,String> headers = new HashMap<String,String>();
	
	/*
	 * ��Ӧ���������Ϣ����
	 */
	//Ҫ��Ӧ��ʵ���ļ�
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
	 * ����Ӧ���ݰ���HTTPЭ���ʽ���͸��ͻ���
	 */
	public void flush(){
		/*
		 * ��Ӧ�ͻ�����������
		 * 1������״̬��
		 * 2��������Ӧͷ
		 * 3��������Ӧ����
		 */
		sendStatusLine();
		sendHeaders();
		sendContent();
	}
	/*
	 * ����״̬��
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
	 * ������Ӧͷ
	 */
	private void sendHeaders(){
		try {
			//����headers,��������Ϣͷ�������ͻ���
			Set<Entry<String,String>> set = headers.entrySet();
			for(Entry<String,String> headers : set){
				//��ȡ��Ϣͷ����
				String name = headers.getKey();
				//��ȡ��Ϣͷ��Ӧ��ֵ
				String value = headers.getValue();
				String line = name+": "+value;
				println(line);
			}
		
//			String line = "Content-Type:text/html";
//			println(line);
//			
//			line = "Content-Length:"+entity.length();
//			println(line);
			
			
			//��ʾ��Ӧͷ���ַ������
			println("");
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	/*
	 * ������Ӧ����
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
	 * �������ַ������͸��ͻ��ˣ���CRLF��β��
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
	 * �ض���ָ��·��
	 * @param url
	 */
	public void sendRedirect(String url){
		//����״̬����
		this.statusCode = 302;
		//������Ӧͷ
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
	 * ������Ӧʵ���ļ�����
	 * �÷������Զ���Ӷ�Ӧ��������Ӧͷ��
	 * Content-Type��Content-Length
	 * @param entity
	 */
	public void setEntity(File entity) {
		this.entity = entity;
		/*
		 * 1:�����ӦͷContent-Length
		 */
		this.headers.put("Content-Length", entity.length()+"");
		/*
		 * 2�������ӦͷContent-Type
		 *    2.1:��ͨ��entity��ȡ���ļ�������
		 *    2.2����ȡ���ļ����ĺ�׺��
		 *    2.3��ͨ��HttpContext���ݸú�׺����ȡ��
		 *         ��Ӧ��Content-Type��ֵ
		 *    2.4����headers�����ø���Ӧͷ��Ϣ
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
	 * ���һ����Ӧͷ
	 * @param name ��Ӧͷ����
	 * @param value ��Ӧͷ��Ӧ��ֵ
	 */
	public void putHeaders(String name,String value){
		this.headers.put(name, value);
	}
	
}
