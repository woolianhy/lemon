package com.lemon.base.bean.qrcode;

public class QrcodeTicketScene {

	private Integer scene_id;
	
	private String scene_str;


	public Integer getScene_id() {
		return scene_id;
	}



	public void setScene_id(Integer scene_id) {
		this.scene_id = scene_id;
	}



	public String getScene_str() {
		return scene_str;
	}



	public void setScene_str(String scene_str) {
		this.scene_str = scene_str;
	}



	public void setScene_id(int scene_id) {
		this.scene_id = scene_id;
	}

	public QrcodeTicketScene(int scene_id) {
		super();
		this.scene_id = scene_id;
	}

	public QrcodeTicketScene(String scene_str) {
		super();
		this.scene_id=null;
		this.scene_str = scene_str;
	}

	public QrcodeTicketScene() {
		super();
	}

	
}
