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

package org.osforce.spring4me.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.GenericFilterBean;

/**
 * 
 * @author gavin
 * @since 0.4.0
 * @create Feb 12, 2012 - 11:29:37 AM
 */
public abstract class FrameworkFilter extends GenericFilterBean {

    public FrameworkFilter() {
    }

    protected void initFramework() throws ServletException {
    }

    protected void doService(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
            FilterChain chain) throws IOException, ServletException {
    }

    protected void destroyFramework() {
    }
    

    protected final void initFilterBean() throws ServletException {
        initFramework();
    }

    public final void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
    	//
    	HttpServletRequest httpRequest = (HttpServletRequest) request;
    	HttpServletResponse httpResponse = (HttpServletResponse) response;
    	//
    	doService(httpRequest, httpResponse, chain);
    }

    public final void destroy()  {
        destroyFramework();
    }
 
}
