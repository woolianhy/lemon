package com.lemon.msg.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lemon.base.controller.BaseController;
import com.lemon.base.util.TokenProxy;
import com.lemon.msg.handle.MessageHandle;
import com.lemon.msg.handle.RobotMessageHandle;

/**
 * 
 * <p>
 * 微信消息自动回复
 * </p>
 * 
 * 
 *
 * @author Chenwanli 2015年7月16日
 */
@Controller
@RequestMapping("/message")
public class MessageController extends BaseController {

	// private static final Logger logger =
	// Logger.getLogger(MessageController.class);

	@Autowired
	@Qualifier("defaultMessageHandle")
	private MessageHandle messageHandle;
	
	@Autowired
	private TokenProxy tokenProxy;
	
	@Autowired
	@Qualifier("robotMessageHandle")
	private RobotMessageHandle robotMessageHandle;

	@RequestMapping(value = { "/api" }, method = RequestMethod.GET)
	@ResponseBody
	public String validateMessage(HttpServletRequest request) {
		return robotMessageHandle.doGet(request);
	}

	@RequestMapping(value = { "/api" }, method = RequestMethod.POST)
	@ResponseBody
	public String replyMessage(HttpServletRequest request) {
		return robotMessageHandle.doPost(request);
	}
	
	@RequestMapping(value = { "/test" }, method = RequestMethod.GET)
	@ResponseBody
	public String test(HttpServletRequest request) {
		return tokenProxy.getAccessToken().getAccess_token();
	}

}
