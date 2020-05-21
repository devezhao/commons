package cn.devezhao.commons;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;

import cn.devezhao.commons.runtime.MemoryInformation;
import cn.devezhao.commons.runtime.RuntimeInformation;

/**
 * JVM 系统工具
 * 
 * @author Zhao Fangfang
 * @version $Id: Bean2Json.java 48 2015-08-18 02:57:54Z zhaofang123@gmail.com $
 */
public final class SystemUtils {

	/**
	 * 执行命令行
	 * 
	 * @param command
	 */
	public static void command(String command) {
		command(command, System.out);
	}

	/**
	 * 执行命令行
	 * 
	 * @param command
	 * @param stream
	 * @return
	 */
	public static void command(String command, PrintStream stream) {
		if (StringUtils.isBlank(command)) {
			return;
		}
		
		BufferedReader reader = null;
		try {
			Runtime rtx = Runtime.getRuntime();
			Process process = rtx.exec(command);
			reader = new BufferedReader(new InputStreamReader( process.getInputStream() ));
			if (stream != null) {
				String str;
				while ((str = reader.readLine()) != null) {
					stream.println(str);
				}
			}
			
			try {
				System.out.println("Exit: " + process.waitFor());
			} catch (Exception ex) {
				System.err.println("Processes was interrupted");
				throw ex;
			}
			
		} catch (Exception ex) {
			if (!command.startsWith("cmd /c")) {
				command("cmd /c " + command);
			} else {
				throw new RuntimeException("Error executing commond: " + command, ex);
			}
		} finally {
			try {
				reader.close();
			} catch (Exception ex) {}
		}
	}
	
	private static long lastGcTime = 0;
	/**
	 * 执行 GC
	 */
	synchronized
	public static void gc() {
		if (System.currentTimeMillis() - lastGcTime < 60 * 1000) {
			return;
		}
		lastGcTime = System.currentTimeMillis();
		
		try {
			 System.gc();
			 TimeUnit.SECONDS.sleep(30);
	         System.runFinalization();
	         TimeUnit.SECONDS.sleep(30);
	         
	         System.gc();
	         TimeUnit.SECONDS.sleep(30);
	         System.runFinalization();
	         TimeUnit.SECONDS.sleep(30);
	         
		} catch (Exception ex) {
			//...
		} finally {
			lastGcTime = System.currentTimeMillis();
		}
	}
	
	/**
	 * 获取当前线程总数
	 * 
	 * @return
	 */
	public static long getThreadCount() {
		ThreadGroup group = Thread.currentThread().getThreadGroup();
		ThreadGroup top = group;
		while (group != null) {
			top = group;
			group = group.getParent();
		}
		
		int estimated_size = top.activeCount() * 2;
		Thread[] slackList = new Thread[estimated_size];
		int actual_size = top.enumerate(slackList);
		return actual_size;
	}
	
	private static RuntimeInformation runtimeInformation;
	/**
	 * 获取运行时信息
	 * 
	 * @return
	 */
	synchronized
	public static RuntimeInformation getRuntimeInformation() {
		if (runtimeInformation == null) {
			if (!org.apache.commons.lang.SystemUtils.isJavaVersionAtLeast(1.5f)) {
				throw new RuntimeException("Java version least 1.5");
			}
			runtimeInformation = new RuntimeInformation();
		}
		return runtimeInformation;
	}

	/**
	 * 内存统计（单位：MB）
	 * 
	 * @return
	 */
	public static List<MemoryInformation> getMemoryStatistics() {
		return getMemoryStatistics(false);
	}
	
	/**
	 * 获取内存统计
	 * 
	 * @return
	 */
	public static List<MemoryInformation> getMemoryStatistics(boolean containsPool) {
		List<MemoryInformation> list = new ArrayList<MemoryInformation>();
		
		final RuntimeInformation memory = new RuntimeInformation();
		list.add(new MemoryInformation() {
			@Override
            public String getName() { return "Heap"; }
			@Override
            public long getUsed() { return memory.getTotalHeapMemoryUsed(); }
			@Override
            public long getTotal() { return memory.getTotalHeapMemory(); }
			@Override
            public long getFree() { return getTotal() - getUsed(); }
		});
		list.add(new MemoryInformation() {
			@Override
            public String getName() { return "PermGen"; }
			@Override
            public long getUsed() { return memory.getTotalPermGenMemoryUsed(); }
			@Override
            public long getTotal() { return memory.getTotalPermGenMemory(); }
			@Override
            public long getFree() { return getTotal() - getUsed(); }
		});
		list.add(new MemoryInformation() {
			@Override
            public String getName() { return "NonHeap"; }
			@Override
            public long getUsed() { return memory.getTotalNonHeapMemoryUsed(); }
			@Override
            public long getTotal() { return memory.getTotalNonHeapMemory(); }
			@Override
            public long getFree() { return getTotal() - getUsed(); }
		});
		
		if (containsPool) {
			list.addAll(memory.getMemoryPoolInformation());
		}
		return Collections.unmodifiableList(list);
	}
	
	/**
	 * DUMP 线程信息
	 * 
	 * @param stream
	 */
    public static void dumpThreads(PrintStream stream) {
        Map<Thread, StackTraceElement[]> traces = Thread.getAllStackTraces();
        for (Thread thread : traces.keySet()) {
            stream.print(String.format("\nThread[%s@%d,%d,%s]: (state = %s)",
                    thread.getName(), thread.getId(), thread.getPriority(),
                    thread.getThreadGroup().getName(), thread.getState()));
            for (StackTraceElement stackTraceElement : traces.get(thread)) {
                stream.print("\n\t" + stackTraceElement);
            }
        }
    }
    
    private SystemUtils() {}
}
