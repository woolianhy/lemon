package com.lemon.base.http.wx.manager;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.google.gson.JsonSyntaxException;
import com.lemon.base.Exception.WeChatException;
import com.lemon.base.bean.token.AccessToken;
import com.lemon.base.bean.token.JSAPITicket;
import com.lemon.base.config.WxConfig;

@Component
public class WxTokenManager extends BaseWxManager{

	private final static Log LOG = LogFactory.getLog(WxTokenManager.class);
	
	private final String ACCESS_TOKEN_URL="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

	private final String JSAPI_TICKET_URL="https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
	
	public AccessToken getAccessToken(WxConfig config) throws WeChatException {
		System.out.println("config:"+config.getAppId()+"|"+config.getAppsecret());
		String requestUrl = ACCESS_TOKEN_URL.replace("APPID", config.getAppId()).replace(
				"APPSECRET", config.getAppsecret());
		String json = null;
		try {

			json = getWxHttpClient().get(requestUrl);
			return safeResult(json, AccessToken.class,config);
		} catch (JsonSyntaxException e) {
			LOG.error("解析获得的AccessToken失败，json：" + json, e);
		} catch (IOException e) {
			LOG.error("获取json请求失败", e);
		}
		return null;
	}
	
	public JSAPITicket getJSAPITicket(WxConfig config,String accessToken) throws WeChatException {
		String requestUrl = JSAPI_TICKET_URL.replace("ACCESS_TOKEN", accessToken);
		String json = null;
		try {

			json = getWxHttpClient().get(requestUrl);
			return safeResult(json, JSAPITicket.class,config);
		} catch (JsonSyntaxException e) {
			LOG.error("解析获得的JSAPITicket失败，json：" + json, e);
		} catch (IOException e) {
			LOG.error("获取json请求失败", e);
		}
		return null;
	}
}
