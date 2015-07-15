# lemon
柠檬微信SpringMVC

自制的基于maven的SpringMVC+hibernate微信平台，实现了对接图灵机器人的消息回复

微信和图灵机器人的配置文件在src/main/resources/properties/wx.properties中
图灵的key可以登录http://www.tuling123.com/openapi/cloud/home.jsp获取，免费的。
具体的实现在 com.lemon.base.util.HttpTuringRobot类中

ps.求不要用我的图灵key，有次数限制


后续会增加:
1.微信的AccessToken中控器
2.微信网页认证
3.微信支付
