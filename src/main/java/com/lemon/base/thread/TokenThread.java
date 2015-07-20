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
import com.lemon.base.util.MyHttpClient;
import com.lemon.base.util.WxHttpSender;

@Component
public class TokenThread implements Runnable {

	private final static Log LOG = LogFactory.getLog(TokenThread.class);

	@Autowired
    private WxHttpSender wxHttpSender;

	private AccessToken accessToken;

	public void run() {
		while (true) {
			try {
				
				accessToken=wxHttpSender.getAccessToken();
				if (null != accessToken) {
					System.out.println("获取access_token成功，有效时长{" + accessToken.getExpires_in()
							+ "}秒 token:{" + accessToken.getAccess_token() + "}");
					LOG.info("获取access_token成功，有效时长{" + accessToken.getExpires_in() + "}秒 token:{"
							+ accessToken.getAccess_token() + "}");
					// 休眠7000秒
					Thread.sleep((accessToken.getExpires_in() - 200) * 1000);
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

	public AccessToken getAccessToken() {
		if (accessToken != null) {
			return new AccessToken(accessToken.getExpires_in(), accessToken.getAccess_token());
		} else {
			return null;
		}
	}

}
