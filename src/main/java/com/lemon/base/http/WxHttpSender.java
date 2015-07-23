package com.lemon.base.http;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.lemon.base.bean.AccessToken;
import com.lemon.base.bean.Oauth2AccessToken;
import com.lemon.base.bean.UnifiedOrderResult;
import com.lemon.base.bean.WxTemplate;
import com.lemon.base.bean.WxTemplateResult;
import com.lemon.base.bean.WxUserInfo;
import com.lemon.base.util.TokenProxy;

/**
 * 
 * <p>
 * 微信http接口发送者
 * </p>
 * 
 * 
 * 
 * @author Chenwanli 2015年7月20日
 */
public class WxHttpSender {
	// 第三方用户唯一凭证
	@Value("#{lemonCommon.appID}")
	private String appId;
	
	@Value("#{lemonCommon.zf_appID}")
	private String zf_appID;//wx pay appId used by test

	// 第三方用户唯一凭证密钥
	@Value("#{lemonCommon.appsecret}")
	private String appsecret;

	@Value("#{lemonCommon.access_token_url}")
	private String access_token_url;

	@Value("#{lemonCommon.oauth2_access_token_url}")
	private String oauth2_access_token_url;

	@Value("#{lemonCommon.template_send_url}")
	private String template_send_url;

	@Value("#{lemonCommon.get_user_info_url}")
	private String get_user_info_url;

	@Value("#{lemonCommon.oauth2_state}")
	private String oauth2_state;

	@Value("#{lemonCommon.mch_id}")
	private String mch_id;

	@Value("#{lemonCommon.unifiedorder_url}")
	private String unifiedorder_url;

	@Value("#{lemonCommon.notify_url}")
	private String notify_url;

	@Value("#{lemonCommon.wxzf_key}")
	private String wxzf_key;

	@Autowired
	private MyHttpClient wxHttpClient;

	@Autowired
	private TokenProxy tokenProxy;

	// MD5算法方法
	private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	@Autowired
	private Gson gson;

	private final static Log LOG = LogFactory.getLog(WxHttpSender.class);

	public AccessToken getAccessToken() {
		String requestUrl = access_token_url.replace("APPID", appId).replace(
				"APPSECRET", appsecret);
		String json = null;
		try {

			json = wxHttpClient.get(requestUrl);
			return gson.fromJson(json, AccessToken.class);
		} catch (JsonSyntaxException e) {
			LOG.error("解析获得的AccessToken失败，json：" + json, e);
		} catch (IOException e) {
			LOG.error("获取json请求失败", e);
		}
		return null;
	}

	public Oauth2AccessToken getUserAccessToken(String code) {
		String url = oauth2_access_token_url.replace("APPID", appId)
				.replace("SECRET", appsecret).replace("CODE", code);
		String json = null;
		Oauth2AccessToken oauth2AccessToken = null;
		try {
			json = wxHttpClient.get(url);
			oauth2AccessToken = gson.fromJson(json, Oauth2AccessToken.class);
			LOG.info(url + "\n" + json);
		} catch (IOException e) {
			LOG.error("请求oauth2_token失败", e);
		}
		return oauth2AccessToken;
	}

	public WxTemplateResult sendTemplateMessage(WxTemplate wxTemplate) {
		String url = template_send_url.replace("ACCESS_TOKEN", tokenProxy
				.getAccessToken().getAccess_token());
		String json = null;
		WxTemplateResult wxTemplateResult = null;
		try {
			json = wxHttpClient.postJson(url, gson.toJson(wxTemplate));
			wxTemplateResult = gson.fromJson(json, WxTemplateResult.class);
			LOG.info(url + "\n" + json);
		} catch (IOException e) {
			LOG.error("发送微信模板消息失败", e);
		}
		return wxTemplateResult;
	}

	public WxUserInfo getUserInfo(String openId) {
		String url = get_user_info_url.replace("ACCESS_TOKEN",
				tokenProxy.getAccessToken().getAccess_token()).replace(
				"OPENID", openId);
		String json = null;
		WxUserInfo wxUserInfo = null;
		try {
			json = wxHttpClient.getJson(url);
			wxUserInfo = gson.fromJson(json, WxUserInfo.class);
			LOG.info(url + "\n" + json);
		} catch (IOException e) {
			LOG.error("获取用户信息失败", e);
		}
		return wxUserInfo;
	}

	public UnifiedOrderResult unifiedorder(String openId, String orderId,
			int total_fee) {
		UnifiedOrderResult unifiedOrderResult = null;
		try {
			SortedMap<String, String> parameters = new TreeMap<String, String>();
			parameters.put("appid", zf_appID);
			parameters.put("mch_id", mch_id);
			UUID uuid = UUID.randomUUID();
			parameters.put("nonce_str", UUID.randomUUID().toString().trim()
					.replaceAll("-", "").substring(0, 31));
			parameters.put("body", "柠檬家电");
			parameters.put("out_trade_no", orderId);
			parameters.put("total_fee", total_fee+"");
			parameters.put("spbill_create_ip", InetAddress.getLocalHost()
					.getHostAddress());
			parameters.put("notify_url", notify_url);
			parameters.put("trade_type", "JSAPI");
			parameters.put("openid", openId);
			String sign = createSign("UTF-8", parameters);
			parameters.put("sign", sign);
			String requestXML = getRequestXml(parameters);

			String json = wxHttpClient.postJson(unifiedorder_url, requestXML);
			unifiedOrderResult = gson.fromJson(json, UnifiedOrderResult.class);
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return unifiedOrderResult;
	}

	/**
	 * 将请求参数转为xml格式的String
	 * 
	 * @param parameters
	 * @return
	 */
	public static String getRequestXml(SortedMap<String, String> parameters) {
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>");
		Set es = parameters.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			sb.append("<" + k + ">" + "<![CDATA[" + v + "]]></" + k + ">");
		}
		sb.append("</xml>");
		return sb.toString();
	}

	/**
	 * 创建sign签名
	 * 
	 * @param characterEncoding
	 *            (编码格式)
	 * @param parameters
	 * @return
	 */
	public String createSign(String characterEncoding,
			SortedMap<String, String> parameters) {
		StringBuffer sb = new StringBuffer();
		Set es = parameters.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			Object v = entry.getValue();
			if (null != v && !"".equals(v) && !"sign".equals(k)
					&& !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		sb.append("key=" + wxzf_key);
		String sign = MD5Encode(sb.toString(), characterEncoding).toUpperCase();
		return sign;
	}

	/**
	 * MD5加密方法
	 * 
	 * @param origin
	 * @param charsetname
	 * @return
	 */
	public static String MD5Encode(String origin, String charsetname) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			if (charsetname == null || "".equals(charsetname))
				resultString = byteArrayToHexString(md.digest(resultString
						.getBytes()));
			else
				resultString = byteArrayToHexString(md.digest(resultString
						.getBytes(charsetname)));
		} catch (Exception exception) {

		}
		return resultString;
	}

	// MD5Util工具类中相关的方法
	private static String byteArrayToHexString(byte b[]) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++)
			resultSb.append(byteToHexString(b[i]));
		return resultSb.toString();
	}

	// MD5算法方法
	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n += 256;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

}
