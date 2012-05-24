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

import org.osforce.spring4me.web.page.PageRenderException;
import org.osforce.spring4me.web.page.cache.CacheProvider;
import org.osforce.spring4me.web.page.cache.simple.SimpleCacheProvider;
import org.osforce.spring4me.web.page.config.GroupConfig;
import org.osforce.spring4me.web.page.config.PageConfig;
import org.osforce.spring4me.web.widget.config.WidgetConfig;
import org.osforce.spring4me.web.widget.http.HttpWidget;

/**
 * 
 * @author gavin
 * @since 0.4.0
 * @create Feb 12, 2012 - 11:29:08 AM
 */
public abstract class AbstractPageRender implements PageRender {
    
    private CacheProvider cacheProvider = new SimpleCacheProvider();
    //
    public AbstractPageRender() {
    }
    
    public CacheProvider getCacheProvider() {
		return cacheProvider;
	}
    
    public void setCacheProvider(CacheProvider cacheProvider) {
		this.cacheProvider = cacheProvider;
	}
    
    protected HttpWidget getCachedWidget(WidgetConfig widgetConfig) {
    	return (HttpWidget) this.cacheProvider.getCache().get(widgetConfig);
    }
    
    protected void cacheWidget(WidgetConfig widgetConfig, HttpWidget httpWidget) {
    	this.cacheProvider.getCache().put(widgetConfig, httpWidget);
    }
    
    public final void render(HttpServletRequest httpRequest, 
    		HttpServletResponse httpResponse) throws PageRenderException {
    	// hook for previous page render
    	preRender(httpRequest, httpResponse);
    	//
    	nativeRender(httpRequest, httpResponse);
    	// hook for after page render
    	postRender(httpRequest, httpResponse);
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
    
    protected abstract void nativeRender(HttpServletRequest httpRequest, HttpServletResponse httpResponse);
    
    protected void preRender(HttpServletRequest httpRequest, HttpServletResponse httpResponse){
    }
    
    protected void postRender(HttpServletRequest httpRequest, HttpServletResponse httpResponse){
    }
    
}
