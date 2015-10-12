package com.lemon.base.util;

import com.lemon.base.bean.token.AccessToken;
import com.lemon.base.bean.token.JSAPITicket;
import com.lemon.base.thread.TokenThreadRunnable;

public class TokenProxyImpl implements TokenProxy{
	
	private TokenThreadRunnable runnable;
	
	public TokenProxyImpl(TokenThreadRunnable tokenThreadRunnable){
		this.runnable=tokenThreadRunnable;
		new Thread(runnable).start();
		System.out.println("customer tokenProxy init...");
	}

	public void init() {
		
	}

	public AccessToken getAccessToken() {

		return runnable.getAccessToken();

	}
	
	public void initAccessToken(){
		runnable.initAccessToken();
	}
	
	public JSAPITicket getJsapiTicket() {

		return runnable.getJsapiTicket();

	}


	
}
