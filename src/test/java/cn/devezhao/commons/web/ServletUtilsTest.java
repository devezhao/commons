package cn.devezhao.commons.web;

import javax.servlet.http.HttpServletRequest;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * 
 * @author zhaofang123@gmail.com
 */
public class ServletUtilsTest {

	@Test
	public void getScheme() {
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		
		Mockito.when(request.getScheme()).thenReturn("https");
		String ret1 = ServletUtils.getScheme(request);
		Assert.assertEquals("https", ret1);
		
		Mockito.when(request.getHeader("x-forwarded-proto")).thenReturn("https");
		String ret2 = ServletUtils.getScheme(request);
		Assert.assertEquals("https", ret2);
	}
}
