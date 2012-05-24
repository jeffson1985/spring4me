/*
 * Copyright 2011-2012 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.osforce.spring4me.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osforce.spring4me.web.Keys;
import org.osforce.spring4me.web.event.EventConstants;
import org.osforce.spring4me.web.navigation.config.EventConfig;
import org.osforce.spring4me.web.navigation.config.NavigationConfig;
import org.osforce.spring4me.web.navigation.config.SiteConfig;
import org.osforce.spring4me.web.navigation.config.SiteConfigFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UrlPathHelper;

/**
 * 
 * @author Gavin
 * @since 0.4.0
 * @create 2012-4-28 - 下午5:28:42
 */
public class EventDrivenPageFilter extends PageFilter {
	
	private static final String EVENT_DRIVEN_SERVICE_PATH = "/dispatch/event";
	
	//
	private enum EventType {ACTION, PAGE, UNKNOW}
	//
	private SiteConfigFactory siteConfigFactory;
	//
	private UrlPathHelper urlPathHelper = new UrlPathHelper();
	
	@Override
	protected void initFramework() throws ServletException {
		super.initFramework();
		//
		this.siteConfigFactory = getPageApplicationContext().getBean(SiteConfigFactory.class);
	}
	
	@Override
	protected void doService(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, FilterChain chain)
			throws IOException, ServletException {
		//
		exportEventDrivenServiceUrl(httpRequest);
		//
		EventType eventType = parseEventType(httpRequest);
		//
		if(eventType==EventType.PAGE) {
			processPageEvent(httpRequest, httpResponse);
		} else if(eventType==EventType.ACTION) {
			processActionEvent(httpRequest, httpResponse);
		} else {
			super.doService(httpRequest, httpResponse, chain);
		}
	}
	
	protected EventType parseEventType(HttpServletRequest httpRequest) {
		String requestUri = urlPathHelper.getRequestUri(httpRequest);
		if(requestUri.contains(EVENT_DRIVEN_SERVICE_PATH)) {
			return EventType.PAGE;
		} else if(StringUtils.hasText(httpRequest.getParameter(Keys.PARAMETER_KEY_EVENT))) {
			return EventType.ACTION;
		}
		//
		return EventType.UNKNOW;
	}
	
	protected void processPageEvent(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
		String event = httpRequest.getParameter(Keys.PARAMETER_KEY_EVENT);
		int separatorPosition = event.indexOf("|");
		//
		String navId = event.substring(0, separatorPosition);
		String eventId = event.substring(separatorPosition+1);
		//
		String publishedEvent = (String) httpRequest.getSession().getAttribute(EventConstants.EVENT);
		if(StringUtils.hasText(publishedEvent)) {
			eventId = publishedEvent;
			//
			httpRequest.getSession().removeAttribute(EventConstants.EVENT);
		}
		//
		SiteConfig siteConfig = siteConfigFactory.findSite();
		//
		NavigationConfig originalNavigationConfig = siteConfig.getNavigationConfig(navId);
		EventConfig originalEventConfig = originalNavigationConfig.getEventConfig(eventId);
		NavigationConfig currentNavigationConfig = siteConfig.getNavigationConfig(originalEventConfig.getTo());
		//
		String redirectUri = urlPathHelper.getContextPath(httpRequest) + currentNavigationConfig.getPath();
		httpResponse.sendRedirect(redirectUri);
	}
	
	protected void processActionEvent(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ServletException, IOException {
		//
		String requestPath = urlPathHelper.getLookupPathForRequest(httpRequest);
		httpRequest.getRequestDispatcher(requestPath).include(httpRequest, httpResponse);
		//
		processPageEvent(httpRequest, httpResponse);
	}
	
	private void exportEventDrivenServiceUrl(HttpServletRequest httpRequest) {
		String contextPath = urlPathHelper.getContextPath(httpRequest);
		String eventDrivenServiceUrl = contextPath + EVENT_DRIVEN_SERVICE_PATH;
		//
		httpRequest.setAttribute(Keys.REQUEST_KEY_EVENT_DRIVEN_SERVICE_URL, eventDrivenServiceUrl);
	}
	
}
