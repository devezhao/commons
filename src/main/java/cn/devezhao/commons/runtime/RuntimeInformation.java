package cn.devezhao.commons.runtime;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.RuntimeMXBean;
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

	/**
	 */
	public RuntimeInformation() {
		this.memory = ManagementFactory.getMemoryMXBean();
		this.runtime = ManagementFactory.getRuntimeMXBean();
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
			public String getName() { return ""; }
			public long getTotal() { return -1; }
			public long getUsed() { return -1; }
			public long getFree() { return -1; }
		};
	}
}
