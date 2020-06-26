package cn.devezhao.commons;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池工具
 * 
 * @author Zhao Fangfang
 * @version $Id: Bean2Json.java 48 2015-08-18 02:57:54Z zhaofang123@gmail.com $
 */
public final class ThreadPool {
	
	private static final ExecutorService EXEC = Executors.newCachedThreadPool();
	static {
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run() {
				super.run();
				EXEC.shutdownNow();
			}
		});
	}

	/**
	 * 执行任务
	 * 
	 * @param task
	 */
	public static void exec(Runnable task) {
		EXEC.execute(task);
	}
	
	/**
	 * 提交任务
	 * 
	 * @param task
	 */
	public static void submit(Runnable task) {
		EXEC.submit(task);
	}
	
	/**
	 * 固定并堵塞的线程池
	 * 
	 * @param nThreads
	 * @return
	 * @see Executors#newFixedThreadPool(int)
	 */
	@Deprecated
	public static ExecutorService newFixedThreadPool(int nThreads) {
		ThreadPoolExecutor exec = new ThreadPoolExecutor(nThreads, nThreads,
                                      0L, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<>(nThreads));
		exec.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		return exec;
    }
	
	/**
	 * @param exec
	 * @param checkInterval
	 */
	public static void checkblockPoolCompleted(ThreadPoolExecutor exec, int checkInterval) {
		checkblockPoolCompleted(exec, checkInterval, false);
	}
	
	/**
	 * @param exec
	 * @param checkInterval
	 * @param shutdown
	 */
	public static void checkblockPoolCompleted(ThreadPoolExecutor exec, int checkInterval, boolean shutdown) {
		while (true) {
			int activite = exec.getActiveCount();
			waitFor(checkInterval);
			if (activite == 0) {
				break;
			}
		}
		
		if (shutdown) {
			exec.shutdown();
			try {
				exec.awaitTermination(60, TimeUnit.SECONDS);
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
		}
	}
	
	/**
	 * 线程等待
	 * 
	 * @param millis
	 */
	public static void waitFor(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
	
	private ThreadPool() {}
}
