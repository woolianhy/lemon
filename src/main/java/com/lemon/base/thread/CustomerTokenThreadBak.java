package com.lemon.base.thread;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.lemon.base.Exception.WeChatException;
import com.lemon.base.bean.token.AccessToken;
import com.lemon.base.bean.token.JSAPITicket;
import com.lemon.base.config.WxConfig;
import com.lemon.base.http.wx.manager.WxTokenManager;

public class CustomerTokenThreadBak implements Runnable {

	private final static Log LOG = LogFactory.getLog(CustomerTokenThreadBak.class);

	@Autowired
	private WxTokenManager httpTokenManager;

	@Autowired
	@Qualifier("customerWxConfig")
	private WxConfig wxConfig;

	private AccessToken accessToken;

	private JSAPITicket jsapiTicket;

	public void run() {
		while (true) {
			try {

				try {
					accessToken = httpTokenManager.getAccessToken(wxConfig);
					if (null != accessToken) {
						jsapiTicket = httpTokenManager.getJSAPITicket(wxConfig,
								accessToken.getAccess_token());
					}

				} catch (WeChatException e) {
					e.printStackTrace();
					LOG.error("获取AccessToken失败！", e);
				}
				if (null != accessToken && jsapiTicket != null) {
					System.out.println("获取customer access_token成功，有效时长{"
							+ accessToken.getExpires_in() + "}秒 token:{"
							+ accessToken.getAccess_token() + "}");
					LOG.info("获取access_token成功，有效时长{"
							+ accessToken.getExpires_in() + "}秒 token:{"
							+ accessToken.getAccess_token() + "}");

					// 休眠7000秒
					Thread.sleep(getSleepTime());
				} else {
					// 如果access_token为null，10秒后再获取
					Thread.sleep(10 * 1000);
				}
			} catch (InterruptedException e) {
				try {
					Thread.sleep(10 * 1000);
				} catch (InterruptedException e1) {
					LOG.error("{}", e1);
				}
				LOG.error("{}", e);
			}
		}
	}

	private long getSleepTime() {
		return (jsapiTicket.getExpires_in() - accessToken.getExpires_in() > 0 ? jsapiTicket
				.getExpires_in() : accessToken.getExpires_in() - 200) * 1000;
	}

	public AccessToken getAccessToken() {
		if (accessToken != null) {
			return new AccessToken(accessToken.getExpires_in(),
					accessToken.getAccess_token());
		} else {
			return null;
		}
	}

	public void initAccessToken() {
		try {
			accessToken = httpTokenManager.getAccessToken(wxConfig);
		} catch (WeChatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public JSAPITicket getJsapiTicket() {
		if (accessToken != null) {
			return new JSAPITicket(jsapiTicket.getTicket(),
					jsapiTicket.getExpires_in());
		} else {
			return null;
		}
	}

}
