package com.lemon.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lemon.base.Exception.WeChatException;
import com.lemon.base.bean.menu.Button;
import com.lemon.base.bean.menu.Menu;
import com.lemon.base.bean.pay.PayResult;
import com.lemon.base.bean.qrcode.Ticket;
import com.lemon.base.bean.template.TemplateData;
import com.lemon.base.bean.template.WxTemplate;
import com.lemon.base.bean.template.WxTemplateResult;
import com.lemon.base.bean.user.WxUserInfo;
import com.lemon.base.config.WxConfig;
import com.lemon.base.controller.BaseController;
import com.lemon.base.http.MyHttpClient;
import com.lemon.base.http.server.manager.CustomerServerManager;
import com.lemon.base.http.wx.manager.WxPayManager;
import com.lemon.base.http.wx.manager.WxQrcodeManager;
import com.lemon.base.http.wx.manager.WxTemplateManager;
import com.lemon.base.http.wx.manager.WxUserManager;
import com.lemon.base.util.TokenProxyImpl;

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
@RequestMapping("/customer")
public class CustomerTestController extends BaseController {

	@Autowired
	@Qualifier("customerTokenProxy")
	private TokenProxyImpl tokenProxy;

	@Autowired
	private MyHttpClient wxHttpClient;
	
	@Autowired
	private WxTemplateManager httpTemplateManager;
	
	@Autowired
	private WxUserManager httpUserManager;
	
	@Autowired
	private CustomerServerManager customerServerManager;
	
	@Autowired
	private WxPayManager httpPayManager;
	
	@Autowired
	private WxQrcodeManager httpQrcodeManager;
	
	@Autowired
	@Qualifier("customerWxConfig")
	private WxConfig config;

	@Autowired
	private Gson gson;
	
	private final static Log LOG = LogFactory.getLog(CustomerTestController.class);

	
	@RequestMapping(value = { "/test1" }, method = RequestMethod.GET)
	@ResponseBody
	public String test(HttpServletRequest request, HttpSession session) {
		LOG.info("token:" + tokenProxy.getAccessToken() + " openid:" + session.getAttribute("openid"));
		return "token:" + tokenProxy.getAccessToken() + " openid:" + session.getAttribute("openid");
	}

	/**
	 * 生成微信按钮
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = { "/test2" }, method = RequestMethod.GET)
	@ResponseBody
	public String test2(HttpServletRequest request, HttpSession session) {

		Menu m = new Menu();
		Button b = new Button();
		b.setName("家电服务");
		b.setType("view");
		b.setUrl("http://apollo.haoge365.com.cn/wzlemon/oauth2/html/wmall/v1.0/index.html?name=xj");
		Button b1 = new Button();
		b1.setName("商城");
		b1.setType("view");
		b1.setUrl("http://apollo.haoge365.com.cn/lemon/oauth2/html/wmall/v2.0/index.html?terminalId=300022");
//		Button b2 = new Button();
//		b2.setName("我的");
//		b2.setType("view");
//		b2.setUrl("http://apollo.haoge365.com.cn/wzlemon/oauth2/html/wmall/v1.0/mine.html");
		List<Button> list = new ArrayList<Button>();
		list.add(b);
		list.add(b1);
//		list.add(b2);
		m.setButton(list);
		gson= new GsonBuilder().disableHtmlEscaping().create();
		String json = gson.toJson(m);
		String result = null;
		try {
			result = wxHttpClient.postJson(
					"https://api.weixin.qq.com/cgi-bin/menu/create?access_token="
							+ tokenProxy.getAccessToken().getAccess_token(), json);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 发送模板消息
	 * @param request
	 * @param session
	 * @return
	 * @throws WeChatException
	 */
	@RequestMapping(value = { "/test3" }, method = RequestMethod.GET,produces = {"text/html;charset=UTF-8"})
	@ResponseBody
	public String test3(HttpServletRequest request,HttpSession session) throws WeChatException {
		
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
        WxTemplateResult sendTemplateMessage = httpTemplateManager.sendTemplateMessage(config,t);
        return sendTemplateMessage.toString();
	}
	
	/**
	 * 获取用户信息
	 * @param request
	 * @param session
	 * @return
	 * @throws WeChatException
	 */
	@RequestMapping(value = { "/test4"}, method = RequestMethod.GET,produces = {"text/html;charset=UTF-8"})
	@ResponseBody
	public String test4(HttpServletRequest request,HttpSession session) throws WeChatException {
        WxUserInfo userInfo = httpUserManager.getUserInfo(config,"oYWlss1fTkWmwX6y_b4UurbGZ420");
        String cid = customerServerManager.updateUserInfoBindTerminal(userInfo,"111111111111");
        System.out.println(cid);
        return userInfo.toString();
	}
	
	/**
	 * 微信下单
	 * @param request
	 * @param session
	 * @return
	 * @throws WeChatException
	 */
	@RequestMapping(value = { "/test5"}, method = RequestMethod.GET)
	@ResponseBody
	public PayResult test5(HttpServletRequest request,HttpSession session) throws WeChatException {
        PayResult unifiedorder = httpPayManager.unifiedorder(config,"oYWlss83x5r8M-cxmpt-rjhz5IL8", "1111111","test",1D);
        return unifiedorder;
	}
	
	/**
	 * 微信临时二维码
	 * @param request
	 * @param response
	 * @param session
	 * @param key
	 * @throws WeChatException
	 */
	@RequestMapping(value = { "/test6"}, method = RequestMethod.GET)
	@ResponseBody
	public void test6(HttpServletRequest request,HttpServletResponse response,HttpSession session,@RequestParam int key) throws WeChatException {
        Ticket qrcodeTiket = httpQrcodeManager.qrcodeTiket(config,key, 604800);
        LOG.info(qrcodeTiket.getTicket());
        httpQrcodeManager.showqrcode(qrcodeTiket.getTicket(), response);
	}
	
	/**
	 * 微信永久二维码
	 * @param request
	 * @param response
	 * @param session
	 * @param key
	 * @throws WeChatException
	 */
	@RequestMapping(value = { "/test7"}, method = RequestMethod.GET)
	@ResponseBody
	public void test7(HttpServletRequest request,HttpServletResponse response,HttpSession session,@RequestParam String key) throws WeChatException {
        Ticket qrcodeTiket = httpQrcodeManager.qrcodeLimitTiket(config,key, 604800);
        LOG.info(qrcodeTiket.getTicket());
        httpQrcodeManager.showqrcode(qrcodeTiket.getTicket(), response);
	}
	
	/**
	 * 获取公众号所有用户信息
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws WeChatException
	 */
	@RequestMapping(value = { "/test8"}, method = RequestMethod.GET)
	@ResponseBody
	public List<String> test8(HttpServletRequest request,HttpServletResponse response,HttpSession session) throws WeChatException {
        List<String> allSubscriber = httpUserManager.allSubscriber(config);
        return allSubscriber;
	}
}
