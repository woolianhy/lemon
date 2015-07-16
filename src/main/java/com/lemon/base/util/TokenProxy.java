package com.lemon.base.util;

import org.springframework.beans.factory.annotation.Autowired;

import com.lemon.base.bean.AccessToken;
import com.lemon.base.thread.TokenThread;

public class TokenProxy {
 
	@Autowired
	private TokenThread tokenThread;
	
	public void init() {
		new Thread(tokenThread).start();
		System.out.println("tokenProxy init...");
	}
	
	public AccessToken getAccessToken(){
		
		return tokenThread.getAccessToken();
		
	}
}
