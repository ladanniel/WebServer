package com.tedu.webserver.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.util.Arrays;

import com.tedu.webserver.http.HttpRequest;
import com.tedu.webserver.http.HttpResponse;
import com.tedu.webserver.servlet.HttpServlet;
import com.tedu.webserver.servlet.LoginServlet;
import com.tedu.webserver.servlet.RegServlet;

public class ClientHandler implements Runnable{
	private Socket socket;

	public ClientHandler(Socket socket) {
		this.socket = socket;
	}
	public void run(){
		/*
		 * ����ÿͻ��˵�����Ĵ��²���
		 * 1����������,����HttpRequest
		 *    ������Ӧ����HttpResponse
		 * 2����������
		 * 3��������Ӧ
		 */
		try {
			//������������HttpRequest����
			HttpRequest request = new HttpRequest(socket);
			HttpResponse response = new HttpResponse(socket);
			
			//��������
			/*
			 * ͨ��request��ȡ�������Դ·������
			 * webapps��Ѱ�Ҷ�Ӧ��Դ
			 */
			String url = request.getRequestURI();
			/*
			 * �ж��Ƿ�����ע��ҵ��
			 * 1:�ȸ����û������ȡ��Ӧ��Servlet����
			 * 2�����õ������ֲ�Ϊnull��˵����Ӧ��ҵ��
			 */
			String servletName = ServletContext.getServletName(url);
			
			if(servletName!=null){
				//����Servlet
				Class cls = Class.forName(servletName);
				System.out.println("����"+url+",����ʵ������Ӧ�ģ�"+servletName);
				//ʵ����
				HttpServlet servlet = (HttpServlet)cls.newInstance();
				//����service���������ҵ��
				servlet.service(request, response);
				
			}else{
				File file = new File("webapps"+url);
				if(file.exists()){
					System.out.println("��Դ���ҵ���");
					/*
					 * ��һ����׼��HTTPЭ����Ӧ��ʽ�ظ��ͻ�����Դ
					 */
					response.setStatusCode(200);
					response.setEntity(file);

				}else{
					System.out.println("��Դδ�ҵ���");
					file = new File("webapps/myweb/404.html");
					//����״̬��
					response.setStatusCode(404);
					response.setEntity(file);
				}
			}
			response.flush();
			
//			/*
//			 * ���Զ�ȡ�ͻ��˷��͹���������ĵ�һ�����ݣ����������ݣ�
//			 */
//			String line = readLine(socket.getInputStream());
//			System.out.println(line);
		}catch (EmptyRequestException e){ 
			System.out.println("������");
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			//��Ӧ��ο�����
			try {
				if(socket!=null){
					socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
//	/**
//	 * ͨ����������������ȡһ���ַ�������CRLF��β��
//	 * @param in
//	 * @return
//	 */
//	public String readLine(InputStream in){
//
//		try {
//			StringBuilder builder = new StringBuilder();
//
//			int d = -1;
//			//c1��ʾ�ϴζ������ַ���c2��ʾ���ζ������ַ�
//			char c1 = 'a',c2 = 'a';
//			while((d = in.read())!=-1){
//				/*
//				 * ��ASC����CR�ı����Ӧ������Ϊ13
//				 * LF�����Ӧ������Ϊ10
//				 * �ͺñ��ַ�a�ı����Ӧ������Ϊ97
//				 */
//				 c2 =(char)d;
//				 if(c1==13 && c2==10){
//					 break;
//				 }
//			}
//			builder.append(c2);
//			c1=c2;
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return "";
//
//
//	}
}
