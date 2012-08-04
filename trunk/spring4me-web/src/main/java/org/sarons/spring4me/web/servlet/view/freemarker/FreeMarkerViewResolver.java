package org.sarons.spring4me.web.servlet.view.freemarker;

import java.util.Locale;

import org.sarons.spring4me.web.filter.EventDrivenPageFilter;
import org.sarons.spring4me.web.servlet.view.RedirectView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.AbstractTemplateViewResolver;

/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-7-30 - 下午9:50:41
 */
public class FreeMarkerViewResolver extends AbstractTemplateViewResolver {

	private static final String EVENT_PREFIX = "event:";
	
	public FreeMarkerViewResolver() {
		setViewClass(requiredViewClass());
	}

	/**
	 * Requires {@link FreeMarkerView}.
	 */
	@Override
	@SuppressWarnings("rawtypes")
	protected Class requiredViewClass() {
		return FreeMarkerView.class;
	}
	
	@Override
	protected View createView(String viewName, Locale locale) throws Exception {
		//
		if(viewName.startsWith(EVENT_PREFIX)) {
			String event = viewName.substring(EVENT_PREFIX.length());
			String redirectUrl = resolveEvent(event);
			return new RedirectView(redirectUrl, isRedirectContextRelative(), isRedirectHttp10Compatible());
		}
		//
		return super.createView(viewName, locale);
	}
	
	protected String resolveEvent(String event) {
		return EventDrivenPageFilter.DISPATCH_EVENT + "?name=" + event;
	}
	
}
