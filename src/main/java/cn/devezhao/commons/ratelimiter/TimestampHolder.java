package cn.devezhao.commons.ratelimiter;

/**
 * @author Tian ZhongBo
 * @author ZHAO
 * @date 2019-05-09 22:37:19
 */
public class TimestampHolder {
	
	private long timestamp;

	public TimestampHolder() {
		this(System.currentTimeMillis());
	}

	public TimestampHolder(long timestamp) {
		this.timestamp = timestamp;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
}
