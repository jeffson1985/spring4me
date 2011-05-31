package org.osforce.spring4me.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 
 * @author <a href="mailto:haozhonghu@hotmail.com">gavin</a>
 * @since 0.1.0
 * @create May 19, 2011 - 5:28:50 PM
 * <a href="http://www.opensourceforce.org">开源力量</a>
 */
public abstract class ControllerInterceptorAdapter implements HandlerInterceptor {

	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		Controller controller = AnnotationUtils.findAnnotation(handler.getClass(), Controller.class);
		if(controller!=null) {
			return preHandleController(request, response, handler);
		}
		return true;
	}
	
	protected boolean preHandleController(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		return true;
	}
	
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		Controller controller = AnnotationUtils.findAnnotation(handler.getClass(), Controller.class);
		if(controller!=null) {
			postHandleController(request, response, handler, modelAndView);
		}
	}

	public void postHandleController(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}
	
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		Controller controller = AnnotationUtils.findAnnotation(handler.getClass(), Controller.class);
		if(controller!=null) {
			afterHandleCompletion(request, response, handler, ex);
		}
	}
	
	public void afterHandleCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}
}
