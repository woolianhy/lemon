package com.lemon.msg.handle;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.lemon.msg.bean.BaseMsg;
import com.lemon.msg.bean.TextMsg;
import com.lemon.msg.req.bean.BaseEvent;
import com.lemon.msg.req.bean.BaseReq;
import com.lemon.msg.req.bean.BaseReqMsg;
import com.lemon.msg.req.bean.EventType;
import com.lemon.msg.req.bean.ImageReqMsg;
import com.lemon.msg.req.bean.LinkReqMsg;
import com.lemon.msg.req.bean.LocationEvent;
import com.lemon.msg.req.bean.LocationReqMsg;
import com.lemon.msg.req.bean.MenuEvent;
import com.lemon.msg.req.bean.QrCodeEvent;
import com.lemon.msg.req.bean.ReqType;
import com.lemon.msg.req.bean.TextReqMsg;
import com.lemon.msg.req.bean.VideoReqMsg;
import com.lemon.msg.req.bean.VoiceReqMsg;
import com.lemon.msg.util.MessageUtil;
import com.lemon.msg.util.SignUtil;

/**
 * 
 * <p>
 * 微信消息处理基类
 * </p>
 * 
 *
 * @author Chen 2015年7月14日
 */
public abstract class MessageHandle {
    
	/**
	 * 该方法返回token
	 */
	protected abstract String getToken();

	/**
	 * 该方法的唯一作用是验证来自微信服务器的消息<br>
	 * 如果没有特殊需要，不建议重写该方法
	 */
	public String  doGet(HttpServletRequest request) {
		// 确认请求来自微信服务器
		if (isLegal(request)) {
			return request.getParameter("echostr");
		} else {
			// 非法请求，默认不予响应
			return null;
		}
	}

	/**
	 * 该方法验证消息是否来自微信服务器，并处理来自微信服务器的消息<br>
	 * 如果没有特殊需要，不建议重写该方法
	 */
	public String doPost(HttpServletRequest request) {


		if (!isLegal(request)) {
			// 非法请求，默认不予响应
			return null;
		}

		// 处理消息
		String resp = processRequest(request);

		// 响应消息
		return resp;
	}

	/**
	 * 处理消息
	 */
	private String processRequest(HttpServletRequest request) {

		Map<String, String> reqMap = MessageUtil.parseXml(request);
		String fromUserName = reqMap.get("FromUserName");
		String toUserName = reqMap.get("ToUserName");
		String msgType = reqMap.get("MsgType");

		BaseMsg msg = null;// 要发送的消息

		// 事件推送
		if (msgType.equals(ReqType.EVENT)) {
			// 事件类型
			String eventType = reqMap.get("Event");

			// 二维码事件
			String ticket = reqMap.get("Ticket");
			if (ticket != null) {
				String eventKey = reqMap.get("EventKey");
				QrCodeEvent event = new QrCodeEvent(eventKey, ticket);
				buildBasicEvent(reqMap, event);
				msg = handleQrCodeEvent(event);
			}
			// 订阅
			if (eventType.equals(EventType.SUBSCRIBE)) {
				BaseEvent event = new BaseEvent();
				buildBasicEvent(reqMap, event);
				msg = handleSubscribe(event);
			}
			// 取消订阅
			else if (eventType.equals(EventType.UNSUBSCRIBE)) {
				BaseEvent event = new BaseEvent();
				buildBasicEvent(reqMap, event);
				msg = handleUnsubscribe(event);
			}
			// 点击菜单拉取消息时的事件推送
			else if (eventType.equals(EventType.CLICK)) {
				String eventKey = reqMap.get("EventKey");
				MenuEvent event = new MenuEvent(eventKey);
				buildBasicEvent(reqMap, event);
				msg = handleMenuClickEvent(event);
			}
			// 点击菜单跳转链接时的事件推送
			else if (eventType.equals(EventType.VIEW)) {
				String eventKey = reqMap.get("EventKey");
				MenuEvent event = new MenuEvent(eventKey);
				buildBasicEvent(reqMap, event);
				msg = handleMenuViewEvent(event);
			}
			// 上报地理位置事件
			else if (eventType.equals(EventType.LOCATION)) {
				double latitude = Double.parseDouble(reqMap.get("Latitude"));
				double longitude = Double.parseDouble(reqMap.get("Longitude"));
				double precision = Double.parseDouble(reqMap.get("Precision"));
				LocationEvent event = new LocationEvent(latitude, longitude,
						precision);
				buildBasicEvent(reqMap, event);
				msg = handleLocationEvent(event);
			}

		} else {// 接受普通消息

			// 文本消息
			if (msgType.equals(ReqType.TEXT)) {
				String content = reqMap.get("Content");
				TextReqMsg textReqMsg = new TextReqMsg(content);
				buildBasicReqMsg(reqMap, textReqMsg);
				msg = handleTextMsg(textReqMsg);
			}
			// 图片消息
			else if (msgType.equals(ReqType.IMAGE)) {
				String picUrl = reqMap.get("PicUrl");
				String mediaId = reqMap.get("MediaId");
				ImageReqMsg imageReqMsg = new ImageReqMsg(picUrl, mediaId);
				buildBasicReqMsg(reqMap, imageReqMsg);
				msg = handleImageMsg(imageReqMsg);
			}
			// 音频消息
			else if (msgType.equals(ReqType.VOICE)) {
				String format = reqMap.get("Format");
				String mediaId = reqMap.get("MediaId");
				String recognition = reqMap.get("Recognition");
				VoiceReqMsg voiceReqMsg = new VoiceReqMsg(mediaId, format,
						recognition);
				buildBasicReqMsg(reqMap, voiceReqMsg);
				msg = handleVoiceMsg(voiceReqMsg);
			}
			// 视频消息
			else if (msgType.equals(ReqType.VIDEO)) {
				String thumbMediaId = reqMap.get("ThumbMediaId");
				String mediaId = reqMap.get("MediaId");
				VideoReqMsg videoReqMsg = new VideoReqMsg(mediaId, thumbMediaId);
				buildBasicReqMsg(reqMap, videoReqMsg);
				msg = handleVideoMsg(videoReqMsg);
			}
			// 地理位置消息
			else if (msgType.equals(ReqType.LOCATION)) {
				double locationX = Double.parseDouble(reqMap.get("Location_X"));
				double locationY = Double.parseDouble(reqMap.get("Location_Y"));
				int scale = Integer.parseInt(reqMap.get("Scale"));
				String label = reqMap.get("Label");
				LocationReqMsg locationReqMsg = new LocationReqMsg(locationX,
						locationY, scale, label);
				buildBasicReqMsg(reqMap, locationReqMsg);
				msg = handleLocationMsg(locationReqMsg);
			}
			// 链接消息
			else if (msgType.equals(ReqType.LINK)) {
				String title = reqMap.get("Title");
				String description = reqMap.get("Description");
				String url = reqMap.get("Url");
				LinkReqMsg linkReqMsg = new LinkReqMsg(title, description, url);
				buildBasicReqMsg(reqMap, linkReqMsg);
				msg = handleLinkMsg(linkReqMsg);
			}

		}

		if (msg == null) {
			// 回复空串是微信的规定，代表不回复
			return "";
		}

		msg.setFromUserName(toUserName);
		msg.setToUserName(fromUserName);
		return msg.toXml();
	}

	

