package com.lemon.base.http.wx.manager;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.lemon.base.Exception.WeChatException;
import com.lemon.base.bean.template.WxTemplate;
import com.lemon.base.bean.template.WxTemplateResult;
import com.lemon.base.config.WxConfig;

@Component
public class WxTemplateManager extends BaseWxManager {


	private final static Log LOG = LogFactory.getLog(WxTemplateManager.class);

	private static final String TEMPLATE_SEND_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";

	public WxTemplateResult sendTemplateMessage(WxConfig config,WxTemplate wxTemplate) throws WeChatException {
		String url = TEMPLATE_SEND_URL.replace("ACCESS_TOKEN", config.getAccessToken());
		String json = null;
		WxTemplateResult wxTemplateResult = null;
		try {
			json = getWxHttpClient().postJson(url, getGson().toJson(wxTemplate));
			wxTemplateResult = safeResult(json, WxTemplateResult.class,config);
			LOG.info(url + "\n" + json);
		} catch (IOException e) {
			LOG.error("发送微信模板消息失败", e);
		}
		return wxTemplateResult;
	}
}
