package com.lemon.base.http.server.manager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.google.gson.reflect.TypeToken;
import com.lemon.base.bean.user.WxUserInfo;

@Component
public class CustomerServerManager extends BaseServerManager {

	private static final Log LOG = LogFactory
			.getLog(CustomerServerManager.class);

	private static final String SUBSCRIBE_URI = "/services/customerInfo/wechat/subscribe.json";

	public String updateUserInfo(WxUserInfo info, String engineerId) {
		try {
			System.out.println(getServerDomain() + SUBSCRIBE_URI);
			Map<String, String> reqmap = new HashMap<String, String>();
			reqmap.put("openId", info.getOpenid());
			reqmap.put("name", info.getNickname());
			reqmap.put("province", info.getProvince());
			reqmap.put("city", info.getCity());
			reqmap.put("nation", info.getCountry());
			reqmap.put("headImg", info.getHeadimgurl());
			reqmap.put("subscribeTime",
					String.valueOf(info.getSubscribe_time()));
			reqmap.put("gender", String.valueOf(info.getSex()));
			if (!StringUtils.isEmpty(engineerId)) {
				reqmap.put("engineerId", engineerId);
			}
			for (String s : reqmap.keySet()) {
				System.out
						.println("subscribe param " + s + ":" + reqmap.get(s));
			}
			String resultStr = getWxHttpClient().post(
					getServerDomain() + SUBSCRIBE_URI, reqmap);
			LOG.info("提交用户信息响应：" + resultStr);
			Map<String, Object> map = getGson().fromJson(resultStr,
					new TypeToken<Map<String, Object>>() {
					}.getType());
			return map.get("id").toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String updateUserInfoBindTerminal(WxUserInfo info, String terminalId) {
		try {
			System.out.println(getServerDomain() + SUBSCRIBE_URI);
			Map<String, String> reqmap = new HashMap<String, String>();
			reqmap.put("openId", info.getOpenid());
			reqmap.put("name", info.getNickname());
			reqmap.put("province", info.getProvince());
			reqmap.put("city", info.getCity());
			reqmap.put("nation", info.getCountry());
			reqmap.put("headImg", info.getHeadimgurl());
			reqmap.put("subscribeTime",
					String.valueOf(info.getSubscribe_time()));
			reqmap.put("gender", String.valueOf(info.getSex()));
			if (!StringUtils.isEmpty(terminalId)) {
				reqmap.put("terminalId", terminalId);
			}
			for (String s : reqmap.keySet()) {
				System.out
						.println("subscribe param " + s + ":" + reqmap.get(s));
			}
			String resultStr = getWxHttpClient().post(
					getServerDomain() + SUBSCRIBE_URI, reqmap);
			LOG.info("提交用户信息响应：" + resultStr);
			Map<String, Object> map = getGson().fromJson(resultStr,
					new TypeToken<Map<String, Object>>() {
					}.getType());
			return map.get("id").toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String updateUserInfo(WxUserInfo info) {
		return updateUserInfo(info, null);
	}

}
