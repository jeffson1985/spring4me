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
import org.osforce.spring4me.web.navigation.EventNotFoundException;

/**
 * 
 * @author Gavin
 * @since 0.4.0
 * @create 2012-5-23 - 上午10:21:32
 */
public class NavigationConfig {

	private static final Log log = LogFactory.getLog(NavigationConfig.class);

	private String id;
	private String path;
	//
	private Map<String, EventConfig> eventConfigMap = new HashMap<String, EventConfig>();
	
	public NavigationConfig(String id, String path) {
		this.id = id;
		this.path = path;
	}
	
	public String getId() {
		return id;
	}
	
	public String getPath() {
		return path;
	}
	
	public EventConfig getEventConfig(String event) {
		EventConfig target = eventConfigMap.get(event);
		if(target==null) {
			throw new EventNotFoundException(String.format("Event not found for %s, " +
					"please check sitemap configuration!", event));
		}
		//
		return target;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public void addEventConfig(EventConfig eventConfig) {
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
