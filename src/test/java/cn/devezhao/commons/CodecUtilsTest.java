package cn.devezhao.commons;

import org.junit.Assert;
import org.junit.Test;

public class CodecUtilsTest {
	
	@Test
	public void unicode() throws Exception {
		String data = "你好世界";
		String unicode = CodecUtils.encodeUnicode(data);
		System.out.println(unicode);
		Assert.assertEquals(data, CodecUtils.decodeUnicode(unicode));
	}

}
