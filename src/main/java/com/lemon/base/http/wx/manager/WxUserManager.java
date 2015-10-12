package com.lemon.base.http.wx.manager;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.RuntimeErrorException;
import javax.servlet.http.HttpUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.google.gson.reflect.TypeToken;
import com.lemon.base.Exception.WeChatException;
import com.lemon.base.bean.oauth2.Oauth2AccessToken;
import com.lemon.base.bean.user.UserCollection;
import com.lemon.base.bean.user.UserGroup;
import com.lemon.base.bean.user.WxUserInfo;
import com.lemon.base.config.WxConfig;

@Component
public class WxUserManager extends BaseWxManager {

	private static final String OAUTH2_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";

	private static final String USER_INFO_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

	private static final String GET_USER_INFO_URL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

	// 获取用户列表
	private static final String USRE_GET_URL = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=";
	// 设置用户备注名
	private static final String USER_UPDATE_REMARK_POST_URL = "https://api.weixin.qq.com/cgi-bin/user/info/updateremark?access_token=";
	// 获取用户基本信息
	private static final String USER_INFO_GET_URL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=";
	// 创建分组
	private static final String UserGroup_CREATE_POST_URL = "https://api.weixin.qq.com/cgi-bin/UserGroups/create?access_token=";
	// 查询所有分组
	private static final String UserGroup_GET_POST_URL = "https://api.weixin.qq.com/cgi-bin/UserGroups/get?access_token=";
	// 查询用户所在分组
	private static final String UserGroup_GETID_POST_URL = "https://api.weixin.qq.com/cgi-bin/UserGroups/getid?access_token=";
	// 修改分组名
	private static final String UserGroup_UPDATE_POST_URL = "https://api.weixin.qq.com/cgi-bin/UserGroups/update?access_token=";
	// 移动用户分组
	private static final String UserGroup_MEMBERS_UPDATE_POST_URL = "https://api.weixin.qq.com/cgi-bin/UserGroups/members/update?access_token=";
	// 批量移动用户分组
	private static final String UserGroup_MEMBERS_DATCHUPDATE_POST_URL = "https://api.weixin.qq.com/cgi-bin/UserGroups/members/batchupdate?access_token=";
	// 删除分组
	private static final String UserGroup_DELETE_POST_URL = "https://api.weixin.qq.com/cgi-bin/UserGroups/delete?access_token=";

	private final static Log LOG = LogFactory.getLog(WxUserManager.class);

	public Oauth2AccessToken getUserAccessToken(WxConfig config, String code)
			throws WeChatException {
		String url = OAUTH2_ACCESS_TOKEN_URL
				.replace("APPID", config.getAppId())
				.replace("SECRET", config.getAppsecret()).replace("CODE", code);
		String json = null;
		Oauth2AccessToken oauth2AccessToken = null;
		try {
			json = getWxHttpClient().get(url);
			oauth2AccessToken = safeResult(json, Oauth2AccessToken.class,config);
			LOG.info(url + "\n" + json);
		} catch (IOException e) {
			LOG.error("请求oauth2_token失败", e);
		}
		return oauth2AccessToken;
	}

	public WxUserInfo getUserInfoByAccessToken(WxConfig config,
			String accessToken, String openid) throws WeChatException {
		String url = USER_INFO_ACCESS_TOKEN_URL.replace("ACCESS_TOKEN",
				accessToken).replace("OPENID", openid);
		String json = null;
		WxUserInfo wxUserInfo = null;
		try {
			json = getWxHttpClient().get(url);
			wxUserInfo = safeResult(json, WxUserInfo.class,config);
			LOG.info(url + "\n" + json);
		} catch (IOException e) {
			LOG.error("请求wxUserInfo失败", e);
		}
		return wxUserInfo;
	}

	public WxUserInfo getUserInfo(WxConfig config, String openId) {
		return getUserInfo(config, openId, 0);
	}

