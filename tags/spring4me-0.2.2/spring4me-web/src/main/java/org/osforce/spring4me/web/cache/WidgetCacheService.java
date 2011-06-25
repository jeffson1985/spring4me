package org.osforce.spring4me.web.cache;


/**
 * 
 * @author <a href="mailto:haozhonghu@hotmail.com">gavin</a>
 * @since 0.2.2
 * @create Jun 13, 2011 - 3:20:06 PM
 * <a href="http://www.opensourceforce.org">开源力量</a>
 */
public interface WidgetCacheService {

	public void put(Object key, Object value);
	
	public Object get(Object key);
	
}
