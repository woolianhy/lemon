package com.lemon.base.bean;

public class WxTemplateResult extends WxGlobalResult {
       
	private String msgid;

	public String getMsgid() {
		return msgid;
	}

	public void setMsgid(String msgid) {
		this.msgid = msgid;
	}

	@Override
	public String toString() {
		return "WxTemplateResult ["+super.toString()+", msgid=" + msgid + "]";
	}

	
}
