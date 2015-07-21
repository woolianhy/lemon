package com.lemon.msg.handle;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.lemon.base.http.HttpTuringRobot;
import com.lemon.msg.bean.BaseMsg;
import com.lemon.msg.bean.TextMsg;
import com.lemon.msg.req.bean.BaseEvent;
import com.lemon.msg.req.bean.BaseReqMsg;
import com.lemon.msg.req.bean.LocationEvent;
import com.lemon.msg.req.bean.MenuEvent;
import com.lemon.msg.req.bean.TemplateFinishedEvent;
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
	
	private final static Log LOG = LogFactory.getLog(RobotMessageHandle.class);

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
	protected BaseMsg handleLocationEvent(LocationEvent event) {
		LOG.info("用户id："+event.getFromUserName()+";纬度："+event.getLatitude()+";经度："+event.getLongitude()+";精确度："+event.getPrecision());
		return null;
	}



	@Override
	protected BaseMsg handleSubscribe(BaseEvent event) {
		return new TextMsg("感谢您的关注！");
	}



	@Override
	protected BaseMsg handleTemplateFinishedEvent(TemplateFinishedEvent event) {
		LOG.info("推送给用户："+event.getFromUserName()+"的模板消息："+event.getMsgID()+"已完成，状态为："+event.getStatus());
		return null;
	}



	@Override
	protected BaseMsg handleMenuViewEvent(MenuEvent event) {
		LOG.info("用户："+event.getFromUserName()+"打开了网页："+event.getEventKey());
		return null;
	}



	@Override
	protected BaseMsg handleDefaultEvent(BaseEvent event) {
		return new TextMsg("默认事件回复！");
	}



	public void setToken(String token) {
		this.token = token;
	}
	
}
