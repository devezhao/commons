package cn.devezhao.commons;

import org.apache.commons.lang.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.zip.CRC32;

/**
 * 加密工具类
 * 
 * @author Zhao Fangfang
 * @version $Id: Bean2Json.java 48 2015-08-18 02:57:54Z zhaofang123@gmail.com $
 * @see CodecUtils
 */
public final class EncryptUtils {

	/**
	 * MD5加密
	 * 
	 * @param input
	 * @return
	 */
	private static byte[] toMD5(byte[] input) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		digest.update(input);
		return digest.digest();
	}
	
	/**
	 * MD5加密
	 * 
	 * @param input
	 * @return
	 */
	public static String toMD5Hex(byte[] input) {
		return toHexString(toMD5(input));
	}

	/**
	 * MD5加密
	 * 
	 * @param input
	 * @return
	 */
	public static String toMD5Hex(String input) {
		return toMD5Hex(input.getBytes(StandardCharsets.UTF_8));
	}
	
	/**
	 * SHA1加密
	 * 
	 * @param input
	 * @return
	 */
	private static byte[] toSHA(byte[] input, String algorithm) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		digest.update(input);
		return digest.digest();
	}
	
	/**
	 * SHA1加密
	 * 
	 * @param input
	 * @return
	 */
	public static String toSHA1Hex(byte[] input) {
		return toHexString(toSHA(input, "SHA1"));
	}

	/**
	 * SHA1加密
	 * 
	 * @param input
	 * @return
	 */
	public static String toSHA1Hex(String input) {
		return toSHA1Hex(input.getBytes(StandardCharsets.UTF_8));
	}
	
	/**
	 * SHA-256加密
	 * 
	 * @param input
	 * @return
	 */
	public static String toSHA256Hex(byte[] input) {
		return toHexString(toSHA(input, "SHA-256"));
	}

	/**
	 * SHA-256加密
	 * 
	 * @param input
	 * @return
	 */
	public static String toSHA256Hex(String input) {
		return toSHA256Hex(input.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * CRC32编码（6-8位）
	 * 
	 * @param input
	 * @return
	 */
	public static String toCRC32Hex(byte[] input) {
		CRC32 crc32 = new CRC32();
		crc32.update(input);
		return Long.toHexString( crc32.getValue() );
	}
	
	/**
	 * 返回8位CRC32编码，不够位数在尾部补0
	 * 
	 * @param input
	 * @return
	 */
	@Deprecated
	public static String toCRC32HexPad(byte[] input) {
		return toCRC32HexPadding(input);
	}
	
	/**
	 * 返回8位CRC32编码，不够位数在尾部补0
	 * 
	 * @param input
	 * @return
	 */
	public static String toCRC32HexPadding(byte[] input) {
		String crc32 = toCRC32Hex(input);
		if (crc32.length() == 8) return crc32;
		return StringUtils.rightPad(crc32, 8, '0');
	}

	/**
	 * @param data
	 * @return
	 */
	public static String toHexString(byte[] data) {
		StringBuilder sb = new StringBuilder();
		for (byte b : data) {
			sb.append(String.format("%02x", b));
		}
		return sb.toString();
	}
	
	private static final SecureRandom SECURE_RANDOM = new SecureRandom();

	private static final int GCM_IV_LENGTH = 12;
	private static final int GCM_SALT_LENGTH = 16;
	private static final int GCM_TAG_BITS = 128;
	private static final int PBKDF2_ITERATIONS = 65536;

	/**
	 * AES加密
	 *
	 * @param input
	 * @param passwd
	 * @return
	 * @deprecated ECB 模式（相同明文产生相同密文）且密钥派生非标准，仅用于兼容存量密文，新代码请使用 {@link #aesEncryptGCM(String, byte[])}
	 */
	@Deprecated
	public static String aesEncrypt(String input, byte[] passwd) {
		try {
			SecretKeySpec key = buildAesSecretKey(passwd);
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, key);

			byte[] result = cipher.doFinal(input.getBytes(StandardCharsets.UTF_8));
			return new String(CodecUtils.base64Encode(result));

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * AES解密
	 *
	 * @param input
	 * @param passwd
	 * @return
	 * @deprecated 仅用于兼容 {@link #aesEncrypt(String, byte[])} 的存量密文，新代码请使用 {@link #aesDecryptGCM(String, byte[])}
	 */
	@Deprecated
	public static String aesDecrypt(String input, byte[] passwd) {
		try {
			SecretKeySpec key = buildAesSecretKey(passwd);
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, key);

			byte[] result = cipher.doFinal(CodecUtils.base64Decode(input.getBytes(StandardCharsets.UTF_8)));
			return new String(result, StandardCharsets.UTF_8);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @param passwd
	 * @return
	 */
	private static SecretKeySpec buildAesSecretKey(byte[] passwd) {
		try {
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			random.setSeed(passwd);
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, random);
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			return new SecretKeySpec(enCodeFormat, "AES");

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * AES-GCM 加密（与 {@link #aesEncrypt(String, byte[])} 输出不兼容）
	 *
	 * 密钥使用 PBKDF2WithHmacSHA256 从口令派生，每次加密生成随机盐与随机 IV，密文输出格式：Base64( IV[12] + Salt[16] + 密文+认证标签 )
	 *
	 * @param input 明文
	 * @param passwd 口令
	 * @return Base64 编码的密文
	 */
	public static String aesEncryptGCM(String input, byte[] passwd) {
		try {
			byte[] iv = new byte[GCM_IV_LENGTH];
			SECURE_RANDOM.nextBytes(iv);

			byte[] salt = new byte[GCM_SALT_LENGTH];
			SECURE_RANDOM.nextBytes(salt);

			SecretKeySpec key = buildAesGcmSecretKey(passwd, salt);
			Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
			cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(GCM_TAG_BITS, iv));

			byte[] ciphertext = cipher.doFinal(input.getBytes(StandardCharsets.UTF_8));

			byte[] result = new byte[GCM_IV_LENGTH + GCM_SALT_LENGTH + ciphertext.length];
			System.arraycopy(iv, 0, result, 0, GCM_IV_LENGTH);
			System.arraycopy(salt, 0, result, GCM_IV_LENGTH, GCM_SALT_LENGTH);
			System.arraycopy(ciphertext, 0, result, GCM_IV_LENGTH + GCM_SALT_LENGTH, ciphertext.length);
			return new String(CodecUtils.base64Encode(result), StandardCharsets.UTF_8);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * AES-GCM 解密，密文格式见 {@link #aesEncryptGCM(String, byte[])}，密文被篡改或口令错误时抛出异常
	 *
	 * @param input Base64 编码的密文
	 * @param passwd 口令
	 * @return 明文
	 */
	public static String aesDecryptGCM(String input, byte[] passwd) {
		try {
			byte[] data = CodecUtils.base64Decode(input.getBytes(StandardCharsets.UTF_8));
			if (data.length < GCM_IV_LENGTH + GCM_SALT_LENGTH + GCM_TAG_BITS / 8) {
				throw new IllegalArgumentException("Invalid AES-GCM ciphertext");
			}

			byte[] iv = new byte[GCM_IV_LENGTH];
			System.arraycopy(data, 0, iv, 0, GCM_IV_LENGTH);

			byte[] salt = new byte[GCM_SALT_LENGTH];
			System.arraycopy(data, GCM_IV_LENGTH, salt, 0, GCM_SALT_LENGTH);

			byte[] ciphertext = new byte[data.length - GCM_IV_LENGTH - GCM_SALT_LENGTH];
			System.arraycopy(data, GCM_IV_LENGTH + GCM_SALT_LENGTH, ciphertext, 0, ciphertext.length);

			SecretKeySpec key = buildAesGcmSecretKey(passwd, salt);
			Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
			cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(GCM_TAG_BITS, iv));

			byte[] result = cipher.doFinal(ciphertext);
			return new String(result, StandardCharsets.UTF_8);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 使用 PBKDF2WithHmacSHA256 从口令与盐派生 AES 密钥（128 位）
	 *
	 * @param passwd
	 * @param salt
	 * @return
	 */
	private static SecretKeySpec buildAesGcmSecretKey(byte[] passwd, byte[] salt) throws GeneralSecurityException {
		PBEKeySpec spec = new PBEKeySpec(
				new String(passwd, StandardCharsets.UTF_8).toCharArray(), salt, PBKDF2_ITERATIONS, 128);
		try {
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			SecretKey secretKey = factory.generateSecret(spec);
			return new SecretKeySpec(secretKey.getEncoded(), "AES");
		} finally {
			spec.clearPassword();
		}
	}
	
	private EncryptUtils() {}
}