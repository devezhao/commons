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
		String r = new HttpClientEx().get("https://www.baidu.com/");
		System.out.println(r);
	}
}
