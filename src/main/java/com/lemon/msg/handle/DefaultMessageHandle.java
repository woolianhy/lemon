package com.lemon.msg.handle;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.lemon.msg.bean.BaseMsg;
import com.lemon.msg.bean.TextMsg;
import com.lemon.msg.req.bean.BaseEvent;
import com.lemon.msg.req.bean.BaseReqMsg;

@Component
public class DefaultMessageHandle extends MessageHandle{
	
	@Value("#{lemonCommon.token}") 
	private String token;

	@Override
	protected String getToken() {
		
		return token;
	}

	
	
	@Override
	protected BaseMsg handleDefaultMsg(BaseReqMsg msg) {
		return new TextMsg("默认回复！");
	}

	


	@Override
	protected BaseMsg handleDefaultEvent(BaseEvent event) {
		return new TextMsg("默认事件回复！");
	}



	public void setToken(String token) {
		this.token = token;
	}
	
}
