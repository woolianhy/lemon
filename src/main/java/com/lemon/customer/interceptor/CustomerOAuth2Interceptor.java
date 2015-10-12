package com.lemon.customer.interceptor;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.jstl.core.Config;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.lemon.base.config.WxConfig;
import com.lemon.base.util.URLUtils;

/**
 * 微信oauth2获取用户信息转发拦截器
 * @author Administrator
 *
 */
public class CustomerOAuth2Interceptor implements HandlerInterceptor {

	@Value("#{lemonCommon.domain_name}")
	private String domain_name;
	
	@Autowired
	@Qualifier("customerWxConfig")
	private WxConfig config;

	/**
	 * 
	 */
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

	/**
	 * 
	 */
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object arg2, ModelAndView modelAndView)
			throws Exception {

	}

	/**
	 * 拦截用户请求，获取用户微信账号信息
	 */
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		HttpSession session = request.getSession();
		String openid = (String) session.getAttribute("openid");
		if (openid == null || StringUtils.isEmpty(openid)) {
			// 获取redirect路径
			String resultUrl = request.getPathInfo();
			if (resultUrl == null) {
				resultUrl = request.getServletPath();
			} else {
				resultUrl = request.getServletPath() + resultUrl;
			}
			if ((resultUrl == null) || (resultUrl.equals(""))) {
				resultUrl = "/";
			}
			String param = request.getQueryString();

			if (param != null) {
				resultUrl += "?" + param;
			}
			System.out.println("OAuth拦截请求：resultUrl=" + resultUrl);
			try {
				resultUrl = URLEncoder.encode(resultUrl, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			response.sendRedirect(URLUtils.getWxOauth2RedirectStr(request, config.getAppId(),
					"/customerOauth2url.do", "oauth2url", resultUrl));
			return false;

		}
		return true;
	}

}