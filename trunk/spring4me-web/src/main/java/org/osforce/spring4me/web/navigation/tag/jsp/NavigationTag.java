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

package org.osforce.spring4me.web.navigation.tag.jsp;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.osforce.spring4me.web.Keys;
import org.osforce.spring4me.web.navigation.tag.NavigationProcessor;
import org.osforce.spring4me.web.page.config.PageConfig;
import org.osforce.spring4me.web.page.utils.PageConfigUtils;

/**
 * 
 * @author Gavin
 * @since 0.4.0
 * @create 2012-5-24 - 上午9:05:03
 */
public class NavigationTag extends TagSupport {
	private static final long serialVersionUID = -845084130157844974L;

	/**
	 * an event name
	 */
	private String event;
	
	/**
	 * an action url for form submit
	 */
	private String action;
	
	public void setEvent(String event) {
		this.event = event;
	}
	
	public void setAction(String action) {
		this.action = action;
	}
	
	@Override
	public int doStartTag() throws JspException {
		PageConfig pageConfig = PageConfigUtils.getPageConfig(pageContext.getRequest());
		String base = (String) pageContext.getRequest().getAttribute(Keys.REQUEST_KEY_BASE);
		String eventDrivenServiceUrl = (String) pageContext.getRequest().getAttribute(
				Keys.REQUEST_KEY_EVENT_DRIVEN_SERVICE_URL);
		//
		NavigationProcessor processor = new NavigationProcessor(pageConfig, eventDrivenServiceUrl);
		try {
			processor.process(pageContext.getOut(), event, base + action);
		} catch (IOException e) {
			throw new JspException(e.getMessage(), e.getCause());
		}
		return SKIP_BODY;
	}
	
}
