package cn.devezhao.commons.identifier;

import cn.devezhao.commons.ByteUtils;

import java.io.Serializable;
import java.net.InetAddress;

/**
 * UUID Hex Generator
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, 06/13/08
 * @version $Id: UUIDHexGenerator.java 8 2015-06-08 09:09:03Z zhaoff@wisecrm.com $
 */
public class UUIDHexGenerator implements IdentifierGenerator {

	private static final int IP;
	static {
		int ipadd;
		try {
			ipadd = ByteUtils.toInt(InetAddress.getLocalHost().getAddress());
		} catch (Exception ex) {
			ipadd = 0;
		}
		IP = ipadd;
	}

	private static short counter = (short) 0;
	final private static int JVM = (int) (System.currentTimeMillis() >>> 8);

	final protected char sep;

	/**
	 * Create a new UUIDHexGenerator
	 */
	public UUIDHexGenerator() {
		this('-');
	}

	/**
	 * Create a new UUIDHexGenerator
	 * 
	 * @param sep
	 */
	public UUIDHexGenerator(char sep) {
		this.sep = sep;
	}

	/**
	 * Generate uuid hex
	 * 
	 * @return
	 */
	@Override
    public Serializable generate() {
		return format(getIP()) + sep +
				format(getJVM()) + sep +
				format(getMoTime()) + sep +
				format(getLoTime()) + sep +
				format(getCount());
	}
	
	@Override
    public int getLength() {
		return 36;
	}

	/**
	 * Format a int value
	 */
	protected String format(int intval) {
		String formatted = Integer.toHexString(intval);
		StringBuilder buf = new StringBuilder("00000000");
		buf.replace(8 - formatted.length(), 8, formatted);
		return buf.toString();
	}

	/**
	 * Format a short value
	 */
	protected String format(short shortval) {
		String formatted = Integer.toHexString(shortval);
		StringBuilder buf = new StringBuilder("0000");
		buf.replace(4 - formatted.length(), 4, formatted);
		return buf.toString();
	}

	/**
	 * Unique across JVMs on this machine (unless they load this class in the
	 * same quater second - very unlikely)
	 */
	protected int getJVM() {
		return JVM;
	}

	/**
	 * Unique in a millisecond for this JVM instance (unless there are &gt;
	 * Short.MAX_VALUE instances created in a millisecond)
	 */
	protected short getCount() {
		synchronized (UUIDHexGenerator.class) {
			if (counter < 0) {
				counter = 0;
			}
			return counter++;
		}
	}

	/**
	 * Unique in a local network
	 */
	protected int getIP() {
		return IP;
	}

	/**
	 * Unique down to month
	 */
	protected short getMoTime() {
		return (short) (System.currentTimeMillis() >>> 32);
	}

	/**
	 * Unique down to millisecond
	 */
	protected int getLoTime() {
		return (int) System.currentTimeMillis();
	}
}
