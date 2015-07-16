package com.lemon.base.thread;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.lemon.base.bean.AccessToken;
import com.lemon.base.util.WxHttpClient;

@Component
public class TokenThread implements Runnable {

	private final static Log LOG = LogFactory.getLog(TokenThread.class);

	// 第三方用户唯一凭证
	@Value("#{lemonCommon.appID}")
	private String appId;

	// 第三方用户唯一凭证密钥
	@Value("#{lemonCommon.appsecret}")
	private String appsecret;

	@Value("#{lemonCommon.access_token_url}")
	private String access_token_url;

	@Autowired
	private WxHttpClient wxHttpClient;

	@Autowired
	private Gson gson;

	private AccessToken accessToken;

	public void run() {
		while (true) {
			try {
				String requestUrl = access_token_url.replace("APPID", appId).replace("APPSECRET",
						appsecret);
				String json = null;
				try {

					json = wxHttpClient.get(requestUrl);
					accessToken = gson.fromJson(json, AccessToken.class);
				} catch (JsonSyntaxException e) {
					LOG.error("解析获得的AccessToken失败，json：" + json, e);
				} catch (IOException e) {
					LOG.error("获取json请求失败", e);
				}

				if (null != accessToken) {
					System.out.println("获取access_token成功，有效时长{" + accessToken.getExpires_in()
							+ "}秒 token:{" + accessToken.getAccess_token() + "}");
					LOG.info("获取access_token成功，有效时长{" + accessToken.getExpires_in() + "}秒 token:{"
							+ accessToken.getAccess_token() + "}");
					// 休眠7000秒
					Thread.sleep((accessToken.getExpires_in() - 200) * 1000);
				} else {
					// 如果access_token为null，60秒后再获取
					Thread.sleep(10 * 1000);
				}
			} catch (InterruptedException e) {
				try {
					Thread.sleep(60 * 1000);
				} catch (InterruptedException e1) {
					LOG.error("{}", e1);
				}
				LOG.error("{}", e);
			}
		}
	}

	public AccessToken getAccessToken() {
		if (accessToken != null) {
			return new AccessToken(accessToken.getExpires_in(), accessToken.getAccess_token());
		} else {
			return null;
		}
	}

}
