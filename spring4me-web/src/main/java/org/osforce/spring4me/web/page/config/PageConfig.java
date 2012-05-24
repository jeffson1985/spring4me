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

import java.util.List;

import org.osforce.spring4me.web.widget.config.WidgetConfig;

/**
 * 
 * @author gavin
 * @since 0.4.0
 * @create Feb 12, 2012 - 11:28:26 AM
 */
public interface PageConfig {
    
    final String KEY = PageConfig.class.getSimpleName();
    
    String getId();
    
    String getPath();
    
    String getParent();
    
    String getTemplate();
    
    List<WidgetConfig> getAllWidgetConfig();
    
    WidgetConfig getWidgetConfig(String groupId, String widgetId);
    
    void addWidgetConfig(String groupId, WidgetConfig widgetConfig);
    
    GroupConfig getGroupConfig(String groupId);
    
    List<GroupConfig> getAllGroupConfig();
    
    void addGroupConfig(GroupConfig groupConfig);
    
}
