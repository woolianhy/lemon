package com.lemon.msg.req.bean;

import com.lemon.msg.util.MessageBuilder;

/**
 * 上报地理位置事件
 */
public final class TemplateFinishedEvent extends BaseEvent {

	private String msgID;// 消息id
	private String status;// 状态

	

	public String getMsgID() {
		return msgID;
	}

	public void setMsgID(String msgID) {
		this.msgID = msgID;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	

	public TemplateFinishedEvent(String msgID, String status) {
		super();
		this.msgID = msgID;
		this.status = status;
	}

	@Override
	public String toXml() {
		MessageBuilder mb = new MessageBuilder(super.toXml());
		mb.addTag("MsgID", String.valueOf(msgID));
		mb.addTag("Status", String.valueOf(status));
		mb.surroundWith("xml");
		return mb.toString();
	}

	@Override
	public String toString() {
		return "TemplateFinishedEvent [msgID=" + msgID + ", status=" + status + "]";
	}

	

}
