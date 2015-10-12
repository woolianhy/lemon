package com.lemon.base.http.wx.manager;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lemon.base.Exception.WeChatException;
import com.lemon.base.bean.pay.OrderQueryResult;
import com.lemon.base.bean.pay.PayResult;
import com.lemon.base.bean.pay.UnifiedOrderResult;
import com.lemon.base.config.WxConfig;
import com.lemon.base.util.TokenProxyImpl;
import com.lemon.base.util.JacksonUtils;
import com.lemon.base.util.SignUtil;

/**
 * 微信支付api
 * @author Administrator
 *
 */
@Component
public class WxPayManager extends BaseWxManager {

	private static final String UNIFIEDORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

	private static final String ORDER_QUERY_URL = "https://api.mch.weixin.qq.com/pay/orderquery";

	// MD5算法方法
	private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	private static final Log LOG = LogFactory.getLog(WxPayManager.class);

	private static final SimpleDateFormat SDF = new SimpleDateFormat(
			"yyyyMMddHHmmss");

	@Autowired
	private TokenProxyImpl customerTokenProxy;

	public PayResult unifiedorder(WxConfig config, String openId,
			String orderId, String title, Double price) throws WeChatException {
		PayResult payResult = null;
		try {
			SortedMap<String, String> parameters = new TreeMap<String, String>();
			parameters.put("appid", config.getAppId());
			parameters.put("mch_id", config.getMch_id());
			parameters.put("nonce_str", UUID.randomUUID().toString().trim()
					.replaceAll("-", "").substring(0, 31));
			parameters.put("body", title);
			parameters.put("out_trade_no", orderId);
			BigDecimal b1 = new BigDecimal(Double.toString(price));
			BigDecimal b2 = new BigDecimal(Double.toString(100));
			int money = b1.multiply(b2).intValue();
			parameters.put("total_fee", money + "");
			parameters.put("spbill_create_ip", InetAddress.getLocalHost()
					.getHostAddress());
			parameters.put("notify_url", config.getNotify_url());
			
			parameters.put("trade_type", "JSAPI");
			parameters.put("time_start", SDF.format(new Date()));
			parameters
					.put("time_expire", SDF.format(new Date(new Date()
							.getTime() + 30 * 60 * 1000)));
			parameters.put("openid", openId);
			String sign = createSign(config.getWxzf_key(), "UTF-8", parameters);
			parameters.put("sign", sign);
			String requestXML = getRequestXml1(parameters);
			LOG.info("requestXML:" + requestXML);
			String xml = getWxHttpClient().post(UNIFIEDORDER_URL, requestXML);
			LOG.info("xml:" + xml);
			String json = JacksonUtils.xml2json(xml);
			LOG.info("json:" + json);
			UnifiedOrderResult uor = this.safeResult(json,
					UnifiedOrderResult.class, config);
			if (uor.getPrepay_id() != null
					&& !StringUtils.isEmpty(uor.getPrepay_id())) {
				payResult = new PayResult(uor.getAppid(),
						new Date().getTime() / 1000, uor.getNonce_str(),
						"prepay_id=" + uor.getPrepay_id(), "MD5",
						config.getWxzf_key(), customerTokenProxy
								.getJsapiTicket().getTicket());
			} else {
				payResult = new PayResult(true);
			}

		} catch (Exception e) {
			e.printStackTrace();
			LOG.info(e);
		}

		return payResult;
	}

	/**
	 * 查询订单是否支付
	 * @param config
	 * @param paymentId
	 * @return
	 */
	public OrderQueryResult orderquery(WxConfig config, String paymentId) {
		SortedMap<String, String> parameters = new TreeMap<String, String>();
		parameters.put("appid", config.getAppId());
		parameters.put("mch_id", config.getMch_id());
		parameters.put("nonce_str", UUID.randomUUID().toString().trim()
				.replaceAll("-", "").substring(0, 31));
		parameters.put("out_trade_no", paymentId);
		SortedMap<Object, Object> map = changeMapToObject(parameters);
		String sign = SignUtil.createSign("UTF-8", map, config.getWxzf_key());
		parameters.put("sign", sign);

		String requestXML = getRequestXml(parameters);

		try {
			String xml = getWxHttpClient().post(ORDER_QUERY_URL, requestXML);
			String json = JacksonUtils.xml2json(xml);
			OrderQueryResult res = this.safeResult(json,
					OrderQueryResult.class, config);
			return res;

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private SortedMap<Object, Object> changeMapToObject(
			SortedMap<String, String> parameters) {
		if (parameters == null) {
			return null;
		}

		SortedMap<Object, Object> map = new TreeMap<Object, Object>();
		for (String key : parameters.keySet()) {
			map.put(key, parameters.get(key));
		}

		return map;
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
	 * 将请求参数转为xml格式的String
	 * 
	 * @param parameters
	 * @return
	 */
	public static String getRequestXml1(SortedMap<String, String> parameters) {
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>");
		Set es = parameters.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			System.out.println(k+":"+v);
			sb.append("<" + k + ">" + v + "</" + k + ">");
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
	public String createSign(String wxzf_key, String characterEncoding,
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
	
	public static void main(String[] args) {
		System.out.println( SDF.format(new Date(new Date()
				.getTime() + 30 * 60 * 1000)));
	}
}
