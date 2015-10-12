package com.lemon.base.config;

import com.lemon.base.util.TokenProxyImpl;

public class WxConfigImpl implements WxConfig {
	// 第三方用户唯一凭证
	private String appId;

	// 第三方用户唯一凭证密钥
	private String appsecret;

	private String mch_id;

	private String wxzf_key;

	private String notify_url;

	private String token;

	private TokenProxyImpl tokenProxy;

	private String subsribeReplyText;

	public WxConfigImpl(String appId, String appsecret, String mch_id,
			String wxzf_key, String notify_url, String token,
			 String subsribeReplyText) {
		super();
		this.appId = appId;
		this.appsecret = appsecret;
		this.mch_id = mch_id;
		this.wxzf_key = wxzf_key;
		this.notify_url = notify_url;
		this.token = token;
		this.subsribeReplyText = subsribeReplyText;
	}

	public String getAppId() {
		return appId;
	}

	public String getAppsecret() {
		return appsecret;
	}

	public String getMch_id() {
		return mch_id;
	}

	public String getAccessToken() {
		return tokenProxy.getAccessToken().getAccess_token();
	}

	public String getWxzf_key() {
		return wxzf_key;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public String getToken() {
		return token;
	}

	public void initAccessToken() {
		tokenProxy.initAccessToken();
	}

	public TokenProxyImpl getTokenProxy() {
		return tokenProxy;
	}

	public void setTokenProxy(TokenProxyImpl tokenProxy) {
		this.tokenProxy = tokenProxy;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public void setAppsecret(String appsecret) {
		this.appsecret = appsecret;
	}

	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}

	public void setWxzf_key(String wxzf_key) {
		this.wxzf_key = wxzf_key;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getSubsribeReplyText() {
		return subsribeReplyText;
	}

	public void setSubsribeReplyText(String subsribeReplyText) {
		this.subsribeReplyText = subsribeReplyText;
	}

}
