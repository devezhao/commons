package cn.devezhao.commons;

import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * @author zhaofang123@gmail.com
 * @since 02/09/2018
 */
public class ShortUrlTest {

	@Test
	public void testShortUrl() {
		String s = ShortUrl.shortUrl("https://www.baidu.com/");
		Assert.assertNotNull(s);
		System.out.println(s);
	}
}
