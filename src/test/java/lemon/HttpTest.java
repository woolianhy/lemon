package lemon;

import java.io.IOException;

import org.junit.Test;

import com.lemon.base.http.MyHttpClient;
import com.lemon.msg.util.MessageUtil;

public class HttpTest {

	@Test
	public void testGetString() {
		
		try {
//			MyHttpClient c=new MyHttpClient();
//			HttpTuringRobot t=new HttpTuringRobot();
//			t.setTuringKey("f8c3ed6e9d831d56797a3c82ebffaf7f");
//			t.setTuringUrl("http://www.tuling123.com/openapi/api");
//			t.setWxHttpClient(c);
//			t.setGson(new Gson());
//			BaseMsg answer = t.answerMsg("机器人的图片", "11111", null);
//			System.out.println(answer);
//			System.out.println(c.get("https://www.baidu.com"));
			
			
//			String html=c.get("http://www.tuling123.com/openapi/");
//			System.out.println(html);
			String xml="<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg><appid><![CDATA[wx783501b9f0d58ed4]]></appid><mch_id><![CDATA[1233512102]]></mch_id><nonce_str><![CDATA[aeoPJNZomPbZKX3l]]></nonce_str><sign><![CDATA[2F693ECBED9D5DB9617968F6F046DFA5]]></sign><result_code><![CDATA[SUCCESS]]></result_code><prepay_id><![CDATA[wx201507241439096d985ba2b10227466858]]></prepay_id><trade_type><![CDATA[JSAPI]]></trade_type></xml>";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
