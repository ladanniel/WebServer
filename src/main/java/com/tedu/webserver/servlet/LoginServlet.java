package com.tedu.webserver.servlet;

import java.io.File;
import java.io.RandomAccessFile;

import com.tedu.webserver.http.HttpRequest;
import com.tedu.webserver.http.HttpResponse;

/**
 * 处理登录流程
 * @author live
 *
 */
public class LoginServlet extends HttpServlet{
	public void service(HttpRequest request,HttpResponse response){
		//处理登录流程
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		try (
				RandomAccessFile raf = new RandomAccessFile("user.dat","r");
		){
			boolean check = false;
			for(int i = 0;i<raf.length()/100;i++){
				raf.seek(i*100);
				
				byte[] data = new byte[32];
				raf.read(data);
				String username_ =new String(data,"gbk").trim();
				
				raf.read(data);
				String password_ = new String(data,"gbk").trim();
				
				if(username_.equals(username) && password_.equals(password)){
					System.out.println("登陆成功");
					
					//响应登录成功的页面
					forward("/myweb/login_success.html",request,response);
					check = true;
				}
			}
			if(!check){
				System.out.println("登录失败");
				forward("/myweb/login_fail.html",request,response);
			}
			raf.close();
			
		} catch (Exception e) {
			
		}
	}
}
