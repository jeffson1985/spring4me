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

package org.sarons.spring4me.web.widget.http.impl;

import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.sarons.spring4me.web.widget.http.HttpWidgetRequest;
import org.springframework.web.util.WebUtils;

/**
 * 
 * @author gavin
 * @since 0.4.0
 * @create Feb 12, 2012 - 11:30:42 AM
 */
public class DefaultHttpWidgetRequest extends HttpServletRequestWrapper 
	implements HttpWidgetRequest {

	private Map<String, ?> widgetModel;

    public DefaultHttpWidgetRequest(HttpServletRequest httpRequest) {
    	super(httpRequest);
    }
    
    public Map<String, ?> getWidgetModel() {
    	return widgetModel;
    }
    
    public void bindWidgetModel(Map<String, ?> widgetModel) {
    	this.widgetModel = widgetModel;
    }
    
    @Override
    public RequestDispatcher getRequestDispatcher(String path) {
    	setAttribute(WebUtils.INCLUDE_PATH_INFO_ATTRIBUTE, path);
    	return super.getRequestDispatcher(path);
    }

}
