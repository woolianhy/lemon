package com.lemon.base.util;

import com.lemon.base.bean.token.AccessToken;
import com.lemon.base.bean.token.JSAPITicket;

public interface TokenProxy {

	public AccessToken getAccessToken();

	public void initAccessToken();

	public JSAPITicket getJsapiTicket();
}
