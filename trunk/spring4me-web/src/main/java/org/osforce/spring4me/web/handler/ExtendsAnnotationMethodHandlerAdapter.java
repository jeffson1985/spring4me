package org.osforce.spring4me.web.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osforce.spring4me.web.cache.DefaultWidgetCacheService;
import org.osforce.spring4me.web.cache.WidgetCacheService;
import org.osforce.spring4me.web.widget.WidgetConfig;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;

/**
 * 
 * @author <a href="mailto:haozhonghu@hotmail.com">gavin</a>
 * @since 0.1.0
 * @create May 21, 2011 - 6:37:00 PM
 * <a href="http://www.opensourceforce.org">开源力量</a>
 */
public class ExtendsAnnotationMethodHandlerAdapter extends AnnotationMethodHandlerAdapter {

	private WidgetCacheService widgetCacheService = new DefaultWidgetCacheService();
	
	public ExtendsAnnotationMethodHandlerAdapter() {
	}
	
	public void setWidgetCacheService(WidgetCacheService widgetCacheService) {
		this.widgetCacheService = widgetCacheService;
	}
	
	@Override
	public ModelAndView handle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		ModelAndView mav = null;
		Object value = beforeHandle(request, handler);
		if(value instanceof ModelAndView) {
			return (ModelAndView) value;
		}
		//
		 mav = super.handle(request, response, handler);
		 //
		 afterHandle(request, handler, mav);
		 return mav;
	}
	
	protected Object beforeHandle(HttpServletRequest request, Object handler) {
		WidgetConfig widgetConfig = (WidgetConfig) request.getAttribute(WidgetConfig.KEY);
		if(widgetConfig!=null) {
			// get ModelAndView from cache
			return widgetCacheService.getFromCache(widgetConfig);
		}
		return null;
	}
	
	protected void afterHandle(HttpServletRequest request, Object handler, ModelAndView mav) {
		WidgetConfig widgetConfig = (WidgetConfig) request.getAttribute(WidgetConfig.KEY);
		if(widgetConfig!=null) {
			// set ModelAndView to cache
			widgetCacheService.setToCache(widgetConfig, mav);
		}
	}
	
}
