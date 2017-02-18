/*
 Copyright (C) 2009 QDSS.org
 
 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.
 
 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cn.devezhao.commons;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.zip.CRC32;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang.StringUtils;

/**
 * 加密工具类
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">Fangfang Zhao</a>
 * @version $Id: EncryptUtils.java 6 2015-06-08 08:56:34Z zhaoff@wisecrm.com $
 * @see CodecUtils
 */
public final class EncryptUtils {

	/**
	 * MD5加密
	 * 
	 * @param input
	 * @return
	 */
	public static byte[] toMD5(byte[] input) {
		MessageDigest digest = null;
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
	 * SHA1加密
	 * 
	 * @param input
	 * @return
	 */
	public static byte[] toSHA1(byte[] input) {
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("SHA1");
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
		return toHexString(toSHA1(input));
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
		if (crc32.length() == 8)
			return crc32;
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
	
	/**
	 * AES加密
	 * 
	 * @param input
	 * @param passwd
	 * @return
	 */
	public static String aesEncrypt(String input, byte[] passwd) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, new SecureRandom(passwd));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] result = cipher.doFinal(input.getBytes("UTF-8"));
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
	 */
	public static String aesDecrypt(String input, byte[] passwd) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, new SecureRandom(passwd));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] result = cipher.doFinal(CodecUtils.base64Decode(input.getBytes("UTF-8")));
			return new String(result);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private EncryptUtils() {}
	
	public static void main(String[] args) {
		String en = aesEncrypt("1823265756@qq.com#innobuddystudy521！", "02133558055".getBytes());
		System.out.println(en);
		System.out.println(aesDecrypt(en, "02133558055".getBytes()));
	}
}