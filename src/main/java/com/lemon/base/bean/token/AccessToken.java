package com.lemon.base.bean.token;

public class AccessToken {

	private long expires_in;

	private String access_token;

	
	
	public AccessToken() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AccessToken(long expires_in, String access_token) {
		super();
		this.expires_in = expires_in;
		this.access_token = access_token;
	}


	public long getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(long expires_in) {
		this.expires_in = expires_in;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	

}
