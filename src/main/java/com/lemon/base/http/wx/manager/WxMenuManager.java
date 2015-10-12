package com.lemon.base.http.wx.manager;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.lemon.base.Exception.WeChatException;
import com.lemon.base.bean.menu.Menu;
import com.lemon.base.config.WxConfig;

@Component
public class WxMenuManager extends BaseWxManager{
	
	private final static Log LOG = LogFactory.getLog(WxMenuManager.class);
	
	private static final String MENU_CREATE_POST_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=";
	private static final String MENU_GET_GET_URL = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=";
	private static final String MENU_DEL_GET_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=";

	/**
	 * 创建菜单
	 * @throws WeChatException 
	 */
	public void create(WxConfig config,Menu menu) throws WeChatException{
		try {
			String resultStr = getWxHttpClient().post(MENU_CREATE_POST_URL+config.getAccessToken(), getGson().toJson(menu));
		    this.checkError(resultStr,config);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 查询菜单
	 * @throws WeChatException 
	 */
	public Menu getMenu(WxConfig config) throws WeChatException {	
		String resultStr=null;
		try {
			resultStr = getWxHttpClient().get(MENU_GET_GET_URL+config.getAccessToken());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.safeResult(resultStr, Menu.class,config);
	}
	/**
	 * 删除菜单
	 * @throws WeChatException 
	 * @throws IOException 
	 */
	public void delete(WxConfig config) throws WeChatException{
		try {
			String resultStr = getWxHttpClient().get(MENU_DEL_GET_URL+config.getAccessToken());
			this.checkError(resultStr,config);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
}
