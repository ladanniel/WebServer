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
		 * 处理该客户端的请求的大致步骤
		 * 1：解析请求,创建HttpRequest
		 *    创建响应对象HttpResponse
		 * 2：处理请求
		 * 3：给予响应
		 */
		try {
			//解析请求，生成HttpRequest对象
			HttpRequest request = new HttpRequest(socket);
			HttpResponse response = new HttpResponse(socket);
			
			//处理请求
			/*
			 * 通过request获取请求的资源路径，从
			 * webapps中寻找对应资源
			 */
			String url = request.getRequestURI();
			/*
			 * 判断是否请求注册业务
			 * 1:先根据用户请求获取对应的Servlet名字
			 * 2：若得到的名字不为null，说明对应的业务。
			 */
			String servletName = ServletContext.getServletName(url);
			
			if(servletName!=null){
				//加载Servlet
				Class cls = Class.forName(servletName);
				System.out.println("请求"+url+",正在实例化对应的："+servletName);
				//实例化
				HttpServlet servlet = (HttpServlet)cls.newInstance();
				//调用service方法处理该业务
				servlet.service(request, response);
				
			}else{
				File file = new File("webapps"+url);
				if(file.exists()){
					System.out.println("资源已找到！");
					/*
					 * 以一个标准的HTTP协议响应格式回复客户端资源
					 */
					response.setStatusCode(200);
					response.setEntity(file);

				}else{
					System.out.println("资源未找到！");
					file = new File("webapps/myweb/404.html");
					//发送状态行
					response.setStatusCode(404);
					response.setEntity(file);
				}
			}
			response.flush();
			
//			/*
//			 * 测试读取客户端发送过来的请求的第一行内容（请求行内容）
//			 */
//			String line = readLine(socket.getInputStream());
//			System.out.println(line);
		}catch (EmptyRequestException e){ 
			System.out.println("空请求！");
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			//响应后段开链接
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
//	 * 通过给定的输入流读取一行字符串（以CRLF结尾）
//	 * @param in
//	 * @return
//	 */
//	public String readLine(InputStream in){
//
//		try {
//			StringBuilder builder = new StringBuilder();
//
//			int d = -1;
//			//c1表示上次读到的字符，c2表示本次独到的字符
//			char c1 = 'a',c2 = 'a';
//			while((d = in.read())!=-1){
//				/*
//				 * 在ASC编码CR的编码对应的数字为13
//				 * LF编码对应的数字为10
//				 * 就好比字符a的编码对应的数字为97
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
