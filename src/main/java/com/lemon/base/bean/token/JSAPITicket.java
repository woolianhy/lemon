package com.lemon.base.bean.token;

public class JSAPITicket {

	private String ticket;

	private Integer expires_in;

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public Integer getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(Integer expires_in) {
		this.expires_in = expires_in;
	}

	public JSAPITicket(String ticket, Integer expires_in) {
		super();
		this.ticket = ticket;
		this.expires_in = expires_in;
	}

	@Override
	public String toString() {
		return "JSAPITicket [ticket=" + ticket + ", expires_in=" + expires_in
				+ "]";
	}
	
	

}
