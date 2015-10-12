package com.lemon.base.util;

public class WxConfigSignatureResult {

	private String appid;

	private String noncestr;

	private String jsapi_ticket;

	private Long timestamp;

	private String signature;

	public WxConfigSignatureResult(String appid, String noncestr,
			String jsapi_ticket, Long timestamp, String signature) {
		super();
		this.appid = appid;
		this.noncestr = noncestr;
		this.jsapi_ticket = jsapi_ticket;
		this.timestamp = timestamp;
		this.signature = signature;
	}

	public WxConfigSignatureResult() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getNoncestr() {
		return noncestr;
	}

	public void setNoncestr(String noncestr) {
		this.noncestr = noncestr;
	}

	public String getJsapi_ticket() {
		return jsapi_ticket;
	}

	public void setJsapi_ticket(String jsapi_ticket) {
		this.jsapi_ticket = jsapi_ticket;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

}
