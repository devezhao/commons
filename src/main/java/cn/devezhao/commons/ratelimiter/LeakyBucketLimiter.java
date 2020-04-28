package cn.devezhao.commons.ratelimiter;

import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * 漏桶算法
 *
 * @author ZHAO
 * @author Tian ZhongBo
 * @date 2019-05-09 22:07:05
 */
public class LeakyBucketLimiter implements RateLimiter {

	private BlockingQueue<Thread> bucket;

	public LeakyBucketLimiter() {
		this(DEFAULT_RATE_LIMIT_PER_SECOND);
	}

	public LeakyBucketLimiter(int limit) {
		if (limit <= 0) {
			throw new IllegalArgumentException();
		}

		bucket = new LinkedBlockingQueue<>(limit);
		ExecutorService threadPool = Executors.newSingleThreadExecutor();

		TimestampHolder holder = new TimestampHolder(System.nanoTime());
		long interval = NANO_SECOND / limit;

		threadPool.submit(() -> {
			while (true) {
				long cur = System.nanoTime();
				if (cur - holder.getTimestamp() >= interval) {
					Thread thread = bucket.poll();
					Optional.ofNullable(thread).ifPresent(LockSupport::unpark);
					holder.setTimestamp(cur);
				}

				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void acquire() throws RejectException {
		if (bucket.remainingCapacity() == 0) {
			throw new RejectException();
		}

		Thread thread = Thread.currentThread();
		bucket.add(thread);
		LockSupport.park();
	}

	public static void main(String[] args) throws InterruptedException {
		RateLimiter rateLimiter = new LeakyBucketLimiter(1);

		Runnable runnable = () -> {
			int num = 100;
			while (num > 0) {
				try {
					rateLimiter.acquire();
				} catch (Exception e) {
					continue;
				}

				num--;
				System.out.println("Thread: " + Thread.currentThread().getName() + ", sec: "
						+ System.currentTimeMillis() / 1000L + ", mil: " + System.currentTimeMillis() + " got a token");
			}
		};

		long start = System.currentTimeMillis();
		ExecutorService threadPool = Executors.newCachedThreadPool();
		for (int i = 0; i < 10; i++) {
			threadPool.submit(runnable);
		}
		threadPool.awaitTermination(100, TimeUnit.SECONDS);
		long end = System.currentTimeMillis();
		System.out.println("over time: " + (end - start) / 1000);
	}
}
