package com.tedu.webserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import com.tedu.webserver.core.EmptyRequestException;

/**
 * HttpRequest��ʾһ��HttpЭ��Ҫ���������Ϣ
 * һ����������������֣�
 * �����У���Ϣͷ����Ϣ����
 * 
 * @author live
 *
 */
public class HttpRequest {
	//��Ӧ�ͻ��˵�Socket
	private Socket socket;
	//ͨ��Socket��ȡ�������������ڶ�ȡ�ͻ��˷��͹���������
	private InputStream in;
	
	/*
	 * �����������Ϣ
	 */
	//����ʽ
	private String method;
	
	//��Դ·��
	private String url;
	
	//����ʹ�õ�Э��汾
	private String protocol;
	
	//url�����󲿷�
	private String queryString;
	
	private String requestURI;
	
	//url�е����в���
	//key:������ value������ֵ
	private Map<String,String> parameters = new HashMap<String,String>();
	
	
	
	
	
	/*
	 * ��Ϣͷ�����Ϣ
	 */
	private Map<String,String> headers = new HashMap<String,String>();
	private String key;
	private String value;
	private int length;
	/**
	 * ʵ����HttpRequestʹ�õĹ��췽������Ҫ����Ӧ
	 * �ͻ��˵�Socket���룬�Ա��ȡ�ÿͻ��˷��͹��� 
	 * ����������
	 * 
	 * @param socket
	 * @throws EmptyRequestException 
	 */
	public HttpRequest(Socket socket) throws EmptyRequestException{
		this.socket = socket;
		try {
			in = this.socket.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("HttpRequest:��ʼ��������");
		try {
			/*
			 * 1:����������
			 * 2��������Ϣͷ
			 * 3��������Ϣ����
			 */
			//1
			parseRequestLine();
			//2
			parseHeaders();
			//3
			parseContent();
			
		} catch (EmptyRequestException e) {
			//���������׳���ClientHandler
			throw e;
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*
	 * ����������
	 */
	private void parseRequestLine() throws EmptyRequestException{
		System.out.println("����������...");
		/*
		 * �������̣�
		 * 1��ͨ����������ȡ��һ���ַ���
		 * 2���������а��տո���Ϊ����
		 * 3������ֵ�����ֱ����õ�method,url,protocol���������ϡ�
		 * 
		 */
		
		/*
		 * ����������ʱ���ڻ�ȡ��ֺ������Ԫ��ʱ
		 * ���ܻ����������±�Խ�磬��������HTTPЭ��
		 * ����ͻ��˷���һ��������������µġ�
		 * ���Ǻ�������
		 */
		String line = readLine();
		System.out.println("�����У�"+line);
		String[] data = line.split("\\s");
		
		//��������������Ƿ��ܴﵽ3��
		if(data.length<3){
			//����һ��������
			throw new EmptyRequestException();
		}
		this.method = data[0];
		this.url = data[1];
		//��һ������url����
		parseURI();
		this.protocol = data[2];
		
		System.out.println("method:"+method);
		System.out.println("url:"+url);
		System.out.println("protocol:"+protocol);
		System.out.println("������������ϣ�");
	}
	
	/**
	 *��һ����url���н���
	 *��url�е����󲿷����õ�����requestURI��
	 *��url�еĲ����������õ�����queryString��
	 *�ڶԲ������ֽ�һ����������ÿ���������ִ��뵽����parameters��
	 *
	 * ����url�����в������֣���ֱ�ӽ�url��ֵ��ֵ��
	 * requestURI���������ֲ����κδ���
	 */
	private void parseURI(){
		System.out.println("��ʼ����url:"+url);
		/*
		 * ˼·��
		 * url�Ƿ��в��������Ը��ݸ�url���Ƿ��У�
		 * ���������������գ����Ϊ������
		 * ��һ����Ϊ���󲿷֣��ڶ�����Ϊ��������
		 * ���õ���Ӧ���Լ��ɡ�Ȼ���ڶԲ������в��
		 * ���ս�ÿ��������������Ϊkey��ֵ��Ϊvalue
		 * ���뵽parameters��
		 * ��������������ֱ�ӽ�url��ֵ��requesstURI���ɡ�
		 * 
		 *  /myweb/reg.html
		 * /myweb/reg?username=fan&password=123&
		 */
		if(this.url.contains("?")){
//			System.out.println("��");
			String[] data =this. url.split("\\?");
			this.requestURI = data[0];
			if(data.length>1){
				this.queryString = data[1];
				/*
				 * �Բ������ֽ���ת�������������%xx������
				 * ���ն�Ӧ�ַ�����ԭΪ�ַ�����
				 */
				System.out.println("��ʼ��queryStringת�룺");
				System.out.println("ת��ǰ��"+queryString);
				
				try {
					queryString = URLDecoder.decode(queryString, "gbk");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				
				System.out.println("ת���"+queryString);
				
				//��һ������queryString
				parseParameters(queryString);
				

			}
			
			
		}else{
			this.requestURI = url;
		}
		
		System.out.println("requestURI:"+requestURI);
		System.out.println("queryString:"+queryString);
		System.out.println("parameters:"+parameters);
		System.out.println("url������ϣ�");
	}
	
	/**
	 * ���ͻ��˷��͹����ĵĲ������н���
	 * ��ʽΪ��name1-valuel&name2= value2&
	 * @param line
	 */
	private void parseParameters(String line){

		
		String[] paras = line.split("&");
		for(String paraStr: paras){
			String[] parData = paraStr.split("=");
			if(parData.length>1){
				this.parameters.put(parData[0], parData[1]);
			}else{
				this.parameters.put(parData[0], "");
			}
		}
	}
	
	
	/*
	 * ������Ϣͷ
	 */
	private void parseHeaders(){
		System.out.println("������Ϣͷ...");
		/*
		 * ���²��裺
		 * 1������ʹ��readLine������ȡ����������
		 *    ÿһ��Ӧ�ö���һ����Ϣͷ
		 * 2����readLine��������ֵΪ���ַ���ʱ��
		 *    ֹͣѭ����ȡ������������ȡ����CRLFʱ
		 *    readLine��������ֵӦ��Ϊ���ַ�����
		 * 3��ÿ����ȡһ����Ϣͷ��ϢʱӦ������":"
		 *    ���Ϊ�����һ��Ϊ��Ϣͷ���֣��ڶ���Ϊ
		 *    ��Ϣͷ��Ӧ��ֵ����������Ϊkey����
		 *    ֵ��Ϊvalue���뵽����headers���Map�С�
		 * 
		 */
		while(true){
			String line = readLine();
//			System.out.println("��Ϣͷ��"+line);
			if("".equals(line)){
				break;
			}
			String[]data = line.split(":\\s");
//			System.out.println(data.length);
			headers.put(data[0],data[1]);

		}

		
		 
		System.out.println("headers:"+headers);
		System.out.println("������Ϣͷ���!");
	}
	/*
	 * ������Ϣ����
	 */
	private void parseContent(){
		System.out.println("������Ϣ����...");
		/*
		 * ��һ��form����post��ʽ�ύʱ����ô
		 * ���е����ݻᱻ��������Ϣ�����У�ͨ��
		 * ��Ϣͷ���Ƿ���Content-Length��
		 * Content-Type�Ϳ��Ե�֪�Ƿ�����Ϣ����
		 * �Լ���Ϣ���ĵ�������ʲô���Ա㿪ʼ
		 * ������Ϣ���ġ�
		 */
		//�Ƿ�����Ϣ����?
		if(headers.containsValue("Content-Length")){
			int length = Integer.parseInt(headers.get("Content-Length"));
			String type = headers.get("Content-Type");
			System.out.println("��Ϣ�������ͣ�"+type);
		}
		/*
		 * ���������ж���������
		 */
		//�ж��Ƿ�Ϊform���ύ������
		if("application/x-www-form-urlencoded".equals(queryString)){
			/*
			 * ͨ����������ȡָ���ֽ�����ԭΪ�ַ���
			 */
			try {
				byte[] data = new byte[length];
				in.read(data);
				String line = new String(data,"ISO8859-1").trim();
				System.out.println("�������ݣ�"+line);
				
				//ת��
				line = URLDecoder.decode(line, "gbk");
				//��������
				parseParameters(line);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("������Ϣ������ϣ�");
	}
	
	/**
	 * ͨ����������������ȡһ���ַ�������CRLF��β��
	 * @return
	 */
	public String readLine(){

		try {
			StringBuilder builder = new StringBuilder();

			int d = -1;
			//c1��ʾ�ϴζ������ַ���c2��ʾ���ζ������ַ�
			char c1 = 'a',c2 = 'a';
			while((d = in.read())!=-1){
				/*
				 * ��ASC����CR�ı����Ӧ������Ϊ13
				 * LF�����Ӧ������Ϊ10
				 * �ͺñ��ַ�a�ı����Ӧ������Ϊ97
				 */
				 c2 =(char)d;
				 if(c1==13 && c2==10){
					 break;
				 }
				 builder.append(c2);
					c1=c2;
			}
			return builder.toString().trim();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	public Socket getSocket() {
		return socket;
	}
	public String getMethod() {
		return method;
	}
	public String getUrl() {
		return url;
	}
	public String getProtocol() {
		return protocol;
	}
	public Map<String, String> getHeaders() {
		return headers;
	}
	public String getqueryString() {
		return queryString;
	}
	public String getRequestURI() {
		return requestURI;
	}
	
	public String getParameter(String name){
		return this.parameters.get(name);
	}
	
	
}

	

