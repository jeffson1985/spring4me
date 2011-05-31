package org.osforce.spring4me.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osforce.spring4me.web.stereotype.Widget;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public abstract class WidgetInterceptorAdapter implements HandlerInterceptor {

	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		Widget widget = AnnotationUtils.findAnnotation(handler.getClass(), Widget.class);
		if(widget!=null) {
			return preHandleWidget(request, response, handler);
		}
		return true;
	}
	
	protected boolean preHandleWidget(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		return true;
	}
	
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		Widget widget = AnnotationUtils.findAnnotation(handler.getClass(), Widget.class);
		if(widget!=null) {
			postHandleWidget(request, response, handler, modelAndView);
		}
	}

	public void postHandleWidget(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}
	
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		Widget widget = AnnotationUtils.findAnnotation(handler.getClass(), Widget.class);
		if(widget!=null) {
			afterHandleCompletion(request, response, handler, ex);
		}
	}
	
	public void afterHandleCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}
	
}
