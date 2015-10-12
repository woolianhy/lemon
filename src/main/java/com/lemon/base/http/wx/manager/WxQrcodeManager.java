package com.lemon.base.http.wx.manager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.lemon.base.Exception.WeChatException;
import com.lemon.base.bean.qrcode.QrcodeTicketActionInfo;
import com.lemon.base.bean.qrcode.QrcodeTicketRequest;
import com.lemon.base.bean.qrcode.QrcodeTicketScene;
import com.lemon.base.bean.qrcode.Ticket;
import com.lemon.base.config.WxConfig;

@Component
public class WxQrcodeManager extends BaseWxManager{
	
	private static final String QRCODE_URL="https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=TOKEN";

	private static final String SHOW_QRCODE_URL="https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=TICKET";
	
	//长链接转短链接接口
	private static final String SHORTURL_POST_URL="https://api.weixin.qq.com/cgi-bin/shorturl?access_token=";
	
	private static final  Log LOG = LogFactory.getLog(WxQrcodeManager.class);
	
	
	/**
	 * 长链接转短链接接口
	 * @param longUrl 需要转换的长链接
	 * @return
	 */
	public String shortUrl(WxConfig config,String longUrl){
		String resultStr=null;
		try {
			Map<String,String> reqMap=new HashMap<String, String>();
			reqMap.put("action", "long2short");
			reqMap.put("long_url", longUrl);
			String requestData=getGson().toJson(reqMap);
			resultStr = getWxHttpClient().post(SHORTURL_POST_URL + config.getAccessToken(), requestData);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return resultStr;
	}
	
	
	public Ticket qrcodeTiket(WxConfig config,int scene_id, int seconds) throws WeChatException {
		String url = QRCODE_URL.replace("TOKEN", config.getAccessToken());
		QrcodeTicketRequest qreq = new QrcodeTicketRequest();
		qreq.setExpire_seconds(seconds);
		qreq.setAction_name(QrcodeTicketRequest.ACTION_NAME_QR_SCENE);
		QrcodeTicketActionInfo info = new QrcodeTicketActionInfo();
		info.setScene(new QrcodeTicketScene(scene_id));
		qreq.setAction_info(info);

		String reqjson = getGson().toJson(qreq);

		try {
			String resultJson = getWxHttpClient().postJson(url, reqjson);
			return safeResult(resultJson, Ticket.class,config);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public Ticket qrcodeLimitTiket(WxConfig config,String scene_str, int seconds) throws WeChatException {
		String url = QRCODE_URL.replace("TOKEN", config.getAccessToken());
		QrcodeTicketRequest qreq = new QrcodeTicketRequest();
		qreq.setAction_name(QrcodeTicketRequest.ACTION_NAME_QR_LIMIT_STR_SCENE);
		QrcodeTicketActionInfo info = new QrcodeTicketActionInfo();
		info.setScene(new QrcodeTicketScene(scene_str));
		qreq.setAction_info(info);
		
		String reqjson = getGson().toJson(qreq);

		try {
			String resultJson = getWxHttpClient().postJson(url, reqjson);
			Ticket tiket = safeResult(resultJson, Ticket.class,config);
			return tiket;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void showqrcode(String ticket, HttpServletResponse rps) {
		String url = SHOW_QRCODE_URL.replace("TICKET", ticket);
		getWxHttpClient().getJpg(url, rps);
	}
}
