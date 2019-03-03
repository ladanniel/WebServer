package com.tedu.webserver.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * WebServer����
 * @author live
 *
 */
public class WebServer {
	private ServerSocket server;
	
	private ExecutorService threadPool;
	
	public WebServer() {
		try {
			//TomcatĬ�Ͽ����Ķ˿ھ���8080
			server = new ServerSocket(8080);
			threadPool = Executors.newFixedThreadPool(50);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	public void start(){
		
		try {
			while(true){
				System.out.println("�ȴ��ͻ�������......");
				Socket socket = server.accept();
				System.out.println("һ���ͻ������ӣ�");
				
				//����һ���̣߳�����ÿͻ�������
				ClientHandler handler = new ClientHandler(socket);
				//���������̳߳ش���
				threadPool.execute(handler);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args){
		WebServer server = new WebServer();
		server.start();
	}
	
}
