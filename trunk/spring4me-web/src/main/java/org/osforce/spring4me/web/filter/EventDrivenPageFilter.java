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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osforce.spring4me.web.Keys;
import org.osforce.spring4me.web.cache.CacheProvider;
import org.osforce.spring4me.web.cache.simple.RequestBackup;
import org.osforce.spring4me.web.event.EventReceiver;
import org.osforce.spring4me.web.navigation.config.EventConfig;
import org.osforce.spring4me.web.navigation.config.NavigationConfig;
import org.osforce.spring4me.web.navigation.config.NavigationConfigFactory;
import org.osforce.spring4me.web.navigation.utils.NavigationConfigUtils;
import org.osforce.spring4me.web.page.config.PageConfig;
import org.osforce.spring4me.web.page.utils.EventParameter;
import org.osforce.spring4me.web.widget.config.WidgetConfig;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UrlPathHelper;

/**
 * 
 * @author Gavin
 * @since 0.4.0
 * @create 2012-4-28 - 下午5:28:42
 */
public class EventDrivenPageFilter extends PageFilter {
	
	private static final Log log = LogFactory.getLog(EventDrivenPageFilter.class);
	
	private static final String EVENT_DRIVEN_SERVICE_PATH = "/dispatch/event";
	//
	private enum EventType {ACTION, PAGE, UNKNOW}
	//
	private NavigationConfigFactory navigationConfigFactory;
	//
	private UrlPathHelper urlPathHelper = new UrlPathHelper();
	
	private CacheProvider requestBackupCacheProvider;
	
	@Override
	protected void initFramework() throws ServletException {
		super.initFramework();
		//
		this.navigationConfigFactory = getPageApplicationContext().getBean(NavigationConfigFactory.class);
		this.requestBackupCacheProvider = getPageApplicationContext().getBean("requestBackupCacheProvider", CacheProvider.class);
	}
	
	@Override
	protected void doService(HttpServletRequest httpRequest, HttpServletResponse httpResponse, 
			FilterChain chain) throws IOException, ServletException {
		//
		exportEventDrivenServiceUrl(httpRequest);
		//
		EventType eventType = parseEventType(httpRequest);
		httpRequest.setAttribute("eventType", eventType.toString());
		//
		if(eventType==EventType.PAGE) {
			processPageEvent(httpRequest, httpResponse, createEventParameter(httpRequest));
		} 
		else if(eventType==EventType.ACTION) {
			processActionEvent(httpRequest, httpResponse, createEventParameter(httpRequest));
		} 
		else {
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
	
	protected void processPageEvent(HttpServletRequest httpRequest, 
			HttpServletResponse httpResponse, EventParameter eventParameter) throws IOException, ServletException {
		//String event = httpRequest.getParameter(Keys.PARAMETER_KEY_EVENT);
		if(log.isDebugEnabled()) {
			log.debug("Process event " + eventParameter.getEvent());
		}
		//
		NavigationConfig fromNavigationConfig = navigationConfigFactory.findNavigation(eventParameter.getPagePath());
		EventConfig fromEventConfig = fromNavigationConfig.getEventConfig(eventParameter.getEvent());
		NavigationConfig toNavigationConfig = navigationConfigFactory.findNavigation(fromEventConfig.getTo());
		//
		NavigationConfigUtils.setFromNavigationConfig(httpRequest, fromNavigationConfig);
		NavigationConfigUtils.setToNavigationConfig(httpRequest, toNavigationConfig);
		//
		String redirectLocation = urlPathHelper.getContextPath(httpRequest) + toNavigationConfig.getPath();
		if(StringUtils.hasText(httpRequest.getParameter("flowId"))) {
			redirectLocation += "?flowId=" + httpRequest.getParameter("flowId");
		}
		httpResponse.sendRedirect(redirectLocation);
	}
	
	protected void processActionEvent(HttpServletRequest httpRequest, 
			HttpServletResponse httpResponse, EventParameter eventParameter) throws ServletException, IOException {
		//
		String requestPath = urlPathHelper.getLookupPathForRequest(httpRequest);
		httpRequest.getRequestDispatcher(requestPath).include(httpRequest, httpResponse);
		//
		String publishedEvent = EventReceiver.receive(httpRequest, true);
		if(StringUtils.hasText(publishedEvent)) {
			eventParameter.setEvent(publishedEvent);
		}
		//
		PageConfig pageConfig = getPageConfigFactory().findPage(eventParameter.getPagePath());
		WidgetConfig widgetConfig = pageConfig.getWidgetConfig(eventParameter.getWidgetPath());
		String key = generateKey(httpRequest, widgetConfig);
		RequestBackup requestBackup = (RequestBackup) requestBackupCacheProvider.getCache().get(key);
		if(requestBackup==null) {
			requestBackup = new RequestBackup(httpRequest);
		} else {
			requestBackup.mergeRequest(httpRequest);
		}
		requestBackupCacheProvider.getCache().put(key, requestBackup);
		//
		processPageEvent(httpRequest, httpResponse, eventParameter);
	}
	
	private String generateKey(HttpServletRequest httpRequest, WidgetConfig widgetConfig) {
		return httpRequest.getSession().getId() + "T" + widgetConfig.getPath();
	}
	
	private EventParameter createEventParameter(HttpServletRequest httpRequest) {
		String event = httpRequest.getParameter(Keys.PARAMETER_KEY_EVENT);
		return new EventParameter(event);
	}
	
	private void exportEventDrivenServiceUrl(HttpServletRequest httpRequest) {
		String contextPath = urlPathHelper.getContextPath(httpRequest);
		String eventDrivenServiceUrl = contextPath + EVENT_DRIVEN_SERVICE_PATH;
		//
		httpRequest.setAttribute(Keys.REQUEST_KEY_EVENT_DRIVEN_SERVICE_URL, eventDrivenServiceUrl);
	}
	
}
