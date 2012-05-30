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

package org.osforce.spring4me.web.navigation.tag;

import java.io.IOException;
import java.io.Writer;

import org.osforce.spring4me.web.page.config.PageConfig;
import org.springframework.util.StringUtils;

/**
 * 
 * @author Gavin
 * @since 0.4.0
 * @create 2012-5-24 - 上午9:20:28
 */
public class NavigationProcessor {

	private PageConfig pageConfig;
	private String eventDrivenServiceUrl;
	
	public NavigationProcessor(PageConfig pageConfig, String eventDrivenServiceUrl) {
		this.pageConfig = pageConfig;
		this.eventDrivenServiceUrl = eventDrivenServiceUrl;
	}
	
	public void process(Writer writer, String event, String action) throws IOException {
		String eventParameter = getEventParameter(pageConfig, event);
		//
		String requestUrl = this.eventDrivenServiceUrl;
		if(StringUtils.hasText(action)) {
			requestUrl = action;
		}
		//
		writer.write(requestUrl +"?"+ eventParameter);
	}
	
	private String getEventParameter(PageConfig pageConfig, String event) {
		return "event=" + pageConfig.getPath() + "|" + event;
	}
	
}
