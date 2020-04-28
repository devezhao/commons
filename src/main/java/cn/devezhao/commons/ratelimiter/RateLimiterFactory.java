package cn.devezhao.commons.ratelimiter;

import java.util.HashMap;
import java.util.Map;

/**
 * 在用量不大的情况下
 *
 * @author ZHAO
 * @date 04/28/2020
 */
public class RateLimiterFactory {

    private static final Map<String, RateLimiter> RL_MAP = new HashMap<>();

    /**
     * @param limit
     * @return
     */
    public static RateLimiter createRateCounter(int limit) {
        return new RateCounter(limit);
    }

    /**
     * @param limit
     * @return
     */
    public static RateLimiter createLeakyBucketLimiter(int limit) {
        return new LeakyBucketLimiter(limit);
    }

    /**
     * @param limit
     * @return
     */
    public static RateLimiter createTokenBucketLimiter(int limit) {
        return new TokenBucketLimiter(limit);
    }

    /**
     * @param key
     * @param limit
     * @return
     */
    public static RateLimiter getRateCounter(String key, int limit) {
        RateLimiter c = RL_MAP.get(key);
        if (c == null) {
            c = new RateCounter(limit);
            RL_MAP.put(key, c);
        }
        return c;
    }

    /**
     * @param key
     * @param limit
     * @return
     */
    public static RateLimiter getLeakyBucketLimiter(String key, int limit) {
        RateLimiter c = RL_MAP.get(key);
        if (c == null) {
            c = new LeakyBucketLimiter(limit);
            RL_MAP.put(key, c);
        }
        return c;
    }

    /**
     * @param key
     * @param limit
     * @return
     */
    public static RateLimiter getTokenBucketLimiter(String key, int limit) {
        RateLimiter c = RL_MAP.get(key);
        if (c == null) {
            c = new TokenBucketLimiter();
            RL_MAP.put(key, c);
        }
        return c;
    }
}
