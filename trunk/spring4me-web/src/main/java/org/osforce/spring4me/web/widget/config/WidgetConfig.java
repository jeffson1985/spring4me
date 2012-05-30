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

package org.osforce.spring4me.web.widget.config;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.osforce.spring4me.web.page.config.GroupConfig;
import org.springframework.util.StringUtils;

/**
 * 
 * @author gavin
 * @since 0.4.0
 * @create Feb 12, 2012 - 11:28:39 AM
 */
public class WidgetConfig {
    
    public static final String KEY = WidgetConfig.class.getSimpleName();
    
    private String id;
    private String name;
    private String path;
    private boolean disabled;
    private int cache;
    //
    private String title;
    private String description;
    //
    private Map<String, String> preferences = new HashMap<String, String>();
    //
    private GroupConfig groupConfig;
    
    public String getId() {
    	return id;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public int getCache() {
        return cache;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Map<String, String> getPreferences() {
        return preferences;
    }
    
    public GroupConfig getGroupConfig() {
		return groupConfig;
	}
    
    public void setId(String id) {
        if(StringUtils.hasText(id)) {
            this.id = id;
        } else {
        	this.id = UUID.randomUUID().toString();
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCache(int cache) {
        this.cache = cache;
    }
    
    public void setPreferences(Map<String, String> preferences) {
        this.preferences = preferences;
    }
    
    public void setGroupConfig(GroupConfig groupConfig) {
		this.groupConfig = groupConfig;
	}
    
    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	sb.append("{");
    	sb.append("id:").append("id");
    	sb.append(" | ");
    	sb.append("name:").append(name);
    	sb.append(" | ");
    	sb.append("path:").append("path");
    	sb.append(" | ");
    	sb.append("}");
    	//
    	return sb.toString();
    }
    
}
