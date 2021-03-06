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

package org.sarons.spring4me.web.widget.utils;

import javax.servlet.ServletRequest;

import org.sarons.spring4me.web.widget.config.WidgetConfig;

/**
 * 
 * @author gavin
 * @since 0.4.0
 * @create Feb 12, 2012 - 11:32:28 AM
 */
public abstract class WidgetConfigUtils {
	
    public static WidgetConfig getWidgetConfig(ServletRequest request) {
        return (WidgetConfig) request.getAttribute(WidgetConfig.KEY);
    }

    public static void setWidgetConfig(ServletRequest request, WidgetConfig widgetConfig) {
        request.setAttribute(WidgetConfig.KEY, widgetConfig);
    }
    
    public static String getWidgetStyleAttrName(String name, WidgetConfig widgetConfig) {
    	return "Widget_" + widgetConfig.getId() + "_" + name + ")";
    }

}
