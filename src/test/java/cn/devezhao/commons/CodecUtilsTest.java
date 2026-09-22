package cn.devezhao.commons;

import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

public class CodecUtilsTest {
	
	@Test
	public void unicode() {
		String data = "你好世界";
		String unicode = CodecUtils.encodeUnicode(data);
		System.out.println(unicode);
		Assert.assertEquals(data, CodecUtils.decodeUnicode(unicode));

		System.out.println(StandardCharsets.UTF_8.name());
	}

	@Test
	public void base64() {
		String data = "你好世界 commons";
		String jdkEnc = java.util.Base64.getEncoder().encodeToString(
				data.getBytes(StandardCharsets.UTF_8));
		Assert.assertEquals(jdkEnc, CodecUtils.base64Encode(data));
		Assert.assertEquals(data, CodecUtils.base64Decode(jdkEnc));
		Assert.assertEquals(data, CodecUtils.base64UrlDecode(CodecUtils.base64UrlEncode(data)));
	}

	@Test(expected = IllegalArgumentException.class)
	public void base64InvalidChar() {
		CodecUtils.base64Decode("!!!!".getBytes(StandardCharsets.UTF_8));
	}

	@Test(expected = IllegalArgumentException.class)
	public void base64InsufficientData() {
		CodecUtils.base64Decode("abc".getBytes(StandardCharsets.UTF_8));
	}
}
