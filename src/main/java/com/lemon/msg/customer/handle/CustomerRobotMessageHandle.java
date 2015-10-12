package com.lemon.msg.customer.handle;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.lemon.base.Exception.WeChatException;
import com.lemon.base.bean.user.WxUserInfo;
import com.lemon.base.config.WxConfig;
import com.lemon.base.http.HttpTuringRobot;
import com.lemon.base.http.wx.manager.WxUserManager;
import com.lemon.msg.bean.BaseMsg;
import com.lemon.msg.bean.TextMsg;
import com.lemon.msg.handle.MessageHandle;
import com.lemon.msg.req.bean.BaseEvent;
import com.lemon.msg.req.bean.BaseReqMsg;
import com.lemon.msg.req.bean.EventType;
import com.lemon.msg.req.bean.LocationEvent;
import com.lemon.msg.req.bean.MenuEvent;
import com.lemon.msg.req.bean.QrCodeEvent;
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
public class CustomerRobotMessageHandle extends MessageHandle {

	@Autowired
	@Qualifier("customerWxConfig")
	private WxConfig wxConfig;

	@Autowired
	private WxUserManager WxUserManager;

	@Autowired
	private HttpTuringRobot httpTuringRobot;

	private final static Log LOG = LogFactory
			.getLog(CustomerRobotMessageHandle.class);

	@Override
	protected String getToken() {

		return wxConfig.getToken();
	}

	@Override
	protected BaseMsg handleDefaultMsg(BaseReqMsg msg) {
		return new TextMsg("默认回复！");
	}

	@Override
	protected BaseMsg handleTextMsg(TextReqMsg msg) {
		LOG.info(msg.toString());
		BaseMsg answer = null;
		try {
			answer = httpTuringRobot.answerMsg(msg.getContent(),
					msg.getFromUserName(), null);
		} catch (IOException e) {
			LOG.error(e);
		}
		return answer;
	}

	@Override
	protected BaseMsg handleLocationEvent(LocationEvent event) {
		LOG.info("用户id：" + event.getFromUserName() + ";纬度："
				+ event.getLatitude() + ";经度：" + event.getLongitude() + ";精确度："
				+ event.getPrecision());
		return null;
	}

	@Override
	protected BaseMsg handleSubscribe(BaseEvent event) {
		WxUserInfo userInfo = WxUserManager.getUserInfo(wxConfig,
				event.getFromUserName());
		LOG.info(userInfo);
		return new TextMsg("感谢您的关注！");
	}

	@Override
	protected BaseMsg handleTemplateFinishedEvent(TemplateFinishedEvent event) {
		LOG.info("推送给用户：" + event.getFromUserName() + "的模板消息："
				+ event.getMsgID() + "已完成，状态为：" + event.getStatus());
		return null;
	}

	@Override
	protected BaseMsg handleMenuViewEvent(MenuEvent event) {
		LOG.info("用户：" + event.getFromUserName() + "打开了网页："
				+ event.getEventKey());
		return null;
	}

	@Override
	protected BaseMsg handleDefaultEvent(BaseEvent event) {
		return new TextMsg("默认事件回复！");
	}

	@Override
	protected BaseMsg handleQrCodeEvent(QrCodeEvent event) {
		if (event.getEvent().equals(EventType.SUBSCRIBE)) {
			return new TextMsg("扫码关注并绑定师傅" + event.getEventKey() + "！event:"
					+ event.toString());
		} else if (event.getEvent().equals(EventType.SCAN)) {
			return new TextMsg("扫码并绑定师傅" + event.getEventKey() + "！event:"
					+ event.toString());
		}
		return null;
	}

}
