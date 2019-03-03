package com.tedu.webserver.core;
/**
 * 空请求异常
 * 当客户端连接后发送空请求时，HttpRequest的构造方法
 * 会抛出该异常
 * @author live
 *
 */
public class EmptyRequestException extends Exception{
	
	
	public EmptyRequestException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EmptyRequestException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public EmptyRequestException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public EmptyRequestException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public EmptyRequestException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
}
