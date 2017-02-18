/*
 Copyright (C) 2010 QDSS.org
 
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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池工具
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">Zhao Fangfang</a>
 * @version $Id: ThreadPool.java 6 2015-06-08 08:56:34Z zhaoff@wisecrm.com $
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
                                      new LinkedBlockingQueue<Runnable>(nThreads));
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
