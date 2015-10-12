package com.lemon.base.bean.qrcode;

public class QrcodeTicketRequest {

	public static final String ACTION_NAME_QR_SCENE="QR_SCENE";
	
	public static final String ACTION_NAME_QR_LIMIT_SCENE="QR_LIMIT_SCENE";
	
	public static final String ACTION_NAME_QR_LIMIT_STR_SCENE="QR_LIMIT_STR_SCENE";
	
	private int expire_seconds;

	private String action_name;

	private QrcodeTicketActionInfo action_info;

	

	public int getExpire_seconds() {
		return expire_seconds;
	}

	public void setExpire_seconds(int expire_seconds) {
		this.expire_seconds = expire_seconds;
	}

	public String getAction_name() {
		return action_name;
	}

	public void setAction_name(String action_name) {
		this.action_name = action_name;
	}

	public QrcodeTicketActionInfo getAction_info() {
		return action_info;
	}

	public void setAction_info(QrcodeTicketActionInfo action_info) {
		this.action_info = action_info;
	}

}
