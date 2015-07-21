package lemon;

import java.io.IOException;

import org.junit.Test;

import com.lemon.base.http.MyHttpClient;

public class HttpTest {

	@Test
	public void testGetString() {
		
		try {
			MyHttpClient c=new MyHttpClient();
//			HttpTuringRobot t=new HttpTuringRobot();
//			t.setTuringKey("f8c3ed6e9d831d56797a3c82ebffaf7f");
//			t.setTuringUrl("http://www.tuling123.com/openapi/api");
//			t.setWxHttpClient(c);
//			t.setGson(new Gson());
//			BaseMsg answer = t.answerMsg("机器人的图片", "11111", null);
//			System.out.println(answer);
			System.out.println(c.get("https://www.baidu.com"));
			
			
//			String html=c.get("http://www.tuling123.com/openapi/");
//			System.out.println(html);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
