package cn.devezhao.commons;

import cn.devezhao.commons.sql.Builder;
import org.junit.Test;

import java.util.Set;

public class ReflectUtilsTest {

	@Test
	public void getAllSubclasses() throws Exception {
		Set<Class<?>> classes = ReflectUtils.getAllSubclasses("cn.devezhao.commons.sql", null);
		for (Class<?> c : classes) System.out.println(c);
		
		classes = ReflectUtils.getAllSubclasses("cn.devezhao.commons.sql", Builder.class);
		System.out.println();
		for (Class<?> c : classes) System.out.println(c);
	}
}
