package cn.devezhao.commons;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author zhaofang123@gmail.com
 * @since 07/18/2018
 */
public class ExpiresMap <K, V> implements Map<K, V> {

	public static final int HOUR_IN_SECOND = 60 * 60;

	private final Map<K, Object[]> nestable = new HashMap<>();

	/**
	 * @param key
	 * @param expires 秒
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public V get(Object key, int expires) {
		Object[] v = nestable.get(key);
		if (v == null) {
			return null;
		}
		
		long time = System.currentTimeMillis() - (Long) v[1];
		if (time / 1000 > expires) {
			remove(key);
			return null;
		}
		return (V) v[0];
	}

	@Deprecated
	@SuppressWarnings("unchecked")
	@Override
	public V get(Object key) {
		Object[] v = nestable.get(key);
		return v == null ? null : (V) v[0];
	}

	@Override
	public V put(K key, V value) {
		nestable.put(key, new Object[] { value, System.currentTimeMillis() });
		return value;
	}

	@SuppressWarnings("unchecked")
	@Override
	public V remove(Object key) {
		Object[] v = nestable.remove(key);
		return v == null ? null : (V) v[0];
	}

	@Override
	public int size() {
		return nestable.size();
	}

	@Override
	public boolean isEmpty() {
		return nestable.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return nestable.containsKey(key);
	}
	
	@Override
	public void clear() {
		nestable.clear();
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<K> keySet() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Collection<V> values() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean containsValue(Object value) {
		throw new UnsupportedOperationException();
	}
}
