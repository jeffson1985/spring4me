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

package org.sarons.spring4me.web.filter;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.FilterChain;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sarons.spring4me.web.page.PageNotFoundException;
import org.sarons.spring4me.web.page.config.PageConfig;
import org.sarons.spring4me.web.page.config.PageConfigFactory;
import org.sarons.spring4me.web.page.render.PageRender;
import org.sarons.spring4me.web.page.utils.PageConfigUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.util.UrlPathHelper;

/**
 * 
 * @author gavin
 * @since 0.4.0
 * @create Feb 12, 2012 - 11:29:43 AM
 */
public class PageFilter extends FrameworkFilter {
	
    private static final String CONTEXT_CONFIG_LOCATION = "/WEB-INF/spring4me/pageContext.xml";
    //
    private String namespace = PageFilter.class.getSimpleName();
    
    private String pageConfigLocation = CONTEXT_CONFIG_LOCATION;
    //
    private WebApplicationContext pageApplicationContext;
    
    private Class<?> contextClass = XmlWebApplicationContext.class;
    //
    private String welcomePage = "/index";
    //
    private PageRender pageRender;
    
    private PageConfigFactory pageConfigFactory;
    //
    private UrlPathHelper urlPathHelper = new UrlPathHelper();
    //
    public PageFilter() {
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
    
    public void setWelcomePage(String welcomePage) {
		this.welcomePage = welcomePage;
	}
    
    public String getWidgetConfigLocation() {
        return pageConfigLocation;
    }

    public void setPageConfigLocation(String pageConfigLocation) {
		this.pageConfigLocation = pageConfigLocation;
	}

    public void setContextClass(Class<?> contextClass) {
        this.contextClass = contextClass;
    }

    public WebApplicationContext getPageApplicationContext() {
        return pageApplicationContext;
    }

    public PageConfigFactory getPageConfigFactory() {
        return pageConfigFactory;
    }
    
    @Override
    protected void initFramework() throws ServletException {
       this.pageApplicationContext = initPageApplicationContext();
       //
       this.pageRender = getPageApplicationContext().getBean(PageRender.class);
       this.pageConfigFactory = getPageApplicationContext().getBean(PageConfigFactory.class);
    }

    @Override
    protected void doService(HttpServletRequest httpRequest, 
            HttpServletResponse httpResponse, FilterChain chain) throws IOException, ServletException {
    	try {
    		PageConfig pageConfig = getPageConfig(httpRequest);
    		PageConfigUtils.setPageConfig(httpRequest, pageConfig);
    		//
            this.pageRender.render(httpRequest, httpResponse);
            // forward this request and stop continue filter
            String pagePath = pageConfig.getPath() + ".page";
            httpRequest.getRequestDispatcher(pagePath).forward(httpRequest, httpResponse);
            //
    	} catch(PageNotFoundException e) {
    		//
    		chain.doFilter(httpRequest, httpResponse);
    	}    	
    }

    protected ConfigurableWebApplicationContext initPageApplicationContext() {
        WebApplicationContext root = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
        //
        ConfigurableWebApplicationContext wac = (ConfigurableWebApplicationContext) BeanUtils.instantiateClass(contextClass);
        wac.setParent(root);
        wac.setServletContext(getServletContext());
		wac.setServletConfig(new DelegatingServletConfig(getServletContext()));
		wac.setNamespace(getNamespace());
		wac.setConfigLocation(getWidgetConfigLocation());
		wac.refresh();
        //
		return wac;
    }
    
    protected PageConfig getPageConfig(HttpServletRequest httpRequest) {
        String requestUri = urlPathHelper.getRequestUri(httpRequest);
        String contextPath = urlPathHelper.getContextPath(httpRequest);
        String pagePath = requestUri.substring(contextPath.length());
        //
        if(!StringUtils.hasText(pagePath) || "/".equals(pagePath)) {
        	pagePath = welcomePage;
        }
        //
        return getPageConfigFactory().findPage(pagePath);
    }
    
    private class DelegatingServletConfig implements ServletConfig {
        
        private ServletContext servletContext;

        public DelegatingServletConfig(ServletContext servletContext) {
            this.servletContext = servletContext;
        }
        
        public String getServletName() {
            return this.servletContext.getServletContextName();
        }

        public ServletContext getServletContext() {
            return this.servletContext;
        }

        public String getInitParameter(String name) {
            return this.servletContext.getInitParameter(name);
        }

        public Enumeration<?> getInitParameterNames() {
            return this.servletContext.getInitParameterNames();
        }
        
    }
    
}