	/**
	 * 处理链接消息
	 */
	protected BaseMsg handleLinkMsg(LinkReqMsg msg) {
		return handleDefaultMsg(msg);
	}

	/**
	 * 处理扫描带参数二维码事件
	 */
	protected BaseMsg handleQrCodeEvent(QrCodeEvent event) {
		return handleDefaultEvent(event);
	}

	/**
	 * 处理上报地理位置事件
	 */
	protected BaseMsg handleLocationEvent(LocationEvent event) {
		return handleDefaultEvent(event);
	}

	

	/**
	 * 处理点击菜单跳转链接时的事件推送
	 */
	protected BaseMsg handleMenuViewEvent(MenuEvent event) {
		return handleDefaultEvent(event);
	}

	/**
	 * 处理订阅事件<br>
	 * 默认不回复
	 */
	protected BaseMsg handleSubscribe(BaseEvent event) {
		return null;
	}

	/**
	 * 处理取消订阅事件<br>
	 * 默认不回复
	 */
	protected BaseMsg handleUnsubscribe(BaseEvent event) {
		return null;
	}

	/**
	 * 处理消息的默认方式<br>
	 * 如果不重写该方法，则默认不返回任何消息
	 */
	protected BaseMsg handleDefaultMsg(BaseReqMsg msg) {
		return null;
	}

	/**
	 * 设置处理事件的默认方式<br>
	 * 如果不重写该方法，则默认不返回任何消息
	 */
	protected BaseMsg handleDefaultEvent(BaseEvent event) {
		return null;
	}
	
	/**
	 * 接收用户发送的文本消息，并以文本消息回应<br>
	 * 注意：该方法为“仅用文本消息回应文本消息”提供便利， 如果可能需要用其他类型的消息去回应文本消息，则不能重写该方法，应该重写onText方法<br>
	 * 
	 * 重写该方法会导致onText方法失效
	 * 
	 * @param content
	 *            用户发送的消息
	 * 
	 * @return 回应用户的文本
	 */
	protected String handleTextWithText(String content) {
		return null;
	}

	/**
	 * 回应用户发送的文本消息
	 * 
	 * @param content
	 *            用户发送的消息
	 * 
	 * @return 回应用户的消息
	 */
	protected BaseMsg onText(String content) {
		return null;
	}

	/**
	 * 回应用户发送的图片消息
	 * 
	 * @param mediaId
	 *            图片消息媒体id
	 * @param picUrl
	 *            图片链接
	 * 
	 * @return 回应用户的消息
	 */
	protected BaseMsg onImage(String mediaId, String picUrl) {
		return null;
	}

	/**
	 * 回应用户发送的语音消息
	 * 
	 * @param mediaId
	 *            语音消息媒体id
	 * @param format
	 *            语音格式
	 * 
	 * @return 回应用户的消息
	 */
	protected BaseMsg onVoice(String mediaId, String format) {
		return null;
	}

