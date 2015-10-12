package com.lemon.base.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.lemon.base.Exception.WeChatException;
import com.lemon.base.bean.template.TemplateData;
import com.lemon.base.bean.template.WxTemplate;
import com.lemon.base.bean.template.WxTemplateResult;
import com.lemon.base.config.WxConfig;
import com.lemon.base.http.wx.manager.WxTemplateManager;

public class TemplateMessageSender {

	@Value("#{lemonCommon.test_template_id}")
	private String test_template_id;

	@Autowired
	private WxTemplateManager httpTemplateManager;

	public WxTemplateResult sendTestTemplate(WxConfig config,String openid, Map<String, String> params) throws WeChatException {
		WxTemplate t = new WxTemplate();
		t.setTouser(openid);
		t.setTopcolor("#000000");
		t.setTemplate_id(test_template_id);
		Map<String, TemplateData> m = new HashMap<String, TemplateData>();
		for (String key : params.keySet()) {
			TemplateData d = new TemplateData();
			d.setColor("#000000");
			d.setValue(params.get(key));
			m.put(key, d);
		}
		t.setData(m);
		return httpTemplateManager.sendTemplateMessage(config,t);

	}

	public WxTemplateResult sendTemplate(WxConfig config,String openid, String templateId,
			Map<String, String> params) throws WeChatException {
		WxTemplate t = new WxTemplate();
		t.setTouser(openid);
		t.setTopcolor("#000000");
		t.setTemplate_id(templateId);
		Map<String, TemplateData> m = new HashMap<String, TemplateData>();
		for (String key : params.keySet()) {
			TemplateData d = new TemplateData();
			d.setColor("#000000");
			d.setValue(params.get(key));
			m.put(key, d);
		}
		t.setData(m);
		return httpTemplateManager.sendTemplateMessage(config,t);
	}
}
