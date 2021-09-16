package cn.devezhao.commons;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Feb 10, 2009
 * @version $Id: ByteUtils.java 8 2015-06-08 09:09:03Z zhaofang123@gmail.com $
 */
public class ByteUtils {

	private static final int BUFFER_SIZE = 1024;
	private static MessageDigest md5Digest = null;
	
	private ByteUtils() {
		try {
			md5Digest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	/**
	 * @param stream
	 * @return
	 * @throws IOException
	 */
	public static byte[] read(InputStream stream) throws IOException {
		List<byte[]> array = new ArrayList<>();
		byte[] buffer = new byte[BUFFER_SIZE * 100];
		int size = 0;
		
		int read;
		while ((read = stream.read(buffer)) > -1) {
			if (read > 0) {
				byte[] chunk = new byte[read];
				System.arraycopy(buffer, 0, chunk, 0, read);
				array.add(chunk);
				size += read;
			}
		}
		if (size <= 0) {
			throw new IllegalArgumentException("stream is empty");
		}

		try (ByteArrayOutputStream baos = new ByteArrayOutputStream(size)) {
			for (byte[] chunk : array) {
				baos.write(chunk);
			}
			return baos.toByteArray();
		}
	}
	
	/**
	 * @param reader
	 * @return
	 * @throws IOException
	 */
	public static String read(Reader reader) throws IOException {
		char[] buffer = new char[BUFFER_SIZE * 100];
		
		int read;
		StringBuilder content = new StringBuilder();
		while ((read = reader.read(buffer)) > 0) {
			char[] chunk = new char[read];
			System.arraycopy(buffer, 0, chunk, 0, read);
			content.append(chunk);
		}
		return content.toString();
	}
	
	/**
	 * @param stream
	 * @return
	 * @throws IOException
	 */
	public static long size(InputStream stream) throws IOException {
		byte[] buffer = new byte[BUFFER_SIZE * 100];
		long size = 0;
		int read;
		while ((read = stream.read(buffer)) > -1) {
			size += read;
		}
		return size;
	}
	
	/**
	 * @param reader
	 * @return
	 * @throws IOException
	 */
	public static long size(Reader reader) throws IOException {
		char[] buffer = new char[BUFFER_SIZE * 100];
		long size = 0;
		int read;
		while ((read = reader.read(buffer)) > -1) {
			size += read;
		}
		return size;
	}
	
	/**
	 * @param bytes
	 * @return
	 */
	public static int toInt(byte[] bytes) {
		int result = 0;
		for (int i = 0; i < 4; i++) {
			result = (result << 8) - Byte.MIN_VALUE + (int) bytes[i];
		}
		return result;
	}
	
	/**
	 * @param h
	 * @return
	 */
	public static int hash(int h) {
		h += ~(h << 9);
		h ^= (h >>> 14);
		h += (h << 4);
		h ^= (h >>> 10);
		return h;
	}
	
	/**
	 * @param bytes
	 * @return
	 */
	public static String toHex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			sb.append(String.format("%02x", b));
		}
		return sb.toString();
	}
	
	/**
	 * @param stream
	 * @return
	 * @throws IOException
	 */
	public static String hash(InputStream stream) throws IOException {
		md5Digest.reset();
		
		byte[] buffer = new byte[1024];
		int read;
		while ((read = stream.read(buffer)) > -1) {
			md5Digest.update(buffer, 0, read);
		}
		return toHex(md5Digest.digest());
	}
}
