package org.osforce.spring4me.web.cache;

import java.util.HashMap;
import java.util.Map;

import org.osforce.spring4me.web.widget.WidgetConfig;
import org.springframework.web.servlet.ModelAndView;

/**
 * 
 * @author <a href="mailto:haozhonghu@hotmail.com">gavin</a>
 * @since 0.1.0
 * @create May 21, 2011 - 6:48:29 PM
 * <a href="http://www.opensourceforce.org">开源力量</a>
 */
public class DefaultWidgetCacheService implements WidgetCacheService {

	private Map<Object, Object> simpleCache = new HashMap<Object, Object>();
	
	public ModelAndView getFromCache(WidgetConfig widgetConfig) {
		System.out.println("get model and view from cache");
		//return (ModelAndView) simpleCache.get(widgetConfig.getPath());
		return null;
	}

	public void setToCache(WidgetConfig widgetConfig, ModelAndView mav) {
		System.out.println("set model and view to cache");
		//simpleCache.put(widgetConfig.getPath(), mav);
	}

}
