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

package org.osforce.spring4me.web.page.render.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osforce.spring4me.web.page.PageRenderException;
import org.osforce.spring4me.web.page.config.PageConfig;
import org.osforce.spring4me.web.page.render.AbstractPageRender;
import org.osforce.spring4me.web.widget.config.WidgetConfig;
import org.osforce.spring4me.web.widget.http.HttpWidget;
import org.osforce.spring4me.web.widget.http.HttpWidgetRequest;
import org.osforce.spring4me.web.widget.http.HttpWidgetResponse;
import org.osforce.spring4me.web.widget.http.impl.DefaultHttpWidgetRequest;
import org.osforce.spring4me.web.widget.http.impl.DefaultHttpWidgetResponse;

/**
 * 
 * @author gavin
 * @since 0.4.0
 * @create Feb 12, 2012 - 11:28:55 AM
 */
public class StandardPageRender extends AbstractPageRender {

    public StandardPageRender() {
    }

	@Override
	protected void nativePageRender(PageConfig pageConfig, 
			HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
    	List<WidgetConfig> widgetConfigList = getWidgetConfigList(pageConfig);
		//
    	try {
	    	for(WidgetConfig widgetConfig : widgetConfigList) {
	    		HttpWidget httpWidget = getCachedWidget(widgetConfig);
	    		if(httpWidget==null && !widgetConfig.isDisabled()) {
		    		// new widget request and response
		    		HttpWidgetRequest widgetRequest = new DefaultHttpWidgetRequest(httpRequest);
		    		HttpWidgetResponse widgetResponse = new DefaultHttpWidgetResponse(httpResponse);
		    		//
		    		preWidgetRender(widgetConfig, widgetRequest, widgetResponse);
		    		//
		    		String widgetPath = widgetConfig.getPath() + ".widget";
	                widgetRequest.getRequestDispatcher(widgetPath).include(widgetRequest, widgetResponse);
		    		//
	                postWidgetRender(widgetConfig, widgetRequest, widgetResponse);
	    		}
	    	}
    	} catch(Exception e) {
    		throw new PageRenderException("Page render exception!", e);
    	}
	}
	
}
