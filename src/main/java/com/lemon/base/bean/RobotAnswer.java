package com.lemon.base.bean;

import java.util.List;

public class RobotAnswer {
    
	private int code;
	
	private String text;
	
	private String url;
	
	
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	private List<RobotAnswerItem> list;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<RobotAnswerItem> getList() {
		return list;
	}

	public void setList(List<RobotAnswerItem> list) {
		this.list = list;
	}
	
	
}
