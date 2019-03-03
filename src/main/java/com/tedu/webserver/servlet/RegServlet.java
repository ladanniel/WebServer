package com.tedu.webserver.servlet;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Arrays;

import com.tedu.webserver.http.HttpRequest;
import com.tedu.webserver.http.HttpResponse;

/**
 * 处理注册业务
 * @author live
 *
 */
public class RegServlet extends HttpServlet{
	public void service(HttpRequest request, HttpResponse response){
		try {
			System.out.println("开始处理注册业务了！");
			/*
			 * 注册业务流程：
			 * 1：通过request获取用户在注册页面上输入的
			 *    注册信息
			 * 2：使用RandomAccessFile打开user.dat文件
			 * 3：将该用户信息写入文件4
			 * 4：设置reponse，响应注册成功页面。
			 */
			//1
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			String nickname = request.getParameter("nickname");
			int age = Integer.parseInt(request.getParameter("age"));
			
			//2
			try (
					RandomAccessFile raf = new RandomAccessFile("user.dat","rw");
			){
				raf.seek(raf.length());
				
				//写用户名
				byte[] data = username.getBytes("gbk");
				data = Arrays.copyOf(data, 32);
				raf.write(data);
				
				//写密码
				data = password.getBytes("gbk");
				data = Arrays.copyOf(data, 32);
				raf.write(data);
				
				//写昵称
				data = nickname.getBytes("gbk");
				data = Arrays.copyOf(data, 32);
				raf.write(data);
				
				//写年龄
				raf.writeInt(age);
				System.out.println("666666666");
				//响应注册页面
				forward("/myweb/reg_success.html",request,response);
				/*
				 * 设置重定向路径时注意，若使用相对路径，相对
				 * 的应当是请求当前Servlet时的路径
				 * 例如，请求RegServlet时，客户端路径为：
				 * http://localhost:8080/myweb/reg
				 * 那么当前目录就是：
				 * http://localhost:8080/myweb/
				 * 所以，在设置重定向目录时只需要设置为：
				 * reg_success.html即可
				 * 那么浏览器得到这个路径后会拼接在当前目录中：
				 * http://localhost:8080/myweb/reg_success.html
				 */
				response.sendRedirect("reg_success.html");
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
