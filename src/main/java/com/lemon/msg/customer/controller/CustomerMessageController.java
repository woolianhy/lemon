package com.lemon.msg.customer.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lemon.base.controller.BaseController;
import com.lemon.base.util.TokenProxyImpl;
import com.lemon.msg.customer.handle.CustomerRobotMessageHandle;
import com.lemon.msg.handle.MessageHandle;

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
public class CustomerMessageController extends BaseController {

	// private static final Logger logger =
	// Logger.getLogger(MessageController.class);

	@Autowired
	@Qualifier("customerRobotMessageHandle")
	private MessageHandle messageHandle;
	
//	@Autowired
//	@Qualifier("customerRobotMessageHandle")
//	private CustomerRobotMessageHandle customerRobotMessageHandle;

	@RequestMapping(value = { "/api" }, method = RequestMethod.GET)
	@ResponseBody
	public String validateMessage(HttpServletRequest request) {
		return messageHandle.doGet(request);
	}

	@RequestMapping(value = { "/api" }, method = RequestMethod.POST)
	@ResponseBody
	public String replyMessage(HttpServletRequest request) {
		return messageHandle.doPost(request);
	}
	


}
