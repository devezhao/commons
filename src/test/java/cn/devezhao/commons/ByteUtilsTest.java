package cn.devezhao.commons;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author zhaofang123@gmail.com
 * @since 11/06/2018
 */
public class ByteUtilsTest {

	@Test
	public void testIntHash() {
		Set<Integer> set = new HashSet<>();
		for (int i = 0; i < 999999; i++) {
			int hash = ByteUtils.hash(i);
			set.add(hash);
			System.out.println(i + " > " + hash);
		}
		Assert.assertTrue(set.size() == 999999);
	}
}
