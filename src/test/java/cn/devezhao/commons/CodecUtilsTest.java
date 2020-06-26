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
}
