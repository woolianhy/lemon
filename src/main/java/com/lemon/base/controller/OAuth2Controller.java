package com.lemon.base.controller;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;
import com.lemon.base.bean.Oauth2AccessToken;
import com.lemon.base.http.WxHttpSender;

/**
 * OAuth2 处理控制器
 * 
 * @author Sunlight
 * 
 */
@Controller
public class OAuth2Controller {

	private final static Log LOG = LogFactory.getLog(OAuth2Controller.class);
	
	// 第三方用户唯一凭证
	@Value("#{lemonCommon.appID}")
	private String appId;
	
	@Value("#{lemonCommon.oauth2_state}")
	private String oauth2_state;

	@Autowired
	private Gson gson;

	@Autowired
	private WxHttpSender wxHttpSender;

	/**
	 * 构造参数并将请求重定向到微信API获取登录信息
	 * 
	 * @param index
	 * @return
	 */
	@RequestMapping(value = { "/oauth2.do", "/oauth2" })
	public String Oauth2API(HttpServletRequest request, @RequestParam String resultUrl) {
		// 此处可以添加获取持久化的数据，如企业号id等相关信息
		String redirectUrl = "";
		if (resultUrl != null) {
			String reqUrl = request.getServerName();
			reqUrl=request.getServerPort()==80?reqUrl:reqUrl+":"+request.getServerPort();
			reqUrl+=request.getContextPath();
			String backUrl = "http://" + reqUrl + "/oauth2url.do?oauth2url=" + resultUrl;
			System.out.println("backUrl=" + backUrl);
			redirectUrl = oAuth2Url(backUrl);
		}
		LOG.info("redirect1:" + redirectUrl);
		return "redirect:" + redirectUrl;
	}

	
	@RequestMapping(value = { "/oauth2url.do" })
	public String Oauth2MeUrl(HttpServletRequest request,@RequestParam String code,
			@RequestParam String oauth2url) {
		LOG.info("code:" +code);
		Oauth2AccessToken oauth2AccessToken = wxHttpSender.getUserAccessToken(code);
		HttpSession session = request.getSession();
		if (oauth2AccessToken != null && oauth2AccessToken.getAccess_token() != null) {
			session.setAttribute("UserId", oauth2AccessToken.getOpenid());
			session.setAttribute("oauth2AccessToken", oauth2AccessToken);
			LOG.info("UserId:" + oauth2AccessToken.getOpenid());
			LOG.info("redirect2:" + oauth2url);
			return "redirect:" + oauth2url;
		}else{
			return null;
		}
		
	}

	private String oAuth2Url(String redirect_uri) {
		try {
			redirect_uri = java.net.URLEncoder.encode(redirect_uri, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String oauth2Url="https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appId+"&redirect_uri="+redirect_uri+"&response_type=code&scope=snsapi_base&state="+oauth2_state+"#wechat_redirect";
		System.out.println("oauth2Url=" + oauth2Url);
		return oauth2Url;
	}

}