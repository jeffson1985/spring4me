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

package org.osforce.spring4me.web.page.tag;

import java.io.Writer;

import org.osforce.spring4me.web.page.config.GroupConfig;
import org.osforce.spring4me.web.page.config.PageConfig;
import org.osforce.spring4me.web.widget.config.WidgetConfig;
import org.osforce.spring4me.web.widget.http.HttpWidget;
import org.springframework.util.StringUtils;

/**
 * 
 * @author gavin
 * @since 0.4.0
 * @create Feb 12, 2012 - 11:32:10 AM
 */
public class PlaceholderProcessor {

	private PageConfig pageConfig;
	private PlaceholderCallback callback;
	
	public PlaceholderProcessor(PageConfig pageConfig) {
		this.pageConfig = pageConfig;
	}
	
	public void setCallback(PlaceholderCallback callback) {
		this.callback = callback;
	}
	
	public void process(Writer writer, String groupId, String widgetId) throws Exception {
		// include widget
        if(StringUtils.hasText(widgetId)) {
            WidgetConfig widgetConfig = pageConfig.getWidgetConfig(groupId, widgetId);
            includeWidget(writer, widgetConfig);
        }
        //
        GroupConfig groupConfig = pageConfig.getGroupConfig(groupId);
        includeGroup(writer, groupConfig);
	}
	
	protected void includeGroup(Writer writer, GroupConfig groupConfig) throws Exception {
		if(groupConfig!=null) { 
	    	for(WidgetConfig widgetConfig : groupConfig.getAllWidgetConfig()) {
	            includeWidget(writer, widgetConfig); 
	    	}
		}
    }
    
    protected void includeWidget(Writer writer, WidgetConfig widgetConfig) throws Exception {
    	HttpWidget widget = callback.getHttpWidget(widgetConfig);
    	String htmlFragment = widget.getWidgetResponse().getResponseAsString();
        appendToPage(writer, htmlFragment);
    }
    
    protected void appendToPage(Writer writer, String fragment) throws Exception {
    	if(fragment!=null) {
    		writer.append(fragment);
    	}
    }
    
}
