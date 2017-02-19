package cn.devezhao.commons.runtime;

import java.lang.management.MemoryPoolMXBean;

/**
 * @author Zhao Fangfang
 * @version $Id: MemoryInformationBean.java 78 2012-01-17 08:05:10Z zhaofang123@gmail.com $
 */
public class MemoryInformationBean implements MemoryInformation {

	public static final long MEGABYTES = 1048576L;
	
	private final MemoryPoolMXBean memoryPool;

	/**
	 * @param memoryPoolMXBean
	 */
	public MemoryInformationBean(MemoryPoolMXBean memoryPoolMXBean) {
		this.memoryPool = memoryPoolMXBean;
	}

	public String getName() {
		return memoryPool.getName();
	}

	public long getTotal() {
		return (memoryPool.getUsage().getMax());
	}

	public long getUsed() {
		return (memoryPool.getUsage().getUsed());
	}

	public long getFree() {
		return getTotal() - getUsed();
	}

	@Override
	public String toString() {
		return getName() + ": " + memoryPool.getUsage();
	}
}
