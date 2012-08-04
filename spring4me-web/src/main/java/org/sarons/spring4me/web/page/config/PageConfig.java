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

package org.sarons.spring4me.web.page.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.sarons.spring4me.web.widget.config.WidgetConfig;
import org.springframework.util.StringUtils;

/**
 * 
 * @author gavin
 * @since 0.4.0
 * @create Feb 12, 2012 - 11:28:26 AM
 */
public class PageConfig {
    
    public static final String KEY = PageConfig.class.getSimpleName();
    
    private String id;
    private String path;
    private String parent;
    private String layout;
    //
    private Map<String, WidgetConfig> widgetConfigMap = new HashMap<String, WidgetConfig>();
    private Map<String, GroupConfig> groupConfigMap = new LinkedHashMap<String, GroupConfig>();
    //
    private Map<String, String> events = new HashMap<String, String>();
    
    public String getId() {
    	if(!StringUtils.hasText(id)) {
    		this.id = UUID.randomUUID().toString();
    	}
    	return id;
    }
    
    public String getPath() {
        return path;
    }
    
    public String getParent() {
		return parent;
	}

    public String getLayout() {
    	return layout;
    }
    
    public Map<String, String> getEvents() {
		return events;
	}
    
    public List<WidgetConfig> getAllWidgetConfig() {
        List<WidgetConfig> allWidgetConfig = new ArrayList<WidgetConfig>();
        //
        List<GroupConfig> allGroupConfig = new ArrayList<GroupConfig>(groupConfigMap.values());
        for(GroupConfig groupConfig : allGroupConfig) {
            allWidgetConfig.addAll(groupConfig.getAllWidgetConfig());
        }
        return allWidgetConfig;
    }

    public WidgetConfig getWidgetConfig(String groupId, String widgetId) {
        GroupConfig groupConfig = groupConfigMap.get(groupId);
        if(groupConfig==null) {
            return null;
        }
        WidgetConfig widgetConfig = groupConfig.getWidgetConfig(widgetId);
        if(widgetConfig==null) {
            return null;
        }
        return widgetConfig;
    }
    
    public WidgetConfig getWidgetConfig(String path) {
    	return this.widgetConfigMap.get(path);
    }

    public void addWidgetConfig(String groupId, WidgetConfig widgetConfig) {
    	GroupConfig groupConfig = this.groupConfigMap.get(groupId);
    	groupConfig.addWidgetConfig(widgetConfig);
    }
    
    public List<GroupConfig> getAllGroupConfig() {
        return new ArrayList<GroupConfig>(groupConfigMap.values());
    }

    public GroupConfig getGroupConfig(String groupId) {
        return groupConfigMap.get(groupId);
    }
    

	public void addGroupConfig(GroupConfig groupConfig) {
		//
		for(WidgetConfig widgetConfig : groupConfig.getAllWidgetConfig()) {
			this.widgetConfigMap.put(widgetConfig.getPath(), widgetConfig);
		}
		//
		this.groupConfigMap.put(groupConfig.getId(), groupConfig);
	}
	
	public void setId(String id) {
		this.id = id;
	}

    public void setPath(String path) {
        this.path = path;
    }
    
    public void setParent(String parent) {
		this.parent = parent;
	}

    public void setLayout(String layout) {
		this.layout = layout;
	}
    
    public void setEvents(Map<String, String> events) {
		this.events = events;
	}
    
}
