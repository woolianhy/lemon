package com.lemon.base.util;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.lemon.base.bean.AccessToken;
import com.lemon.base.bean.Oauth2AccessToken;

/**
 * 
 * <p>
 * 微信http接口发送者
 * </p>
 * 
 * 
 *
 * @author Chenwanli 2015年7月20日
 */
@Component
public class WxHttpSender {
	// 第三方用户唯一凭证
		@Value("#{lemonCommon.appID}")
		private String appId;

		// 第三方用户唯一凭证密钥
		@Value("#{lemonCommon.appsecret}")
		private String appsecret;

		@Value("#{lemonCommon.access_token_url}")
		private String access_token_url;

		@Value("#{lemonCommon.oauth2_access_token_url}")
		private String oauth2_access_token_url;
		
		@Value("#{lemonCommon.oauth2_state}")
		private String oauth2_state;

		@Autowired
		private MyHttpClient wxHttpClient;
		
		@Autowired
		private Gson gson;
		
		private final static Log LOG = LogFactory.getLog(WxHttpSender.class);
		
		public AccessToken getAccessToken(){
			String requestUrl = access_token_url.replace("APPID", appId).replace("APPSECRET",
					appsecret);
			String json = null;
			try {

				json = wxHttpClient.get(requestUrl);
				return  gson.fromJson(json, AccessToken.class);
			} catch (JsonSyntaxException e) {
				LOG.error("解析获得的AccessToken失败，json：" + json, e);
			} catch (IOException e) {
				LOG.error("获取json请求失败", e);
			}
			return null;
		}
		
		public Oauth2AccessToken getUserAccessToken(String code) {
			String url=oauth2_access_token_url.replace("APPID", appId).replace("SECRET", appsecret).replace("CODE", code);
	    	String json=null;
	    	Oauth2AccessToken oauth2AccessToken=null;
			try {
				json = wxHttpClient.get(url);
				oauth2AccessToken = gson.fromJson(json, Oauth2AccessToken.class);
				LOG.info(url+"\n"+json);
			} catch (IOException e) {
				LOG.error("请求oauth2_token失败", e);
			}
	    	return oauth2AccessToken;
		}
		
		
}
