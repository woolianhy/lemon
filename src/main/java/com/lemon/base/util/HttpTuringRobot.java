package com.lemon.base.util;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.lemon.base.bean.RobotAnswer;
import com.lemon.base.bean.RobotAnswerItem;
import com.lemon.msg.bean.Article;
import com.lemon.msg.bean.BaseMsg;
import com.lemon.msg.bean.NewsMsg;
import com.lemon.msg.bean.TextMsg;

@Component
public class HttpTuringRobot {

	@Autowired
	private WxHttpClient wxHttpClient;

	@Value("#{lemonCommon.turingKey}")
	private String turingKey;

	@Value("#{lemonCommon.turingUrl}")
	private String turingUrl;
	
	@Autowired
	private Gson gson;

	public String answer(String info, String userid, String loc) throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append(turingUrl);
		sb.append("?key=").append(turingKey);
		if (!StringUtils.isEmpty(info)) {
			sb.append("&info=").append(info);
		} else {
			throw new IllegalArgumentException("与图灵机器人对话的内容不能为空！");
		}

		if (!StringUtils.isEmpty(userid)) {
			sb.append("&userid=").append(userid);
		}
		if (!StringUtils.isEmpty(loc)) {
			sb.append("&loc=").append(loc);
		}
		return wxHttpClient.getJson(sb.toString());
	}
	
	public BaseMsg answerMsg(String info, String userid, String loc) throws IOException {
		String answer = answer(info, userid, loc);
		RobotAnswer robotAnswer = gson.fromJson(answer, RobotAnswer.class);
		
		if(100000==robotAnswer.getCode()){
			TextMsg msg=new TextMsg(robotAnswer.getText());
			return msg;
		}
		
		if(200000==robotAnswer.getCode()){
			TextMsg msg=new TextMsg();
			msg.addLink(robotAnswer.getText(), robotAnswer.getUrl());
			return msg;
		}
		
		if(302000==robotAnswer.getCode()){
			NewsMsg msg=new NewsMsg();
			msg.add(robotAnswer.getText());
			for (RobotAnswerItem item : robotAnswer.getList()) {
				Article a=new Article(item.getArticle(), null, item.getIcon(), item.getDetailurl());
				msg.add(a);
			}
			return msg;
		}
		
		if(305000==robotAnswer.getCode()){
			NewsMsg msg=new NewsMsg();
			msg.add(robotAnswer.getText());
			for (RobotAnswerItem item : robotAnswer.getList()) {
				Article a=new Article(item.getTrainnum(), item.getStart()+"-"+item.getTerminal()+"\n"+"时间："+item.getStarttime()+"->"+item.getEndtime(), item.getIcon(), item.getDetailurl());
				msg.add(a);
			}
			return msg;
		}
		
		if(306000==robotAnswer.getCode()){
			NewsMsg msg=new NewsMsg();
			msg.add(robotAnswer.getText());
			for (RobotAnswerItem item : robotAnswer.getList()) {
				Article a=new Article(item.getFlight(), item.getRoute()+"   "+item.getState()+"\n"+"时间："+item.getStarttime()+"->"+item.getEndtime(), item.getIcon(), item.getDetailurl());
				msg.add(a);
			}
			return msg;
		}
		
		
		if(308000==robotAnswer.getCode()){
			NewsMsg msg=new NewsMsg();
			msg.add(robotAnswer.getText());
			for (RobotAnswerItem item : robotAnswer.getList()) {
				Article a=new Article(item.getName(), item.getInfo(), item.getIcon(), item.getDetailurl());
				msg.add(a);
			}
			return msg;
		}
		return null;
	}

	public WxHttpClient getWxHttpClient() {
		return wxHttpClient;
	}

	public void setWxHttpClient(WxHttpClient wxHttpClient) {
		this.wxHttpClient = wxHttpClient;
	}

	public String getTuringKey() {
		return turingKey;
	}

	public void setTuringKey(String turingKey) {
		this.turingKey = turingKey;
	}

	public String getTuringUrl() {
		return turingUrl;
	}

	public void setTuringUrl(String turingUrl) {
		this.turingUrl = turingUrl;
	}

	public Gson getGson() {
		return gson;
	}

	public void setGson(Gson gson) {
		this.gson = gson;
	}
	
	

}
