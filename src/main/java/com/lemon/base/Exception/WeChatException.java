package com.lemon.base.Exception;

/**
 * 微信异常
 * @author chen
 *
 */
public class WeChatException extends Exception {
	


	/**
	 * 
	 */
	private static final long serialVersionUID = 1299314571179064274L;

	public WeChatException(String msg){
		super(msg);
	}
	
	public WeChatException(String message, Throwable cause){
		super(message, cause);
	}
}

