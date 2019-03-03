package com.tedu.webserver.servlet;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Arrays;

import com.tedu.webserver.http.HttpRequest;
import com.tedu.webserver.http.HttpResponse;

/**
 * ����ע��ҵ��
 * @author live
 *
 */
public class RegServlet extends HttpServlet{
	public void service(HttpRequest request, HttpResponse response){
		try {
			System.out.println("��ʼ����ע��ҵ���ˣ�");
			/*
			 * ע��ҵ�����̣�
			 * 1��ͨ��request��ȡ�û���ע��ҳ���������
			 *    ע����Ϣ
			 * 2��ʹ��RandomAccessFile��user.dat�ļ�
			 * 3�������û���Ϣд���ļ�4
			 * 4������reponse����Ӧע��ɹ�ҳ�档
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
				
				//д�û���
				byte[] data = username.getBytes("gbk");
				data = Arrays.copyOf(data, 32);
				raf.write(data);
				
				//д����
				data = password.getBytes("gbk");
				data = Arrays.copyOf(data, 32);
				raf.write(data);
				
				//д�ǳ�
				data = nickname.getBytes("gbk");
				data = Arrays.copyOf(data, 32);
				raf.write(data);
				
				//д����
				raf.writeInt(age);
				System.out.println("666666666");
				//��Ӧע��ҳ��
				forward("/myweb/reg_success.html",request,response);
				/*
				 * �����ض���·��ʱע�⣬��ʹ�����·�������
				 * ��Ӧ��������ǰServletʱ��·��
				 * ���磬����RegServletʱ���ͻ���·��Ϊ��
				 * http://localhost:8080/myweb/reg
				 * ��ô��ǰĿ¼���ǣ�
				 * http://localhost:8080/myweb/
				 * ���ԣ��������ض���Ŀ¼ʱֻ��Ҫ����Ϊ��
				 * reg_success.html����
				 * ��ô������õ����·�����ƴ���ڵ�ǰĿ¼�У�
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
