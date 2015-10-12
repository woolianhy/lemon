package com.lemon.msg.customer.handle;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.lemon.base.Exception.WeChatException;
import com.lemon.base.bean.user.WxUserInfo;
import com.lemon.base.config.WxConfig;
import com.lemon.base.http.HttpTuringRobot;
import com.lemon.base.http.server.manager.CustomerServerManager;
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

@Component
public class DefaultMessageHandle extends MessageHandle {

	@Autowired
	@Qualifier("customerWxConfig")
	private WxConfig wxConfig;

	@Autowired
	private WxUserManager httpUserManager;

	@Autowired
	private CustomerServerManager customerServerManager;

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
		return new TextMsg("");
	}

	@Override
	protected BaseMsg handleTextMsg(TextReqMsg msg) {
		 return new TextMsg("");
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
		 return new TextMsg(wxConfig.getSubsribeReplyText());
//		return new TextMsg("您好！欢迎关注温州“一线通”家电服务平台。");
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
		return new TextMsg("");
	}

	@Override
	protected BaseMsg handleQrCodeEvent(QrCodeEvent event) {
		String terminalId = event.getEventKey().replace("qrscene_", "");
		WxUserInfo wxInfo = httpUserManager.getUserInfo(wxConfig,
				event.getFromUserName());
		customerServerManager.updateUserInfoBindTerminal(wxInfo, terminalId);
		// if (event.getEvent().equals(EventType.SUBSCRIBE)) {
		return new TextMsg(wxConfig.getSubsribeReplyText());
		// } else if (event.getEvent().equals(EventType.SCAN)) {
		// return new TextMsg("欢迎关注小匠服务，中国领先的家电服务O2O服务提供商。");
		// }
		// return new TextMsg("您好！欢迎关注温州“一线通”家电服务平台。");
		// return null;
	}

}
