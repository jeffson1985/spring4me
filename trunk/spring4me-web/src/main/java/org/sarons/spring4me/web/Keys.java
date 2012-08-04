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

package org.sarons.spring4me.web;

import org.sarons.spring4me.web.page.config.PageConfig;
import org.sarons.spring4me.web.widget.config.WidgetConfig;
import org.springframework.util.StringUtils;

/**
 * 
 * @author Gavin
 * @since 0.4.0
 * @create 2012-5-24 - 上午9:37:51
 */
public interface Keys {
	
	/**
	 * Parameter Key Scope 
	 */
	String PARAMETER_KEY_EVENT = "event";
	
	/**
	 * Request key Scope
	 */
	String REQUEST_KEY_BASE = "base";
	
	String REQUEST_KEY_EVENT_DRIVEN_SERVICE_URL = "eventDrivenServiceUrl";
	
	String REQUEST_KEY_WIDGET_CONFIG = StringUtils.uncapitalize(WidgetConfig.KEY);
	
	String REQUEST_KEY_PAGE_CONFIG = StringUtils.uncapitalize(PageConfig.KEY);
	
	String REQUEST_KEY_FROM_NAVIGATION_CONFIG = "fromNavigationConfig";
	
	String REQUEST_KEY_TO_NAVIGATION_CONFIG = "toNavigationConfig";
	
	/**
	 * Session Key Scope
	 */
	
}
