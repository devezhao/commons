package cn.devezhao.commons.http4;

import org.junit.Test;

/**
 * 
 * @author zhaofang123@gmail.com
 * @since 05/03/2017
 */
public class HttpClientExTest {

	@Test
	public void testGet() {
		String r = new HttpClientEx().post("https://api.mysubmail.com/message/xsend.json", "{}");
		System.out.println(r);
	}
}
