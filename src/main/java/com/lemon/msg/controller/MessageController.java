package com.lemon.msg.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lemon.base.controller.BaseController;
import com.lemon.msg.handle.MessageHandle;

@Controller
@RequestMapping("/message")
public class MessageController extends BaseController {

	// private static final Logger logger =
	// Logger.getLogger(MessageController.class);

	@Autowired
	@Qualifier("defaultMessageHandle")
	private MessageHandle messageHandle;

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
