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

package org.osforce.spring4me.web;

import org.osforce.spring4me.web.page.config.PageConfig;
import org.osforce.spring4me.web.widget.config.WidgetConfig;

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
	
	String REQUEST_KEY_WIDGET_CONFIG = WidgetConfig.KEY;
	
	String REQUEST_KEY_PAGE_CONFIG = PageConfig.KEY;
	
	/**
	 * Session Key Scope
	 */
	
}
