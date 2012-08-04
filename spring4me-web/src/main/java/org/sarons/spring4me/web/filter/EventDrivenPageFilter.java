package org.sarons.spring4me.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sarons.spring4me.web.page.config.PageConfig;
import org.sarons.spring4me.web.page.utils.PageConfigUtils;

public class EventDrivenPageFilter extends PageFilter {

	public static final String DISPATCH_EVENT = "/dispatch/event";
	
	@Override
	protected void doService(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, FilterChain chain) throws IOException, ServletException {
		//
		if(httpRequest.getRequestURI().contains(DISPATCH_EVENT)) {
			String event = httpRequest.getParameter("name");
			dispatchEvent(httpRequest, httpResponse, event);
		} else {
			super.doService(httpRequest, httpResponse, chain);
		}
	}

	protected void dispatchEvent(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, String event) throws IOException, ServletException {
		//
		PageConfig pageConfig = PageConfigUtils.getPageConfig(httpRequest);
		if(pageConfig.getEvents().containsKey(event)) {
			String redirectUrl = httpRequest.getContextPath() + pageConfig.getEvents().get(event);
			httpResponse.sendRedirect(redirectUrl);
		} else {
			throw new UnsupportedOperationException(
					String.format("Event %s not supported in page %s", event, pageConfig.getPath()));
		}
	}
	
}
