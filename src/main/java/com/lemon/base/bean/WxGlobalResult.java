package com.lemon.base.bean;

public class WxGlobalResult {

	private Integer errcode;
	
	private String errmsg;

	public Integer getErrcode() {
		return errcode;
	}

	public void setErrcode(Integer errcode) {
		this.errcode = errcode;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	@Override
	public String toString() {
		return "WxGlobalResult [errcode=" + errcode + ", errmsg=" + errmsg + "]";
	}
}
