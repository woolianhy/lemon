package com.lemon.msg.handle;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.lemon.base.util.HttpTuringRobot;
import com.lemon.msg.bean.BaseMsg;
import com.lemon.msg.bean.TextMsg;
import com.lemon.msg.req.bean.BaseEvent;
import com.lemon.msg.req.bean.BaseReqMsg;
import com.lemon.msg.req.bean.TextReqMsg;

/**
 * 
 * <p>
 * 图灵机器人消息回复消息工具类
 * </p>
 * 
 * 
 *
 * @author Chenwanli 2015年7月16日
 */
@Component
public class RobotMessageHandle extends MessageHandle{
	
	@Value("#{lemonCommon.token}") 
	private String token;
	
	@Autowired
	private HttpTuringRobot httpTuringRobot;

	@Override
	protected String getToken() {
		
		return token;
	}

	
	
	@Override
	protected BaseMsg handleDefaultMsg(BaseReqMsg msg) {
		return new TextMsg("默认回复！");
	}

	
	@Override
	protected BaseMsg handleTextMsg(TextReqMsg msg) {
		BaseMsg answer=null;
		try {
			 answer= httpTuringRobot.answerMsg(msg.getContent(), msg.getFromUserName(), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return answer;
	}

	@Override
	protected BaseMsg handleDefaultEvent(BaseEvent event) {
		return new TextMsg("默认事件回复！");
	}



	public void setToken(String token) {
		this.token = token;
	}
	
}
