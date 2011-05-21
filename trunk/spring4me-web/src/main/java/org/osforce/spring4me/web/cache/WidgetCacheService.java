package org.osforce.spring4me.web.cache;

import org.osforce.spring4me.web.widget.WidgetConfig;
import org.springframework.web.servlet.ModelAndView;

/**
 * 
 * @author <a href="mailto:haozhonghu@hotmail.com">gavin</a>
 * @since 0.1.0
 * @create May 21, 2011 - 6:40:57 PM
 * <a href="http://www.opensourceforce.org">开源力量</a>
 */
public interface WidgetCacheService {

	ModelAndView getFromCache(WidgetConfig widgetConfig);

	void setToCache(WidgetConfig widgetConfig, ModelAndView mav);
	
}
