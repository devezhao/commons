package cn.devezhao.commons.ratelimiter;

/**
 * @author Tian ZhongBo
 * @author devezhao
 * @date 2019-05-09 22:07:38
 */
public interface RateLimiter {

	/**
	 * 每秒限制
	 */
	static final int DEFAULT_RATE_LIMIT_PER_SECOND = Integer.MAX_VALUE;
	
	/**
	 * 纳秒
	 */
	static final long NANO_SECOND = 1000 * 1000 * 1000;
	
	/**
	 * @throws RejectException
	 */
    void acquire() throws RejectException;
}
