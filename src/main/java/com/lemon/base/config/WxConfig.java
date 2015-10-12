package com.lemon.base.config;


public interface WxConfig {
	
	public String getAppId();

	public String getAppsecret();

	public String getMch_id();
	
	public String getToken();
	
	public String getAccessToken();

	public String getWxzf_key();
	
	public String getNotify_url();
	
	public void initAccessToken();
	
	public String getSubsribeReplyText();

}
