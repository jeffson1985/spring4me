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

package org.sarons.spring4me.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sarons.spring4me.web.Keys;
import org.sarons.spring4me.web.steroetype.Widget;
import org.sarons.spring4me.web.widget.config.WidgetConfig;
import org.sarons.spring4me.web.widget.http.HttpWidgetRequest;
import org.sarons.spring4me.web.widget.utils.WidgetConfigUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.FlashMapManager;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * 
 * @author Gavin
 * @since 0.4.0
 * @create 2012-5-23 - 上午10:07:39
 */
@SuppressWarnings("serial")
public class DispatcherServlet extends org.springframework.web.servlet.DispatcherServlet {

	private static final String CONTEXT_CONFIG_LOCATION = "/WEB-INF/spring4me/dispatcherContext.xml";
	
	private String contextConfigLocation = CONTEXT_CONFIG_LOCATION;
	
	public void setContextConfigLocation(String contextConfigLocation) {
		this.contextConfigLocation = contextConfigLocation;
	}
	
	@Override
	public String getContextConfigLocation() {
		return this.contextConfigLocation;
	}
	
	@Override
	protected String getDefaultViewName(HttpServletRequest request)
			throws Exception {
		return null;
	}
	
	@Override
	protected HandlerExecutionChain getHandler(HttpServletRequest request)
			throws Exception {
		HandlerExecutionChain mappedHandler =  super.getHandler(request);
		//
		if(mappedHandler!=null && mappedHandler.getHandler()!=null) {
			prepareFlashMapForWidget(request, mappedHandler.getHandler());
		}
		//
		return mappedHandler;
	}
	
	@Override
	protected void render(ModelAndView mv, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		//
		exportContextPath(request, mv);
		//
		exportWidgetConfig(request, mv);
		//
		exportWidgetModel(request, mv);
		//
		if(mv!=null && mv.hasView()) {
			//
			super.render(mv, request, response);
		}
	}
	
	private void exportContextPath(HttpServletRequest request, ModelAndView mv) {
		mv.addObject(Keys.REQUEST_KEY_BASE, request.getContextPath());
		request.setAttribute(Keys.REQUEST_KEY_BASE, request.getContextPath());
	}

	private void exportWidgetConfig(HttpServletRequest request, ModelAndView mv) {
		if(request instanceof HttpWidgetRequest) {
    		WidgetConfig widgetConfig = WidgetConfigUtils.getWidgetConfig(request);
        	mv.addObject(Keys.REQUEST_KEY_WIDGET_CONFIG, widgetConfig);
    	}
	}
	
	private void exportWidgetModel(HttpServletRequest request, ModelAndView mv) {
		if(request instanceof HttpWidgetRequest) {
			ModelMap widgetModel = (ModelMap) mv.getModelMap();
			((HttpWidgetRequest)request).bindWidgetModel(widgetModel);
		}
	}
	
	private void prepareFlashMapForWidget(HttpServletRequest request, Object handler) {
		if(handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			//
			Widget widget = AnnotationUtils.getAnnotation(handlerMethod.getBeanType(), Widget.class);
			if(widget!=null) {
				//
				String widgetName = widget.value();
				if(!StringUtils.hasText(widgetName)) {
					String targetWidget = handlerMethod.getBeanType().getSimpleName();
					widgetName = StringUtils.uncapitalize(targetWidget);
				}
				//
				FlashMap flashMap = RequestContextUtils.getOutputFlashMap(request);
				flashMap.setTargetRequestPath(widgetName);
				FlashMapManager flashMapManager = RequestContextUtils.getFlashMapManager(request);
				flashMapManager.saveOutputFlashMap(flashMap, request, null);
			}
		}
	}
	
}
