package com.lemon.base.http.server.manager;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lemon.base.bean.user.UserGroup;
import com.lemon.base.http.MyHttpClient;

public abstract class BaseServerManager {

	@Autowired
	private MyHttpClient wxHttpClient;

	@Autowired
	private Gson gson;
	
	@Value("#{lemonCommon.wechatServerDomain}")
	private String serverDomain;

//

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

	public String getServerDomain() {
		return serverDomain;
	}

	public void setServerDomain(String serverDomain) {
		this.serverDomain = serverDomain;
	}

	 // Bean --> Map 1: 利用Introspector和PropertyDescriptor 将Bean --> Map  
    public  Map<String, String> transBean2Map(Object obj) {  
        String json = gson.toJson(obj);
        return gson.fromJson(json, new TypeToken<Map<String, String>>(){}.getType());
    }  

}
