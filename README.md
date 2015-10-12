# lemon
微信SpringMVC开发

自制的基于maven的SpringMVC+hibernate微信公众号平台

1.基于多公众号配置
 lemon-serlvet.xml中 可以配置多个微信配置，com.lemon.base.config.WxConfigImpl
 <!-- 公众号配置，可以配置多个 -->
 <!-- customer公众号配置 -->
	<bean id="customerWxConfig" class="com.lemon.base.config.WxConfigImpl">
		<constructor-arg name="appId" value="wx8181e822a15cdc22"></constructor-arg>
		<constructor-arg name="appsecret" value="6df6fa84c1c360b799928c893e8ac799"></constructor-arg>
		<constructor-arg name="mch_id" value=""></constructor-arg>
		<constructor-arg name="wxzf_key" value=""></constructor-arg>
		<constructor-arg name="notify_url" value=""></constructor-arg>
		<constructor-arg name="token" value="dsaREf3434654fGDShdftgh657ku"></constructor-arg>
		<constructor-arg name="subsribeReplyText" value=""></constructor-arg>
		<property name="tokenProxy" ref="customerTokenProxy"></property>
	</bean>
	
	配置对应的多个微信token获取线程和token代理
	<!-- 公众号token中控器，需要注入微信配置 -->
	<bean id="customerTokenThreadRunnable" class="com.lemon.base.thread.TokenThreadRunnable">
		<constructor-arg name="wxConfig" ref="customerWxConfig"></constructor-arg>
	</bean>

	<!-- 公众号token代理，可以使用代理获取token -->
	<bean id="customerTokenProxy" class="com.lemon.base.util.TokenProxyImpl"
		init-method="init">
		<constructor-arg name="tokenThreadRunnable" ref="customerTokenThreadRunnable"></constructor-arg>
	</bean>
	
2.微信自动回复 以customer公众号配置为例
customer公众号自动回复入口为com.lemon.msg.customer.controller.CustomerMessageController ，他的GET POST方法url在customer微信公众号开发者中心配置。
CustomerMessageController类中MessageHandle这个类需要注入微信配置信息WxConfig
MessageHandle类的继承类CustomerRobotMessageHandle为customer公众号的图灵机器人自动回复处理类

3.token中控器
com.lemon.base.util.TokenProxyImpl 为通用token中控器。将需要的WxConfig注入com.lemon.base.thread.TokenThreadRunnable，
再讲这个Runnable植入TokenProxyImpl即可获得对应WxConfig的token中控器

4.获取微信用户信息
 com.lemon.customer.controller.OauthCustomerController注入了customer的Wxconfig，会拦截一切oauth2开头的uri，并按照微信api方式跳转，获取用户信息。详情请见代码和微信api。
 
 5.其他各类微信api实现，请见com.lemon.base.http.wx.manager包。
 可以在自己的controller中使用这些api，如com.lemon.web.controller.CustomerTestController类
 
 .....其他功能或者文档尚未完善，有待自行发掘