	/**
	 * 回应用户发送的视频消息
	 * 
	 * @param mediaId
	 *            视频消息媒体id
	 * @param thumbMediaId
	 *            视频消息缩略图的媒体id
	 * 
	 * @return 回应用户的消息
	 */
	protected BaseMsg onVideo(String mediaId, String thumbMediaId) {
		return null;
	}

	/**
	 * 回应用户发送的地理位置消息
	 * 
	 * @param locationX
	 *            纬度
	 * @param locationY
	 *            经度
	 * @param scale
	 *            地图缩放大小
	 * @param label
	 *            地理位置信息
	 * 
	 * @return 回应用户的消息
	 */
	protected BaseMsg onLocation(double locationX, double locationY, int scale,
			String label) {
		return null;
	}

	/**
	 * 回应用户发送的链接消息
	 * 
	 * @param title
	 *            消息标题
	 * @param description
	 *            消息描述
	 * @param url
	 *            消息链接
	 * 
	 * @return 回应用户的消息
	 */
	protected BaseMsg onLink(String title, String description, String url) {
		return null;
	}

	/**
	 * 回应菜单点击事件
	 * 
	 * @param eventKey
	 *            事件KEY值，与自定义菜单接口中KEY值对应
	 * 
	 * @return 回应用户的消息
	 */
	private BaseMsg onMenuClick(String eventKey) {
		return null;
	}

	protected final BaseMsg handleTextMsg(TextReqMsg msg) {

		String reqText = msg.getContent();

		String respText = handleTextWithText(reqText);
		if (respText != null) {// handleTextWithText方法被重写
			return new TextMsg(respText);
		}

		BaseMsg respMsg = onText(reqText);
		return responseMsgOrDefault(respMsg, msg);
	}

	protected final BaseMsg handleImageMsg(ImageReqMsg msg) {
		BaseMsg respMsg = onImage(msg.getMediaId(), msg.getPicUrl());
		return responseMsgOrDefault(respMsg, msg);
	}

	protected final BaseMsg handleVoiceMsg(VoiceReqMsg msg) {
		BaseMsg respMsg = onVoice(msg.getMediaId(), msg.getFormat());
		return responseMsgOrDefault(respMsg, msg);
	}

	protected final BaseMsg handleVideoMsg(VideoReqMsg msg) {
		BaseMsg respMsg = onVideo(msg.getMediaId(), msg.getThumbMediaId());
		return responseMsgOrDefault(respMsg, msg);
	}

	protected final BaseMsg handleLocationMsg(LocationReqMsg msg) {
		BaseMsg respMsg = onLocation(msg.getLocationX(), msg.getLocationY(),
				msg.getScale(), msg.getLabel());
		return responseMsgOrDefault(respMsg, msg);
	}

	protected BaseMsg handleMenuClickEvent(MenuEvent event) {
		BaseMsg respMsg = onMenuClick(event.getEventKey());
		return responseMsgOrDefault(respMsg, event);
	}

	/**
	 * 如果respMsg不为null，则返回respMsg，否则返回默认消息
	 */
	private BaseMsg responseMsgOrDefault(BaseMsg respMsg, BaseReqMsg reqMsg) {
		if (respMsg != null) {
			return respMsg;
		}

		return handleDefaultMsg(reqMsg);
	}

	/**
	 * 如果respEvent不为null，则返回respEvent，否则返回默认消息
	 */
	private BaseMsg responseMsgOrDefault(BaseMsg respMsg, BaseEvent reqEvent) {
		if (respMsg != null) {
			return respMsg;
		}

		return handleDefaultEvent(reqEvent);
	}

	/**
	 * 为事件普通消息对象添加基本参数<br>
	 * 参数包括：MsgId、MsgType、FromUserName、ToUserName和CreateTime
	 */
	private void buildBasicReqMsg(Map<String, String> reqMap, BaseReqMsg reqMsg) {
		addBasicReqParams(reqMap, reqMsg);
		reqMsg.setMsgId(reqMap.get("MsgId"));
	}

	/**
	 * 为事件推送对象添加基本参数<br>
	 * 参数包括：Event、MsgType、FromUserName、ToUserName和CreateTime
	 */
	private void buildBasicEvent(Map<String, String> reqMap, BaseEvent event) {
		addBasicReqParams(reqMap, event);
		event.setEvent(reqMap.get("Event"));
	}

	/**
	 * 为请求对象添加基本参数，包括MsgType、FromUserName、ToUserName和CreateTime<br>
	 * 请求对象包括普通消息和事件推送
	 */
	private void addBasicReqParams(Map<String, String> reqMap, BaseReq req) {
		req.setMsgType(reqMap.get("MsgType"));
		req.setFromUserName(reqMap.get("FromUserName"));
		req.setToUserName(reqMap.get("ToUserName"));
		req.setCreateTime(Long.parseLong(reqMap.get("CreateTime")));
	}

	/**
	 * 判断请求是否来自微信服务器
	 */
	private boolean isLegal(HttpServletRequest request) {
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		return SignUtil.checkSignature(getToken(), signature, timestamp, nonce);
	}
	
	
}
