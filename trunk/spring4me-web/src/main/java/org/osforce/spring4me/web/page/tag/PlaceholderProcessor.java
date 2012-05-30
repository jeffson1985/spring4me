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
	
	public void process(Writer writer, String groupId) throws Exception {
        GroupConfig groupConfig = pageConfig.getGroupConfig(groupId);
        includeGroup(writer, groupConfig);
	}
	
	protected void includeGroup(Writer writer, GroupConfig groupConfig) throws Exception {
		if(groupConfig!=null) { 
	    	for(int i=0, len=groupConfig.getAllWidgetConfig().size(); i<len; i++) {
	    		WidgetConfig widgetConfig = groupConfig.getAllWidgetConfig().get(i);
	    		boolean first = (i==0);
	    		boolean last = (i==len-1);
	    		//
	            includeWidget(writer, widgetConfig, first, last);
	    	}
		}
    }
    
    protected void includeWidget(Writer writer, WidgetConfig widgetConfig, boolean first, boolean last) throws Exception {
    	HttpWidget widget = callback.getHttpWidget(widgetConfig);
    	if(widget!=null) {
	    	String htmlFragment = widget.getWidgetResponse().getResponseAsString();
	        appendToPage(widgetConfig, writer, htmlFragment, first, last);
    	}
    }
    
    protected void appendToPage(WidgetConfig widgetConfig, Writer writer, 
    		String fragment, boolean first, boolean last) throws Exception {
    	if(fragment!=null) {
    		writer.append("<div");
    		writer.append(" id=\"" + widgetConfig.getId() + "\"");
    		writer.append(" class=\"widget-wrapper");
    		// append class only
    		if(first && last) {
    			writer.append(" widget-only");
    		}
    		// append class first
    		if(first && !last) {
    			writer.append(" widget-first");
    		}
    		// append class last
    		if(last && !first) {
    			writer.append(" widget-last");
    		}
    		//
    		writer.append("\">");
    		writer.append(fragment);
            writer.append("</div>");
    	}
    }
    
}
