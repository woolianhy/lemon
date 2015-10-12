package com.lemon.base.bean.pay;

import java.util.SortedMap;
import java.util.TreeMap;

import com.lemon.base.util.SignUtil;

public class PayResult {

	private String appId;

	private Long timeStamp;

	private String nonceStr;

	private String packageStr;

	private String signType;

	private String paySign;

	private String ticket;

	private Boolean isOverTime=false;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public Long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getNonceStr() {
		return nonceStr;
	}

	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}

	public String getPackageStr() {
		return packageStr;
	}

	public void setPackageStr(String packageStr) {
		this.packageStr = packageStr;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getPaySign() {
		return paySign;
	}

	public void setPaySign(String paySign) {
		this.paySign = paySign;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public PayResult(String appId, Long timeStamp, String nonceStr,
			String packageStr, String signType, String key, String ticket) {
		super();
		this.appId = appId;
		this.timeStamp = timeStamp;
		this.nonceStr = nonceStr;
		this.packageStr = packageStr;
		this.signType = signType;
		SortedMap<Object, Object> map = new TreeMap<Object, Object>();
		map.put("appId", appId);
		map.put("timeStamp", timeStamp);
		map.put("nonceStr", nonceStr);
		map.put("package", packageStr);
		map.put("signType", signType);
		this.paySign = SignUtil.createSign("UTF-8", map, key);
		this.ticket = ticket;
	}

	public PayResult(Boolean isOverTime) {
		super();
		this.isOverTime = isOverTime;
	}

	public Boolean getIsOverTime() {
		return isOverTime;
	}

	public void setIsOverTime(Boolean isOverTime) {
		this.isOverTime = isOverTime;
	}

	public PayResult(String appId, Long timeStamp, String nonceStr,
			String packageStr, String signType, String paySign, String ticket,
			Boolean isOverTime) {
		super();
		this.appId = appId;
		this.timeStamp = timeStamp;
		this.nonceStr = nonceStr;
		this.packageStr = packageStr;
		this.signType = signType;
		this.paySign = paySign;
		this.ticket = ticket;
		this.isOverTime = isOverTime;
	}

	@Override
	public String toString() {
		return "PayResult [appId=" + appId + ", timeStamp=" + timeStamp
				+ ", nonceStr=" + nonceStr + ", packageStr=" + packageStr
				+ ", signType=" + signType + ", paySign=" + paySign
				+ ", ticket=" + ticket + ", isOverTime=" + isOverTime + "]";
	}

	
	
}
