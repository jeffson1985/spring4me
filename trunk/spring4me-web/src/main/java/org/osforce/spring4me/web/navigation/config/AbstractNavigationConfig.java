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

package org.osforce.spring4me.web.navigation.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author Gavin
 * @since 0.4.0
 * @create 2012-5-23 - 上午11:00:37
 */
public abstract class AbstractNavigationConfig implements NavigationConfig {
	
	protected static final Log log = LogFactory.getLog(AbstractNavigationConfig.class);

	private String id;
	private String path;
	//
	private Map<String, EventConfig> eventConfigMap = new HashMap<String, EventConfig>();
	
	public String getId() {
		return id;
	}
	
	public String getPath() {
		return path;
	}
	
	public EventConfig getEventConfig(String event) {
		return eventConfigMap.get(event);
	}
	
	protected void setId(String id) {
		this.id = id;
	}
	
	protected void setPath(String path) {
		this.path = path;
	}
	
	protected void addEventConfig(EventConfig eventConfig) {
		//
		if(log.isDebugEnabled()) {
			log.debug("Found event config " + eventConfig);
		}
		this.eventConfigMap.put(eventConfig.getOn(), eventConfig);
	}
	
	/**
	 * {id:id | path:path}
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("id:").append(id);
		sb.append(" | ");
		sb.append("path:").append(path);
		sb.append("}");
		return sb.toString();
	}
	
}
