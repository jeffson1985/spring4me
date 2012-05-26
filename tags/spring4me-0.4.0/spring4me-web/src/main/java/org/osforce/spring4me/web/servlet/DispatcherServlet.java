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

package org.osforce.spring4me.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osforce.spring4me.web.Keys;
import org.osforce.spring4me.web.widget.config.WidgetConfig;
import org.osforce.spring4me.web.widget.http.HttpWidgetRequest;
import org.springframework.web.servlet.ModelAndView;

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
	protected void render(ModelAndView mv, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		//
		exportContextPath(request, mv);
		//
		exportWidgetConfig(request, mv);
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
    		WidgetConfig widgetConfig = ((HttpWidgetRequest)request).getWidgetConfig();
        	mv.addObject(Keys.REQUEST_KEY_WIDGET_CONFIG, widgetConfig);
    	}
	}
	
}
