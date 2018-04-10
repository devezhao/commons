package cn.devezhao.commons;

import org.apache.commons.lang.math.RandomUtils;
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
		for (int i = 0; i < 1; i++) {
			String s = ShortUrl.shortUrl("https://www.baidu.com/?s=" + RandomUtils.nextInt());
			Assert.assertNotNull(s);
			System.out.println(s);
		}
	}
	
	@Test
	public void testFt12Url() {
		String s = ShortUrl.ft12Url("https://www.baidu.com/?s=" + RandomUtils.nextInt());
		Assert.assertNotNull(s);
		System.out.println(s);
	}
}
