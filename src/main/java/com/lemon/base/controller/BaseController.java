package com.lemon.base.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.lemon.base.Exception.WeChatException;

/**
 * 
 * <p>
 * 基础控制器，系统错误屏障，提供通用的异常 -> http响应 映射处理
 * </p>
 * 
 * 
 *
 * @author chenwanli 2015年4月10日
 */
public abstract class BaseController {

	private final static Log LOG = LogFactory.getLog(BaseController.class);

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> handleInvalidInput(Throwable ex) {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");
		return new ResponseEntity<String>(ex.getMessage(), responseHeaders, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(WeChatException.class)
	public ResponseEntity<String> handleWeChatException(Throwable ex) {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");
		LOG.error(ex.getMessage());
		return new ResponseEntity<String>("微信服务器异常！", responseHeaders, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({ RuntimeException.class })
	public ResponseEntity<String> handleRuntimeException(Throwable ex) {
		LOG.error(ex.getMessage(), ex);
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");

		return new ResponseEntity<String>(ex.getMessage(), responseHeaders,
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
