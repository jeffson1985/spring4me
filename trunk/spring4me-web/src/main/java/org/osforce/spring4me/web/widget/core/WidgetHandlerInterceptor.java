package org.osforce.spring4me.web.widget.core;

import org.osforce.spring4me.web.interceptor.WidgetInterceptorAdapter;
import org.springframework.web.servlet.ModelAndView;


/**
 * 
 * @author <a href="mailto:haozhonghu@hotmail.com">gavin</a>
 * @since 0.3.0
 * @create Jul 13, 2011 - 9:31:41 PM
 * <a href="http://www.opensourceforce.org">开源力量</a>
 */
public class WidgetHandlerInterceptor extends WidgetInterceptorAdapter {

	@Override
	protected boolean preHandleWidget(HttpWidgetRequest request,
			HttpWidgetResponse response, Object handler) throws Exception {
		return super.preHandleWidget(request, response, handler);
	}
	
	@Override
	public void postHandleWidget(HttpWidgetRequest request,
			HttpWidgetResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		super.postHandleWidget(request, response, handler, modelAndView);
	}
	
	@Override
	public void afterCompletionWidget(HttpWidgetRequest request,
			HttpWidgetResponse response, Object handler, Exception ex)
			throws Exception {
		System.out.println(response.getResponseAsString());
		super.afterCompletionWidget(request, response, handler, ex);
	}
	
}
