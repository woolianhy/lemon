package com.lemon.customer.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.Cookie;
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
import com.lemon.base.Exception.WeChatException;
import com.lemon.base.bean.pay.OrderQueryResult;
import com.lemon.base.bean.pay.PayResult;
import com.lemon.base.bean.qrcode.Ticket;
import com.lemon.base.bean.user.WxUserInfo;
import com.lemon.base.config.WxConfig;
import com.lemon.base.controller.BaseController;
import com.lemon.base.http.wx.manager.WxPayManager;
import com.lemon.base.http.wx.manager.WxQrcodeManager;
import com.lemon.base.util.SHAUtil;
import com.lemon.base.util.TokenProxyImpl;
import com.lemon.base.util.WxConfigSignatureResult;

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
public class CustomerController extends BaseController {

	@Autowired
	private WxPayManager httpPayManager;

	@Autowired
	private WxQrcodeManager httpQrcodeManager;

	@Autowired
	@Qualifier("customerWxConfig")
	private WxConfig config;

	@Autowired
	private Gson gson;


	@Autowired
	private TokenProxyImpl customerTokenProxy;

	private final static Log LOG = LogFactory.getLog(CustomerController.class);

	/**
	 * 微信统一下单接口，返回前端需要字段，让前端js可以调起微信支付
	 * @param request
	 * @param session
	 * @param paymentId
	 * @param title
	 * @return
	 * @throws WeChatException
	 */
	@RequestMapping(value = { "/order/pay" }, method = RequestMethod.GET)
	@ResponseBody
	public PayResult orderPay(HttpServletRequest request, HttpSession session,
			@RequestParam String paymentId,
			@RequestParam(required = false) String title)
			throws WeChatException {
		String openid = (String) session.getAttribute("openid");
		if (openid != null) {
			Double price = 0.01D;
			if (price == null) {
				return new PayResult(true);
			}
			// Double price = 0.02D;
			PayResult result = httpPayManager.unifiedorder(config, openid,
					paymentId, title, price);
			LOG.info("PayResult:" + result);
			return result;
		}
		return null;

	}

	/**
	 * 获取微信签名需要信息
	 * @param request
	 * @param session
	 * @param url
	 * @return
	 */
	@RequestMapping(value = { "/createSign" }, method = RequestMethod.GET)
	@ResponseBody
	public WxConfigSignatureResult createSign(HttpServletRequest request,
			HttpSession session, @RequestParam String url) {
		String noncestr = UUID.randomUUID().toString().trim()
				.replaceAll("-", "").substring(0, 31);
		Long timestamp = new Date().getTime() / 1000L;
		String tikect = customerTokenProxy.getJsapiTicket().getTicket();
		StringBuilder sb = new StringBuilder();
		try {
			sb.append("jsapi_ticket=").append(tikect).append("&noncestr=")
					.append(noncestr).append("&timestamp=").append(timestamp)
					.append("&url=").append(URLDecoder.decode(url,"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
		System.out.println("params="+sb.toString());
		String signature = SHAUtil.SHA1(sb.toString());
		System.out.println("signature="+signature);
		return new WxConfigSignatureResult(config.getAppId(), noncestr, tikect,
				timestamp, signature);
	}

	/**
	 * 查询订单支付状况
	 * @param request
	 * @param session
	 * @param paymentId
	 * @return
	 */
	@RequestMapping(value = { "/order/query" }, method = RequestMethod.GET)
	@ResponseBody
	public OrderQueryResult orderQuery(HttpServletRequest request,
			HttpSession session, @RequestParam String paymentId) {
		return httpPayManager.orderquery(config, paymentId);
	}

	/**
	 * 获取jsapi 的tiket
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = { "/jsAPItiket" }, method = RequestMethod.GET)
	@ResponseBody
	public String jsAPItiket(HttpServletRequest request, HttpSession session) {
		return customerTokenProxy.getJsapiTicket().getTicket();
	}

	/**
	 * 获取openid
	 * @param request
	 * @param session
	 * @return
	 * @throws WeChatException
	 */
	@RequestMapping(value = { "getOpenid" }, method = RequestMethod.GET)
	@ResponseBody
	public String getOpenid(HttpServletRequest request, HttpSession session)
			throws WeChatException {
		return (String) session.getAttribute("openid");
	}

	/**
	 * 获取业务系统用户id
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = { "getCustomerId" }, method = RequestMethod.GET)
	@ResponseBody
	public String getCustomerId(HttpServletRequest request, HttpSession session) {
		String customerId = (String) session.getAttribute("customerId");
		if (customerId == null) {
			Cookie[] cookies = request.getCookies();
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("customerId")) {
					customerId = cookie.getValue();
				}
			}
		}
		return customerId;
	}

	/**
	 * 获取微信用户信息
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = { "getWxInfo" }, method = RequestMethod.GET)
	@ResponseBody
	public WxUserInfo getWxInfo(HttpServletRequest request, HttpSession session) {
		LOG.info((WxUserInfo) session.getAttribute("wxInfo"));
		return (WxUserInfo) session.getAttribute("wxInfo");
	}

	/**
	 * 获取带参数微信二维码
	 * @param request
	 * @param response
	 * @param session
	 * @param key
	 * @throws WeChatException
	 */
	@RequestMapping(value = { "/getCode" }, method = RequestMethod.GET)
	@ResponseBody
	public void getCode(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@RequestParam String key) throws WeChatException {
		Ticket qrcodeTiket = httpQrcodeManager.qrcodeLimitTiket(config, key,
				604800);
		httpQrcodeManager.showqrcode(qrcodeTiket.getTicket(), response);
	}

}
