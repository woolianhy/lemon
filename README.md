
#微信SpringMVC开发

自制的基于maven的SpringMVC+hibernate微信公众号平台

1.基于多公众号配置

 lemon-serlvet.xml中 可以配置多个微信
 请见customerWxConfig bean
 配置Wxconfig对应的多个微信token获取线程和token中控器，请见customerTokenThreadRunnable customerTokenProxy

	
2.微信自动回复 （以customer公众号配置为例）

customer公众号自动回复入口为com.lemon.msg.customer.controller.CustomerMessageController 
，他的GET POST方法url在customer微信公众号开发者中心配置。
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


