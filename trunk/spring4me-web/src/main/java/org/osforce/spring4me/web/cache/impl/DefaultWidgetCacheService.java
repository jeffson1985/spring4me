package org.osforce.spring4me.web.cache.impl;

import java.io.IOException;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.constructs.blocking.BlockingCache;

import org.apache.commons.lang.StringUtils;
import org.osforce.spring4me.web.cache.WidgetCacheService;
import org.osforce.spring4me.web.widget.config.WidgetConfig;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * 
 * @author <a href="mailto:haozhonghu@hotmail.com">gavin</a>
 * @since 0.2.2
 * @create Jun 13, 2011 - 3:56:14 PM
 * <a href="http://www.opensourceforce.org">开源力量</a>
 */
public class DefaultWidgetCacheService implements WidgetCacheService, ResourceLoaderAware {
	
	private ResourceLoader resourceLoader;
	private String cacheConfigLocation = "classpath:ehcache-widget.xml";
	private CacheManager cacheManager;
	private BlockingCache blockingCache;
	private String cacheName = "widgetCache";
	
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}
	
	public void setCacheConfigLocation(String cacheConfigLocation) {
		this.cacheConfigLocation = cacheConfigLocation;
	}
	
	public void setCacheName(String cacheName) {
		this.cacheName = cacheName;
	}
	
	public Object get(Object key) {
		Element element = getCache().get(key);
        Object value = null;
        if(element!=null) {
        	value = element.getValue();
        } else {
        	getCache().put(new Element(key, value));
        }
		return value;
	}

	public void put(Object key, Object value) {
		Element element = new Element(key, value, false, getCacheTime(key), getCacheTime(key));
		getCache().put(element);
	}
	
	protected Integer getCacheTime(Object key) {
		WidgetConfig widgetConfig = (WidgetConfig) key;
		String cache = StringUtils.isNotBlank(widgetConfig.getCache()) ? widgetConfig.getCache() : "-1";
		return Integer.parseInt(cache);
	}
	
	protected CacheManager getCacheManager() {
		if(cacheManager==null) {
			Resource resource = resourceLoader.getResource(cacheConfigLocation);
			try {
				cacheManager = new CacheManager(resource.getURL());
			} catch (CacheException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return cacheManager;
	}
	
	protected Ehcache getCache() {
		 Ehcache cache = getCacheManager().getEhcache(cacheName);
         if (cache == null) {
             throw new CacheException("cache '" + cacheName
                     + "' not found in configuration");
         }
         if (!(cache instanceof BlockingCache)) {
             // decorate and substitute
             BlockingCache newBlockingCache = new BlockingCache(cache);
             getCacheManager().replaceCacheWithDecoratedCache(cache, newBlockingCache);
         }
         blockingCache = (BlockingCache) getCacheManager().getEhcache(cacheName);
         return blockingCache;
	}
	
}
