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

package org.osforce.spring4me.web.page.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osforce.spring4me.web.widget.config.WidgetConfig;

/**
 * 
 * @author gavin
 * @since 0.4.0
 * @create Feb 12, 2012 - 11:27:56 AM
 */
public abstract class AbstractGroupConfig implements GroupConfig {

    private String id;
    private String layout;
    private boolean disabled;
    //
    private PageConfig pageConfig;
    //
    private List<WidgetConfig> widgetConfigList = new ArrayList<WidgetConfig>();
    private Map<String, WidgetConfig> widgetConfigMap = new HashMap<String, WidgetConfig>();
    
    public String getId() {
        return id;
    }

    public String getLayout() {
        return layout;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public List<WidgetConfig> getAllWidgetConfig() {
    	return widgetConfigList;
    }

    public WidgetConfig getWidgetConfig(String widgetId) {
        return widgetConfigMap.get(widgetId);
    }
    
    public void addWidgetConfig(WidgetConfig widgetConfig) {
    	this.widgetConfigList.add(widgetConfig);
        this.widgetConfigMap.put(widgetConfig.getId(), widgetConfig);
    }

    public PageConfig getPageConfig() {
        return pageConfig;
    }

    protected void setId(String id) {
        this.id = id;
    }

    protected void setLayout(String layout) {
        this.layout = layout;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    protected void setPageConfig(PageConfig pageConfig) {
        this.pageConfig = pageConfig;
    }
    
}
