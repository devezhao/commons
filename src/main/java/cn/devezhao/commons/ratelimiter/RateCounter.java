package cn.devezhao.commons.ratelimiter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 计数器算法
 * 
 * @author Tian ZhongBo
 * @author ZHAO
 */
public class RateCounter implements RateLimiter {

	private static final int DEFAULT_RATE_LIMIT_PER_SECOND = Integer.MAX_VALUE;

	private int limit;

	private AtomicInteger counter;

	public RateCounter() {
		this(DEFAULT_RATE_LIMIT_PER_SECOND);
	}

	public RateCounter(int limit) {
		if (limit < 0) {
			throw new IllegalArgumentException("limit less than zero");
		}

		this.limit = limit;
		counter = new AtomicInteger();

		TimestampHolder holder = new TimestampHolder();
		ExecutorService executorService = Executors.newSingleThreadExecutor();

		executorService.submit(() -> {
			while (true) {
				long cur = System.currentTimeMillis();
				if (cur - holder.getTimestamp() >= 1000) {
					holder.setTimestamp(cur);
					counter.set(0);
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
		if (counter.incrementAndGet() > limit) {
			throw new RejectException();
		}
	}

	public static void main(String[] args) {
		RateLimiter rateLimiter = new RateCounter(10);

		int num = 100;
		while (num > 0) {
			try {
				rateLimiter.acquire();
			} catch (Exception e) {
				continue;
			}

			num--;
			System.out.println("sec: " + System.currentTimeMillis() / 1000L + ", mil: " + System.currentTimeMillis()
					+ " got a token");
		}
	}
}
