package com.tedu.webserver.http;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * ���ඨ����HTTPЭ�������Ϣ
 * @author live
 *
 */
public class HttpContext {
	/*
	 * ״̬�������Ӧ״̬������ӳ���ϵ
	 * Key:״̬����
	 * value:״̬����
	 */
	private static Map<Integer,String> STATUS_REASON_MAPPING = new HashMap<>();
	
	/*
	 * ��Դ��׺��Content-Type֮���ӳ���ϵ
	 * key:��Դ�ĺ�׺��
	 * value:����Դ��Ӧ��Content-Type��ֵ
	 * ע����ͬ����Դ��Ӧ��Content-Type��ֵ��w3c�϶��ж���
	 *     ��ǰ��w3c������ѯMIME����
	 */
	private static Map<String,String> MIME_MAPPING = new HashMap<String,String>();
	
	static{
		initStatusReasonMapping();
		initMimeMapping();
	}
	/**
	 * ��ʼ����Դ��׺����Content-Type��ӳ��Map
	 */
	public static void initMimeMapping(){
		/*
		 * ��ȡconf/web.xml�ļ�������Ԫ��������
		 * ��Ϊ<mime-mapping>����Ԫ�ض�ȡ������
		 * Ȼ��ÿ��<mime-mapping>Ԫ���е���Ԫ��
		 * <extension>�м���ı���Ϊkey������Ԫ��
		 * <mime-type>�м���ı���Ϊvalue������
		 * ��MIME_MAPPING�У���ɳ�ʼ����
		 */
		try {
			FileInputStream in = new FileInputStream("conf/web.xml");
			SAXReader reader = new SAXReader();
			Document doc = reader.read(in);
			in.close();
			
			Element root = doc.getRootElement();
			List<Element> mimeList = root.elements("mime-mapping");
			System.out.println(mimeList.size());
			
			for(Element mimeEle : mimeList){
				String key = mimeEle.elementText("extension");
				String value = mimeEle.elementText("mime-type");
				MIME_MAPPING.put(key, value);
			}
			
//			for(int i = 0;i<list.size();i++){
//				Element e = (Element) list.get(i);
//				//��ȡ<extension>
//				Element e1= e.element("extension");
//				String key = e1.getText();
//				
//				Element e2 = e.element("mime-type");
//				String value =e2.getText();
//				MIME_MAPPING.put(key, value);
//				
//			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
//		MIME_MAPPING.put("html", "text/html");
//		MIME_MAPPING.put("png", "image/png");
//		MIME_MAPPING.put("gif", "image/gif");
//		MIME_MAPPING.put("jpg", "image/jpeg");
//		MIME_MAPPING.put("css", "text/css");
//		MIME_MAPPING.put("js", "application/javascript");
	}
	
	/**
	 * ��ʼ��״̬������������ӳ��MAP
	 */
	private static void initStatusReasonMapping(){
		STATUS_REASON_MAPPING.put(200, "OK");
		STATUS_REASON_MAPPING.put(302, "Move Temporaily");
		STATUS_REASON_MAPPING.put(404, "Not Found");
		STATUS_REASON_MAPPING.put(500, "Internal Server Entity");
		
	}
	
	/**
	 * ������Դ��׺����ȡ��Ӧ��Content-Type��ֵ
	 * @param ext
	 * @return
	 */
	public static String getMimeType(String ext){
		return MIME_MAPPING.get(ext);
	}
	
	/**
	 * ���ݸ�����״̬�����ȡ��Ӧ��״̬����
	 * @param statusCode
	 * @return
	 */
	public static String getStatusReason(int statusCode){
		return STATUS_REASON_MAPPING.get(statusCode);
	}
	public static void main(String[] args) {
//		String str = getStatusReason(500);
//		System.out.println(str);
		String str = getMimeType("js");
		System.out.println(str);
		
	}
}
