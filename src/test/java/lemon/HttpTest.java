package lemon;

import java.io.IOException;

import org.junit.Test;

import com.google.gson.Gson;
import com.lemon.base.util.HttpTuringRobot;
import com.lemon.base.util.WxHttpClient;
import com.lemon.msg.bean.BaseMsg;

public class HttpTest {

	@Test
	public void testGetString() {
		
		try {
			WxHttpClient c=new WxHttpClient();
			HttpTuringRobot t=new HttpTuringRobot();
			t.setTuringKey("f8c3ed6e9d831d56797a3c82ebffaf7f");
			t.setTuringUrl("http://www.tuling123.com/openapi/api");
			t.setWxHttpClient(c);
			t.setGson(new Gson());
			BaseMsg answer = t.answerMsg("机器人的图片", "11111", null);
			System.out.println(answer);
			
			
//			String html=c.get("http://www.tuling123.com/openapi/");
//			System.out.println(html);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
