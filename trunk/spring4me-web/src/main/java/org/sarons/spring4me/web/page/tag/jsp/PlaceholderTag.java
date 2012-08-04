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

package org.sarons.spring4me.web.page.tag.jsp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.sarons.spring4me.web.page.PlaceholderException;
import org.sarons.spring4me.web.page.config.PageConfig;
import org.sarons.spring4me.web.page.tag.PlaceholderCallback;
import org.sarons.spring4me.web.page.tag.PlaceholderProcessor;
import org.sarons.spring4me.web.page.utils.PageConfigUtils;
import org.sarons.spring4me.web.widget.config.WidgetConfig;
import org.sarons.spring4me.web.widget.http.HttpWidget;
import org.sarons.spring4me.web.widget.utils.WidgetUtils;

/**
 * 
 * @author gavin
 * @since 0.4.0
 * @create Feb 12, 2012 - 11:31:49 AM
 */
public class PlaceholderTag extends TagSupport {
	private static final long serialVersionUID = 7410243473016617321L;
	//
    private String groupId;

    public PlaceholderTag() {
    }

    public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
    
    @Override
    public int doStartTag() throws JspException {
        PageConfig pageConfig = PageConfigUtils.getPageConfig((HttpServletRequest)pageContext.getRequest());
        //
        PlaceholderProcessor pp = new PlaceholderProcessor(pageConfig);
		pp.setCallback(new PlaceholderCallback(){

			public HttpWidget getHttpWidget(WidgetConfig widgetConfig) throws Exception {
				return WidgetUtils.getWidget(pageContext.getRequest(), widgetConfig);
			}
			
		});
        //
		try {
			pp.process(pageContext.getOut(), groupId);
			return SKIP_BODY;
		} catch (Exception e) {
			throw new PlaceholderException(e.getMessage(), e.getCause());
		} 
    }
    
}
