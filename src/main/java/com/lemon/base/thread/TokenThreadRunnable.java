package com.lemon.base.thread;

import java.util.Date;

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

public class TokenThreadRunnable implements Runnable {

	private final static Log LOG = LogFactory.getLog(TokenThreadRunnable.class);

	@Autowired
	private WxTokenManager httpTokenManager;

	private WxConfig wxConfig;

	private AccessToken accessToken;

	private JSAPITicket jsapiTicket;

	private long refreshTime = 0;
	

	public void run() {
		while (true) {
			try {
				
				//如果四分钟内刷新过，休息四分钟
				if (!getAllowRefresh()) {
					Thread.sleep(4 * 60 * 1000);
				}

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
					// 更新刷新时间
					initRefreshTime();
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

	/**
	 * 根据token有效时间，获取线程休眠时间
	 * @return
	 */
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

	/**
	 * token异常时，刷新token
	 */
	public synchronized void initAccessToken() {
		try {
			// 获取是否允许刷新
			if (!getAllowRefresh()) {
				return;
			}
			accessToken = httpTokenManager.getAccessToken(wxConfig);
			if (accessToken != null) {
				// 更新刷新时间
				initRefreshTime();
			}
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

	/**
	 * 重置刷新时间
	 */
	public void initRefreshTime() {
		refreshTime = new Date().getTime();
	}

	/**
	 * 获取是否允许刷新token（限制至少刷新四分钟之后方可刷新）
	 * 
	 * @return
	 */
	public boolean getAllowRefresh() {
		return new Date().getTime() - refreshTime > 4 * 60 * 1000;
	}


	public void setWxConfig(WxConfig wxConfig) {
		this.wxConfig = wxConfig;
	}
	
	

}
