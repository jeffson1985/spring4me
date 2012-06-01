package org.osforce.spring4me.web.cache.simple;

import org.osforce.spring4me.web.cache.Cache;
import org.osforce.spring4me.web.cache.CacheProvider;

/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-5-31 - 下午4:38:18
 */
public class RequestBackupProvider implements CacheProvider {

	private RequestBackupCache cache = new RequestBackupCache();
	
	public Cache getCache() {
		return cache;
	}

}
