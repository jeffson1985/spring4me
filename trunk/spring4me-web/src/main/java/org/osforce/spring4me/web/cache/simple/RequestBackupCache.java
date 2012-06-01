package org.osforce.spring4me.web.cache.simple;

import java.util.HashMap;
import java.util.Map;

import org.osforce.spring4me.web.cache.Cache;

/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-5-31 - 下午4:25:52
 */
public class RequestBackupCache implements Cache {
	
	private Map<Object, Object> cache = new HashMap<Object, Object>();

	public Object get(Object key) {
		return cache.get(key);
	}

	public void put(Object key, Object value) {
		cache.put(key, value);
	}

}
