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

package org.osforce.spring4me.web.page.render;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osforce.spring4me.web.cache.CacheProvider;
import org.osforce.spring4me.web.cache.simple.HttpWidgetCacheProvider;
import org.osforce.spring4me.web.page.PageRenderException;
import org.osforce.spring4me.web.page.config.GroupConfig;
import org.osforce.spring4me.web.page.config.PageConfig;
import org.osforce.spring4me.web.page.utils.PageConfigUtils;
import org.osforce.spring4me.web.widget.config.WidgetConfig;
import org.osforce.spring4me.web.widget.http.HttpWidget;
import org.osforce.spring4me.web.widget.http.HttpWidgetRequest;
import org.osforce.spring4me.web.widget.http.HttpWidgetResponse;
import org.osforce.spring4me.web.widget.http.impl.DefaultHttpWidget;
import org.osforce.spring4me.web.widget.utils.WidgetConfigUtils;
import org.osforce.spring4me.web.widget.utils.WidgetUtils;

/**
 * 
 * @author gavin
 * @since 0.4.0
 * @create Feb 12, 2012 - 11:29:08 AM
 */
public abstract class AbstractPageRender implements PageRender {
    
    private CacheProvider httpWidgetCacheProvider = new HttpWidgetCacheProvider();
    //
    public AbstractPageRender() {
    }
    
    public CacheProvider getHttpWidgetCacheProvider() {
		return httpWidgetCacheProvider;
	}
    
    public void setHttpWidgetCacheProvider(CacheProvider httpWidgetCacheProvider) {
		this.httpWidgetCacheProvider = httpWidgetCacheProvider;
	}
    
    protected HttpWidget getCachedWidget(WidgetConfig widgetConfig) {
    	return (HttpWidget) this.httpWidgetCacheProvider.getCache().get(widgetConfig);
    }
    
    public final void render(HttpServletRequest httpRequest, 
    		HttpServletResponse httpResponse) throws PageRenderException {
    	//
    	PageConfig pageConfig = PageConfigUtils.getPageConfig(httpRequest);
    	// hook for previous page render
    	prePageRender(pageConfig, httpRequest, httpResponse);
    	//
    	nativePageRender(pageConfig, httpRequest, httpResponse);
    	// hook for after page render
    	postPageRender(pageConfig, httpRequest, httpResponse);
    }
    
    protected List<WidgetConfig> getWidgetConfigList(PageConfig pageConfig) {
    	List<WidgetConfig> widgetConfigList = new ArrayList<WidgetConfig>();
        for (GroupConfig groupConfig : pageConfig.getAllGroupConfig()) {
            if (groupConfig.isDisabled()) {
                continue;
            }
            //
            for (WidgetConfig widgetConfig : groupConfig.getAllWidgetConfig()) {
                if (groupConfig.isDisabled()) {
                    continue;
                }
                //
                widgetConfigList.add(widgetConfig);
            }
        }
        return widgetConfigList;
    }
    
    protected abstract void nativePageRender(PageConfig pageConfig, 
    		HttpServletRequest httpRequest, HttpServletResponse httpResponse);
    
    protected void prePageRender(PageConfig pageConfig, 
    		HttpServletRequest httpRequest, HttpServletResponse httpResponse){
    }
    
    protected void postPageRender(PageConfig pageConfig, 
    		HttpServletRequest httpRequest, HttpServletResponse httpResponse){
    }
    
    protected void preWidgetRender(WidgetConfig widgetConfig,
    		HttpWidgetRequest widgetRequest, HttpWidgetResponse widgetResponse) {
    	WidgetConfigUtils.setWidgetConfig(widgetRequest, widgetConfig);
    }
    
    protected void postWidgetRender(WidgetConfig widgetConfig,
    		HttpWidgetRequest widgetRequest, HttpWidgetResponse widgetResponse) {
    	//
        HttpWidget httpWidget = new DefaultHttpWidget(widgetRequest, widgetResponse);
        //
        WidgetUtils.setWidget(widgetRequest, widgetConfig, httpWidget);
        // cache http widget if needs
        getHttpWidgetCacheProvider().getCache().put(widgetConfig, httpWidget);
    }
    
}
