package com.lemon.base.interceptor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 
 * <p>
 * js跨域拦截器
 * </p>
 * 
 * 
 *
 * @author Chenwanli 2015年7月16日
 */
public class AccessControlAllowOriginInterceptor extends HandlerInterceptorAdapter {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object handler) throws IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		return true;
	}

}
