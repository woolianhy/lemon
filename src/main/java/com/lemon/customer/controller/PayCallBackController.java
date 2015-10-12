package com.lemon.customer.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.lemon.base.Exception.WeChatException;
import com.lemon.base.bean.pay.PayCallBack;
import com.lemon.base.controller.BaseController;

@Controller
public class PayCallBackController extends BaseController{

	
	@RequestMapping(value = { "/pay/back"})
	public void payCallBack(HttpServletRequest request,HttpSession session,@ModelAttribute PayCallBack back) throws WeChatException {
		System.out.println(back);
	}
}
