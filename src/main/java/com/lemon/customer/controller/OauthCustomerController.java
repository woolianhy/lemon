package com.lemon.customer.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;
import com.lemon.base.Exception.WeChatException;
import com.lemon.base.bean.oauth2.Oauth2AccessToken;
import com.lemon.base.bean.user.WxUserInfo;
import com.lemon.base.config.WxConfig;
import com.lemon.base.controller.BaseController;
import com.lemon.base.http.server.manager.CustomerServerManager;
import com.lemon.base.http.wx.manager.WxUserManager;
import com.lemon.base.util.URLUtils;

@Controller
public class OauthCustomerController extends BaseController {

	private final static Log LOG = LogFactory
			.getLog(OauthCustomerController.class);

	@Value("#{lemonCommon.oauth2_state}")
	private String oauth2_state;

	@Value("#{lemonCommon.domain_name}")
	private String domain_name;

	@Autowired
	private Gson gson;

	@Autowired
	private WxUserManager httpUserManager;

	@Autowired
	private CustomerServerManager customerServerManager;

	@Autowired
	@Qualifier("customerWxConfig")
	private WxConfig config;


	@RequestMapping(value = { "/oauth2/**" }, method = RequestMethod.GET)
	public String oauth2(HttpServletRequest request, HttpSession session) {
		String openid = (String) session.getAttribute("openid");
		String resultUrl = request.getServletPath().replace("/oauth2", "")
				+ (StringUtils.isNotEmpty(request.getQueryString()) ? "?"
						+ request.getQueryString() : "");
		try {
			resultUrl = URLEncoder.encode(resultUrl, "utf-8");
		} catch (UnsupportedEncodingException e) {
			LOG.error(e);
		}
		if (openid == null || StringUtils.isEmpty(openid)) {
			String redirectUrl = "";
			if (resultUrl != null) {
				redirectUrl = URLUtils.getWxOauth2RedirectStr(request,
						config.getAppId(), "/customerOauth2url.do",
						"oauth2url", resultUrl);
			}
			LOG.info("redirect1:" + redirectUrl);

			return "redirect:" + redirectUrl;
		}

		System.out.println(request.getServletPath().replace("/oauth2", "")+ (StringUtils.isNotEmpty(request.getQueryString()) ? "?"
				+ request.getQueryString() : ""));
		return "redirect:" + request.getServletPath().replace("/oauth2", "")+ (StringUtils.isNotEmpty(request.getQueryString()) ? "?"
				+ request.getQueryString() : "");
	}

	@RequestMapping(value = { "/customerOauth2url.do" })
	public String Oauth2MeUrl(HttpServletRequest request,
			HttpServletResponse response, @RequestParam String code,
			@RequestParam String oauth2url) throws WeChatException {
		LOG.info("redirect2:" + oauth2url);
		HttpSession session = request.getSession();
		LOG.info("code:" + code);
		System.out.println("getQueryString:" + request.getQueryString());
		Oauth2AccessToken oauth2AccessToken = httpUserManager
				.getUserAccessToken(config, code);

		if (oauth2AccessToken != null
				&& oauth2AccessToken.getAccess_token() != null) {
			session.setAttribute("openid", oauth2AccessToken.getOpenid());
			response.addCookie(new Cookie("openid", oauth2AccessToken
					.getOpenid()));
			WxUserInfo wxInfo = httpUserManager.getUserInfo(config,
					oauth2AccessToken.getOpenid());
			if (wxInfo != null) {
				LOG.info(wxInfo);
				session.setAttribute("wxInfo", wxInfo);
				response.addCookie(new Cookie("wxInfo", gson.toJson(wxInfo)));
			}
			String customerId = customerServerManager.updateUserInfo(wxInfo);

			// String customerId ="1";
			if (customerId != null && !StringUtils.isEmpty(customerId)) {
				LOG.info("add customerId:" + customerId);
				response.addCookie(new Cookie("customerId", customerId));
				session.setAttribute("customerId", customerId);
			} else {
				LOG.error("customerId is null ,wxInfo:" + gson.toJson(wxInfo));
			}

			return "redirect:" + oauth2url;
		} else {
			return null;
		}

	}

}
