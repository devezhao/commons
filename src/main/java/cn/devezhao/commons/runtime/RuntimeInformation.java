package cn.devezhao.commons.runtime;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 运行时信息
 * 
 * @author Zhao Fangfang
 * @version $Id: RuntimeInformation.java 78 2012-01-17 08:05:10Z zhaofang123@gmail.com $
 */
public class RuntimeInformation {

	private final MemoryMXBean memory;
	private final RuntimeMXBean runtime;
	private final OperatingSystemMXBean osMXBean;
	private final ThreadMXBean threadMXBean;

	private long preTime = System.nanoTime();
	private long preUsedTime = 0;

	/**
	 */
	public RuntimeInformation() {
		this.memory = ManagementFactory.getMemoryMXBean();
		this.runtime = ManagementFactory.getRuntimeMXBean();
		this.osMXBean = ManagementFactory.getOperatingSystemMXBean();
		this.threadMXBean = ManagementFactory.getThreadMXBean();
	}

	/**
	 * @return
	 */
	public long getTotalHeapMemory() {
		return (memory.getHeapMemoryUsage().getMax());
	}

	/**
	 * @return
	 */
	public long getTotalHeapMemoryUsed() {
		return (memory.getHeapMemoryUsage().getUsed());
	}

	/**
	 * @return
	 */
	public long getTotalNonHeapMemory() {
		return (memory.getNonHeapMemoryUsage().getMax());
	}

	/**
	 * @return
	 */
	public long getTotalNonHeapMemoryUsed() {
		return (memory.getNonHeapMemoryUsage().getUsed());
	}

	/**
	 * @return
	 */
	public long getTotalPermGenMemory() {
		return (getPermGen().getTotal());
	}

	/**
	 * @return
	 */
	public long getTotalPermGenMemoryUsed() {
		return (getPermGen().getUsed());
	}

	/**
	 * @return
	 */
	public List<MemoryInformation> getMemoryPoolInformation() {
		List<MemoryPoolMXBean> mxBeans = ManagementFactory.getMemoryPoolMXBeans();
		List<MemoryInformation> result = new ArrayList<MemoryInformation>(mxBeans.size());
		for (MemoryPoolMXBean mxBean : mxBeans) {
			result.add(new MemoryInformationBean(mxBean));
		}
		return Collections.unmodifiableList(result);
	}

	/**
	 * 获取 JVM 输入参数
	 *
	 * @return
	 */
	public String getJvmInputArguments() {
		final StringBuilder sb = new StringBuilder();
		for (final String argument : runtime.getInputArguments()) {
			sb.append(argument).append(" ");
		}
		return sb.toString();
	}

	/**
	 * 获取永久代内存
	 *
	 * @return
	 */
	private MemoryInformation getPermGen() {
		for (final MemoryInformation info : getMemoryPoolInformation()) {
			final String name = info.getName().toLowerCase();
			if (name.contains("perm gen")) {
				return info;
			}
		}
		return new MemoryInformation() {
			@Override
            public String getName() { return ""; }
			@Override
            public long getTotal() { return -1; }
			@Override
            public long getUsed() { return -1; }
			@Override
            public long getFree() { return -1; }
		};
	}

	/**
	 * 获取系统负载
	 *
	 * @return
	 */
	public double getSystemLoad() {
		return osMXBean.getSystemLoadAverage();
	}

	/**
	 * 获取 JVM 进程负载
	 *
	 * @return
	 */
	public double getProcessLoad() {
		long totalTime = 0;
		for (long id : threadMXBean.getAllThreadIds()) {
			totalTime += threadMXBean.getThreadCpuTime(id);
		}

		long currentTime = System.nanoTime();
		long usedTime = totalTime - preUsedTime;
		long totalPassedTime = currentTime - preTime;
		preTime = currentTime;
		preUsedTime = totalTime;
		return (((double) usedTime) / totalPassedTime / osMXBean.getAvailableProcessors()) * 100;
	}
}
