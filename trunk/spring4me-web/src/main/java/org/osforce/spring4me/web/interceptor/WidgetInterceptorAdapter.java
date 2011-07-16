package org.osforce.spring4me.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osforce.spring4me.web.stereotype.Widget;
import org.osforce.spring4me.web.widget.core.HttpWidgetRequest;
import org.osforce.spring4me.web.widget.core.HttpWidgetResponse;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 
 * @author <a href="mailto:haozhonghu@hotmail.com">gavin</a>
 * @since 0.3.0
 * @create Jul 12, 2011 - 10:29:07 PM
 * <a href="http://www.opensourceforce.org">开源力量</a>
 */
public abstract class WidgetInterceptorAdapter implements HandlerInterceptor {

	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		Widget widget = AnnotationUtils.findAnnotation(handler.getClass(), Widget.class);
		if(widget!=null) {
			return preHandleWidget(new HttpWidgetRequest(request), 
					new HttpWidgetResponse(response), handler);
		}
		return true;
	}
	
	protected boolean preHandleWidget(HttpWidgetRequest request,
			HttpWidgetResponse response, Object handler) throws Exception {
		return true;
	}
	
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		Widget widget = AnnotationUtils.findAnnotation(handler.getClass(), Widget.class);
		if(widget!=null) {
			postHandleWidget(new HttpWidgetRequest(request), 
					new HttpWidgetResponse(response), handler, modelAndView);
		}
	}

	public void postHandleWidget(HttpWidgetRequest request,
			HttpWidgetResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}
	
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		Widget widget = AnnotationUtils.findAnnotation(handler.getClass(), Widget.class);
		if(widget!=null) {
			afterCompletionWidget(new HttpWidgetRequest(request), 
					new HttpWidgetResponse(response), handler, ex);
		}
	}
	
	public void afterCompletionWidget(HttpWidgetRequest request,
			HttpWidgetResponse response, Object handler, Exception ex)
			throws Exception {
	}
	
}
