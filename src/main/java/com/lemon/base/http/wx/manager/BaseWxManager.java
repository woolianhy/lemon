package com.lemon.base.http.wx.manager;

import java.lang.reflect.Type;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.google.gson.Gson;
import com.lemon.base.Exception.WeChatException;
import com.lemon.base.bean.WxGlobalResult;
import com.lemon.base.config.WxConfig;
import com.lemon.base.http.MyHttpClient;
import com.lemon.base.util.TokenProxyImpl;
import com.lemon.base.util.WeChatReturnCode;

public abstract class BaseWxManager {


	@Autowired
	private MyHttpClient wxHttpClient;



	@Autowired
	private Gson gson;



	public MyHttpClient getWxHttpClient() {
		return wxHttpClient;
	}

	public void setWxHttpClient(MyHttpClient wxHttpClient) {
		this.wxHttpClient = wxHttpClient;
	}



	public Gson getGson() {
		return gson;
	}

	public void setGson(Gson gson) {
		this.gson = gson;
	}

	public <T> T safeResult(String json, Class<T> classOfT, WxConfig wxConfig)
			throws WeChatException {
		if (json == null) {
			return null;
		}
		checkError(json, wxConfig);
		return getGson().fromJson(json, classOfT);
	}

	public <T> T safeResult(String json, Type typeOfT, WxConfig wxConfig)
			throws WeChatException {
		if (json == null) {
			return null;
		}
		checkError(json, wxConfig);
		return getGson().fromJson(json, typeOfT);
	}

	/**
	 * 检查微信返回的是否是异常数据
	 * 
	 * @param json
	 * @throws WeChatException
	 */
	public void checkError(String json, WxConfig wxConfig)
			throws WeChatException {

		WxGlobalResult result = gson.fromJson(json, WxGlobalResult.class);

		if (result.getErrcode() != null && result.getErrcode() != 0) {
			String errMsg = WeChatReturnCode.getMsg(result.getErrcode());
			if (errMsg.equals("")) {
				errMsg = result.getErrmsg();
			}

			// 如果token过期异常，更新token
			if (result.getErrcode().equals(40001)
					|| result.getErrcode().equals(40014)) {
				wxConfig.initAccessToken();
			}
			throw new WeChatException("异常码:" + result.getErrcode() + ";异常说明:"
					+ errMsg);
		}
	}

}
