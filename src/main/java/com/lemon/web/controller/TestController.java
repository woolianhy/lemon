package com.lemon.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.lemon.base.annotation.OAuthRequired;
import com.lemon.base.bean.Button;
import com.lemon.base.bean.Menu;
import com.lemon.base.controller.BaseController;
import com.lemon.base.util.TokenProxy;
import com.lemon.base.util.MyHttpClient;

/**
 * 
 * <p>
 * </p>
 * 
 * 
 *
 * @author Chenwanli 2015年7月16日
 */
@Controller
@RequestMapping("/web")
public class TestController extends BaseController {

	
	@Autowired
	private TokenProxy tokenProxy;
	
	@Autowired
	private MyHttpClient wxHttpClient;
	
	@Autowired
	private Gson gson;
	
	@OAuthRequired
	@RequestMapping(value = { "/test1.html" }, method = RequestMethod.GET)
	@ResponseBody
	public String test(HttpServletRequest request,HttpSession session) {
		return "token:"+tokenProxy.getAccessToken()+" openid:"+session.getAttribute("UserId");
	}
	
	@RequestMapping(value = { "/test2" }, method = RequestMethod.GET)
	@ResponseBody
	public String test2(HttpServletRequest request,HttpSession session) {
		
		Menu m=new Menu();
		Button b=new Button();
		b.setName("test");
		b.setType("view");
		b.setUrl("http://120.25.127.129/lemon/web/test1.html");
		List<Button> list=new ArrayList<Button>();
		list.add(b);
		m.setButton(list);
		String json = gson.toJson(m);
		String result=null;
		try {
			result = wxHttpClient.post("https://api.weixin.qq.com/cgi-bin/menu/create?access_token="+tokenProxy.getAccessToken().getAccess_token(),json);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

}
