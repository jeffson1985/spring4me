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

package org.sarons.spring4me.web.page.utils;

import javax.servlet.http.HttpServletRequest;

import org.sarons.spring4me.web.page.config.PageConfig;
import org.springframework.web.context.request.WebRequest;

/**
 * 
 * @author gavin
 * @since 0.4.0
 * @create Feb 12, 2012 - 11:32:22 AM
 */
public abstract class PageConfigUtils {
	
	public static PageConfig getPageConfig(WebRequest webRequest) {
		return (PageConfig) webRequest.getAttribute(PageConfig.KEY, WebRequest.SCOPE_SESSION);
	}
    
    public static PageConfig getPageConfig(HttpServletRequest request) {
        return (PageConfig) request.getSession().getAttribute(PageConfig.KEY);
    }
    
    public static void setPageConfig(HttpServletRequest request, PageConfig pageConfig) {
    	request.getSession().setAttribute(PageConfig.KEY, pageConfig);
    }
    
}
