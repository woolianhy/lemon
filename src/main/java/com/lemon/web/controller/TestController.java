package com.lemon.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.lemon.base.annotation.OAuthRequired;
import com.lemon.base.bean.Button;
import com.lemon.base.bean.Menu;
import com.lemon.base.bean.TemplateData;
import com.lemon.base.bean.UnifiedOrderResult;
import com.lemon.base.bean.WxTemplate;
import com.lemon.base.bean.WxTemplateResult;
import com.lemon.base.bean.WxUserInfo;
import com.lemon.base.controller.BaseController;
import com.lemon.base.http.MyHttpClient;
import com.lemon.base.http.WxHttpSender;
import com.lemon.base.util.TokenProxy;

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
	private WxHttpSender wxHttpSender;

	@Autowired
	private Gson gson;
	
	private final static Log LOG = LogFactory.getLog(TestController.class);

	@OAuthRequired
	@RequestMapping(value = { "/test1.html" }, method = RequestMethod.GET)
	@ResponseBody
	public String test(HttpServletRequest request, HttpSession session) {
		LOG.info("token:" + tokenProxy.getAccessToken() + " openid:" + session.getAttribute("UserId"));
		return "token:" + tokenProxy.getAccessToken() + " openid:" + session.getAttribute("UserId");
	}

	@RequestMapping(value = { "/test2" }, method = RequestMethod.GET)
	@ResponseBody
	public String test2(HttpServletRequest request, HttpSession session) {

		Menu m = new Menu();
		Button b = new Button();
		b.setName("test");
		b.setType("view");
		b.setUrl("http://www.haoge365.com.cn/wxtest/web/test1.html");
		List<Button> list = new ArrayList<Button>();
		list.add(b);
		m.setButton(list);
		String json = gson.toJson(m);
		String result = null;
		try {
			result = wxHttpClient.post(
					"https://api.weixin.qq.com/cgi-bin/menu/create?access_token="
							+ tokenProxy.getAccessToken().getAccess_token(), json);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	@RequestMapping(value = { "/test3" }, method = RequestMethod.GET)
	@ResponseBody
	public String test3(HttpServletRequest request,HttpSession session) {
		
		WxTemplate t = new WxTemplate();  
        t.setUrl("");  
        t.setTouser("oYWlss83x5r8M-cxmpt-rjhz5IL8");  
        t.setTopcolor("#000000");  
        t.setTemplate_id("K9WP4odVocHWv5GhvCnnR2TgiBO_zNg7XZJm6m7vSeQ");  
        Map<String,TemplateData> m = new HashMap<String,TemplateData>();  
        TemplateData first = new TemplateData();  
        first.setColor("#000000");  
        first.setValue("***标题***");  
        m.put("first", first);  
        TemplateData product = new TemplateData();  
        product.setColor("#000000");  
        product.setValue("***产品***");  
        m.put("product", product);  
        TemplateData price = new TemplateData();  
        price.setColor("blue");  
        price.setValue("***1.00***");  
        m.put("price", price); 
        TemplateData time = new TemplateData();  
        time.setColor("#000000");  
        time.setValue(new Date().toString());  
        m.put("time", time); 
        t.setData(m); 
        WxTemplateResult sendTemplateMessage = wxHttpSender.sendTemplateMessage(t);
        return sendTemplateMessage.toString();
	}
	
	@RequestMapping(value = { "/test4"}, method = RequestMethod.GET)
	@ResponseBody
	public String test4(HttpServletRequest request,HttpSession session) {
        WxUserInfo userInfo = wxHttpSender.getUserInfo("oYWlss83x5r8M-cxmpt-rjhz5IL8");
        return userInfo.toString();
	}
	
	@RequestMapping(value = { "/test5"}, method = RequestMethod.GET)
	@ResponseBody
	public UnifiedOrderResult test5(HttpServletRequest request,HttpSession session) {
        UnifiedOrderResult unifiedorder = wxHttpSender.unifiedorder("oYWlss83x5r8M-cxmpt-rjhz5IL8", "1111111", 1);
        return unifiedorder;
	}
}
