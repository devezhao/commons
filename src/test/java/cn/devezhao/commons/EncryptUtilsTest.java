package cn.devezhao.commons;

import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

/**
 * 
 * @author zhaofang123@gmail.com
 * @since 09/22/2026
 */
public class EncryptUtilsTest {

	private static final byte[] PASSWD = "test-password-123".getBytes(StandardCharsets.UTF_8);

	@Test
	public void aesGcmRoundTrip() {
		String plain = "你好世界 Hello GCM";
		String cipher = EncryptUtils.aesEncryptGCM(plain, PASSWD);
		Assert.assertNotEquals(plain, cipher);
		Assert.assertEquals(plain, EncryptUtils.aesDecryptGCM(cipher, PASSWD));

		Assert.assertNotEquals(cipher, EncryptUtils.aesEncryptGCM(plain, PASSWD));
	}

	@Test
	public void aesGcmEmptyPlain() {
		String cipher = EncryptUtils.aesEncryptGCM("", PASSWD);
		Assert.assertEquals("", EncryptUtils.aesDecryptGCM(cipher, PASSWD));
	}

	@Test(expected = RuntimeException.class)
	public void aesGcmTampered() {
		String cipher = EncryptUtils.aesEncryptGCM("some data", PASSWD);
		char c = cipher.charAt(4);
		char replaced = (c == 'A') ? 'B' : 'A';
		EncryptUtils.aesDecryptGCM(cipher.substring(0, 4) + replaced + cipher.substring(5), PASSWD);
	}

	@Test(expected = RuntimeException.class)
	public void aesGcmWrongPasswd() {
		String cipher = EncryptUtils.aesEncryptGCM("some data", PASSWD);
		EncryptUtils.aesDecryptGCM(cipher, "another-passwd".getBytes(StandardCharsets.UTF_8));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void aesLegacyRoundTrip() {
		String plain = "你好 legacy";
		String cipher = EncryptUtils.aesEncrypt(plain, PASSWD);
		Assert.assertEquals(plain, EncryptUtils.aesDecrypt(cipher, PASSWD));
	}

	@Test
	public void digests() {
		Assert.assertEquals("900150983cd24fb0d6963f7d28e17f72", EncryptUtils.toMD5Hex("abc"));
		Assert.assertEquals("a9993e364706816aba3e25717850c26c9cd0d89d", EncryptUtils.toSHA1Hex("abc"));
		Assert.assertEquals("ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad",
				EncryptUtils.toSHA256Hex("abc"));
		Assert.assertEquals("352441c2", EncryptUtils.toCRC32Hex("abc".getBytes(StandardCharsets.UTF_8)));
		Assert.assertEquals("352441c2", EncryptUtils.toCRC32HexPadding("abc".getBytes(StandardCharsets.UTF_8)));
	}
}
