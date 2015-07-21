package com.lemon.base.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.lemon.base.bean.TemplateData;
import com.lemon.base.bean.WxTemplate;
import com.lemon.base.bean.WxTemplateResult;
import com.lemon.base.http.WxHttpSender;

public class TemplateMessageSender {

	@Value("#{lemonCommon.test_template_id}")
	private String test_template_id;

	@Autowired
	private WxHttpSender wxHttpSender;

	public WxTemplateResult sendTestTemplate(String openid, Map<String, String> params) {
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
		return wxHttpSender.sendTemplateMessage(t);

	}

	public WxTemplateResult sendTemplate(String openid, String templateId,
			Map<String, String> params) {
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
		return wxHttpSender.sendTemplateMessage(t);
	}
}
