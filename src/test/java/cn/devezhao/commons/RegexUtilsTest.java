package cn.devezhao.commons;

import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * @author zhaofang123@gmail.com
 * @since 01/11/2019
 */
public class RegexUtilsTest {

	@Test
	public void testTel() throws Exception {
		Assert.assertTrue(RegexUtils.isTel("1234567"));
		Assert.assertFalse(RegexUtils.isTel("123456"));
		Assert.assertTrue(RegexUtils.isTel("11-2345-6"));
		Assert.assertFalse(RegexUtils.isTel("11-2345--6"));
	}
	
	@Test
	public void testCNMobile() throws Exception {
		Assert.assertFalse(RegexUtils.isCNMobile("123"));
		Assert.assertTrue(RegexUtils.isCNMobile("13761512988"));
		Assert.assertTrue(RegexUtils.isCNMobile("+8613761512988"));
		Assert.assertTrue(RegexUtils.isCNMobile("8613761512988"));
		Assert.assertFalse(RegexUtils.isCNMobile("12761512988"));
		Assert.assertFalse(RegexUtils.isCNMobile("1387689099"));
	}

	@Test
	public void testIsValid() throws Exception {
		Assert.assertTrue(RegexUtils.isValidEmail("zhaofang123@gmail.com"));
		Assert.assertFalse(RegexUtils.isValidEmail("invalid-email"));
		Assert.assertTrue(RegexUtils.isValidUrl("https://github.com/devezhao/commons"));
		Assert.assertTrue(RegexUtils.isValidCNMobile("13761512988"));
		Assert.assertFalse(RegexUtils.isValidCNMobile("123"));
		Assert.assertTrue(RegexUtils.isValidTel("1234567"));
		Assert.assertFalse(RegexUtils.isValidTel("123456"));
	}
}