	private WxUserInfo getUserInfo(WxConfig config, String openId, int count) {
		String url = GET_USER_INFO_URL.replace("ACCESS_TOKEN",
				config.getAccessToken()).replace("OPENID", openId);
		String json = null;
		WxUserInfo wxUserInfo = null;
		try {
			json = getWxHttpClient().getJson(url);
			wxUserInfo = safeResult(json, WxUserInfo.class,config);
			LOG.info(url + "\n" + json);
		} catch (IOException e) {
			LOG.error("获取用户信息失败", e);
		} catch (WeChatException e) {
			LOG.error("WeChatException", e);
			if (count < 2) {
				String msg = e.getMessage().toString();
				if (msg.indexOf("40001") > -1 || msg.indexOf("40014") > -1) {
					getUserInfo(config, openId, count + 1);
				}
			}else{
				throw new IllegalArgumentException("微信服务器异常！");
			}

		}
		return wxUserInfo;
	}

	/**
	 * 获取所有的关注者列表
	 * 
	 * @return
	 * @throws WeChatException
	 */
	public List<String> allSubscriber(WxConfig config) throws WeChatException {
		UserCollection UserCollection = subscriberList(config);
		String nextOpenId = UserCollection.getNextOpenid();
		while (StringUtils.isNotBlank(nextOpenId)) {
			UserCollection f = subscriberList(config, nextOpenId);
			nextOpenId = f.getNextOpenid();
			if (f.getData() != null) {
				UserCollection.getData().getOpenid()
						.addAll(f.getData().getOpenid());
			}
		}
		return UserCollection.getData().getOpenid();
	}

	/**
	 * 获取帐号的关注者列表前10000人
	 * 
	 * @return
	 * @throws WeChatException
	 */
	public UserCollection subscriberList(WxConfig config)
			throws WeChatException {
		return subscriberList(config, null);
	}

