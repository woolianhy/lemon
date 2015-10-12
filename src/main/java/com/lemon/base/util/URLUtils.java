package com.lemon.base.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

public class URLUtils {
	
	public static final String DOMAIN_NAME="apollo.haoge365.com.cn";
	
	public static final String OAUTH2_STATE="123";

	/**
	 * 获取微信oauth2跳转链接（通过redirect到这个返回的链接，微信会调用链接中的redirect_uri，程序监听redirect_uri,并在其中完成认证）
	 * @param request
	 * @param appId  
	 * @param oauthUrl  微信回调链接
	 * @param oauthParamName redirect_uri监听方法中接收来源链接的参数名
	 * @param resultUrl 来源链接
	 * @return
	 */
	public static String getWxOauth2RedirectStr(HttpServletRequest request,String appId,String oauthUrl,String oauthParamName,String resultUrl){
		String reqUrl = DOMAIN_NAME;
		reqUrl += request.getContextPath();
		String backUrl = "http://" + reqUrl
				+ oauthUrl+"?"+oauthParamName+"=" + resultUrl;
		System.out.println("backUrl=" + backUrl);
		return oAuth2Url(backUrl,appId);
	}
	
	private static String oAuth2Url(String redirect_uri,String appId) {
		try {
			redirect_uri = URLEncoder.encode(redirect_uri, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String oauth2Url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="
				+ appId
				+ "&redirect_uri="
				+ redirect_uri
				+ "&response_type=code&scope=snsapi_userinfo&state="
				+ OAUTH2_STATE + "#wechat_redirect";
		System.out.println("oauth2Url=" + oauth2Url);
		return oauth2Url;
	}
}
