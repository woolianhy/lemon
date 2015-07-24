package com.lemon.base.interceptor;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.lemon.base.annotation.OAuthRequired;
  
public class OAuth2Interceptor implements HandlerInterceptor {  
	
	@Value("#{lemonCommon.domain_name}")
	private String domain_name;
  
    /** 
     * 在DispatcherServlet完全处理完请求后被调用 
     * 当有拦截器抛出异常时,会从当前拦截器往回执行所有的拦截器的afterCompletion() 
     */  
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {  
        System.out.println("**执行顺序: 3、afterCompletion**");  
  
    }  
  
    /** 
     * 在业务处理器处理请求执行完成后,生成视图之前执行的动作 
     */  
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object arg2, ModelAndView modelAndView) throws Exception {  
        System.out.println("**执行顺序: 2、postHandle**");  
  
    }  
  
    /** 
     * 在业务处理器处理请求之前被调用 如果返回false 从当前的拦截器往回执行所有拦截器的afterCompletion(),再退出拦截器链 
     * 如果返回true 执行下一个拦截器,直到所有的拦截器都执行完毕 再执行被拦截的Controller 然后进入拦截器链, 
     * 从最后一个拦截器往回执行所有的postHandle() 接着再从最后一个拦截器往回执行所有的afterCompletion() 
     */  
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {  
        System.out.println("**执行顺序: 1、preHandle**");  
  
        HttpSession session = request.getSession();  
        // 先判断是否有注解  
  
        HandlerMethod handlerMethod = (HandlerMethod) handler;  
        Method method = handlerMethod.getMethod();  
        OAuthRequired annotation = method.getAnnotation(OAuthRequired.class);  
        if (annotation != null) {  
            System.out.println("OAuthRequired：你的访问需要获取登录信息！");  
            Object objUid = session.getAttribute("UserId");  
            if (objUid == null) {  
//                String resultUrl = request.getRequestURL().toString();  
                String resultUrl = domain_name+request.getRequestURI();
                String param=request.getQueryString();  
                if(param!=null){  
                    resultUrl+= "?" + param;  
                }  
                System.out.println("OAuth拦截请求：resultUrl="+resultUrl);  
                try {  
                    resultUrl = java.net.URLEncoder.encode(resultUrl, "utf-8");  
                } catch (UnsupportedEncodingException e) {  
                    e.printStackTrace();  
                }  
                //请求的路径  
                String contextPath=request.getContextPath();  
                response.sendRedirect(contextPath + "/oauth2.do?resultUrl=" + resultUrl);  
                return false;  
            }  
  
        }  
        return true;  
    }  
  
}  