	/**
	 * 获取帐号的关注者列表
	 * 
	 * @param nextOpenId
	 * @return
	 * @throws WeChatException
	 */
	public UserCollection subscriberList(WxConfig config, String nextOpenId)
			throws WeChatException {
		String url = USRE_GET_URL + config.getAccessToken();
		if (StringUtils.isNotBlank(nextOpenId)) {
			url += "&next_openid=" + nextOpenId;
		}
		String resultStr = null;
		try {
			resultStr = getWxHttpClient().get(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return safeResult(resultStr, UserCollection.class,config);
	}

	/**
	 * 设置用户备注名
	 * 
	 * @param openid
	 *            用户openid
	 * @param remark
	 *            新的备注名，长度必须小于30字符
	 * @return
	 * @throws WeChatException
	 */
	public void updateRemark(WxConfig config, String openId, String remark)
			throws WeChatException {
		Map<String, String> pushData = new HashMap<String, String>();
		pushData.put("openid", openId);
		pushData.put("remark", remark);
		String requestData = getGson().toJson(pushData);

		try {
			String resultStr = getWxHttpClient().post(
					USER_UPDATE_REMARK_POST_URL + config.getAccessToken(),
					requestData);
			this.checkError(resultStr,config);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 创建分组
	 * 
	 * @param name
	 *            分组名字（30个字符以内）
	 * @return
	 * @throws WeChatException
	 */
	public UserGroup createUserGroup(WxConfig config, String name)
			throws WeChatException {
		Map<String, String> nameJson = new HashMap<String, String>();
		Map<String, String> UserGroupJson = new HashMap<String, String>();
		nameJson.put("name", name);
		UserGroupJson.put("UserGroup", getGson().toJson(nameJson));
		String requestData = UserGroupJson.toString();
		try {
			String resultStr = getWxHttpClient().post(
					UserGroup_CREATE_POST_URL + config.getAccessToken(),
					requestData);
			return this.safeResult(resultStr, UserGroup.class,config);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 查询所有分组
	 * 
	 * @return
	 * @throws WeChatException
	 */
	public List<UserGroup> getUserGroup(WxConfig config) throws WeChatException {
		String resultStr = null;
		try {
			resultStr = getWxHttpClient().post(
					UserGroup_GET_POST_URL + config.getAccessToken(), "");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<UserGroup> list = this.safeResult(resultStr,
				new TypeToken<List<UserGroup>>() {
				}.getType(),config);
		return list;
	}

	/**
	 * 查询用户所在分组
	 * 
	 * @param openId
	 *            用户的OpenID
	 * @return 用户所属的UserGroupid
	 */
	public Integer getIdUserGroup(WxConfig config, String openId) {
		Map<String, String> jsonObject = new HashMap<String, String>();
		jsonObject.put("openid", openId);

		String requestData = getGson().toJson(jsonObject);
		try {
			String resultStr = getWxHttpClient().post(
					UserGroup_GETID_POST_URL + config.getAccessToken(),
					requestData);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 修改分组名
	 * 
	 * @param UserGroupId
	 *            分组id
	 * @param name
	 *            分组名称
	 * @throws WeChatException
	 */
	public void updateUserGroup(WxConfig config, int UserGroupId, String name)
			throws WeChatException {
		Map<String, Object> nameJson = new HashMap<String, Object>();
		Map<String, String> UserGroupJson = new HashMap<String, String>();
		nameJson.put("id", UserGroupId);
		nameJson.put("name", name);
		UserGroupJson.put("UserGroup", getGson().toJson(nameJson));
		String requestData = getGson().toJson(UserGroupJson);
		try {
			String resultStr = getWxHttpClient().post(
					UserGroup_UPDATE_POST_URL + config.getAccessToken(),
					requestData);

			this.checkError(resultStr,config);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 移动用户分组
	 * 
	 * @param openid
	 *            用户的OpenID
	 * @param UserGroupId
	 *            分组id
	 * @throws WeChatException
	 */
	public void membersUpdateUserGroup(WxConfig config, String openId,
			int UserGroupId) throws WeChatException {
		Map<String, Object> jsonObject = new HashMap<String, Object>();
		jsonObject.put("openid", openId);
		jsonObject.put("to_UserGroupid", UserGroupId);
		String requestData = getGson().toJson(jsonObject);
		try {
			String resultStr = getWxHttpClient()
					.post(UserGroup_MEMBERS_UPDATE_POST_URL
							+ config.getAccessToken(), requestData);
			this.checkError(resultStr,config);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 批量移动用户分组
	 * 
	 * @param openids
	 *            用户唯一标识符openid的列表（size不能超过50）
	 * @param toUserGroupid
	 *            分组id
	 * @return 是否修改成功
	 * @throws WeChatException
	 */
	public void membersDatchUpdateUserGroup(WxConfig config, String[] openIds,
			int UserGroupId) throws WeChatException {
		Map<String, Object> jsonObject = new HashMap<String, Object>();
		jsonObject.put("openid_list", openIds);
		jsonObject.put("to_UserGroupid", UserGroupId);
		String requestData = getGson().toJson(jsonObject);
		try {
			String resultStr = getWxHttpClient().post(
					UserGroup_MEMBERS_DATCHUPDATE_POST_URL
							+ config.getAccessToken(), requestData);
			this.checkError(resultStr,config);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 删除分组
	 * 
	 * @param UserGroupId
	 * @throws WeChatException
	 */
	public void deleteUserGroup(WxConfig config, int UserGroupId)
			throws WeChatException {
		Map<String, Object> idJson = new HashMap<String, Object>();
		idJson.put("id", UserGroupId);
		Map<String, Object> UserGroupJson = new HashMap<String, Object>();
		UserGroupJson.put("UserGroup", idJson);
		String requestData = getGson().toJson(UserGroupJson);
		try {
			String resultStr = getWxHttpClient().post(
					UserGroup_DELETE_POST_URL + config.getAccessToken(),
					requestData);
			this.checkError(resultStr,config);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
