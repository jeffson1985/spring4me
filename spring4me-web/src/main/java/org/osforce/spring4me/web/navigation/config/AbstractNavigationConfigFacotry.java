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
import org.springframework.beans.factory.annotation.Value;

/**
 * 
 * @author Gavin
 * @since 0.4.0
 * @create 2012-5-23 - 上午10:36:38
 */
public abstract class AbstractNavigationConfigFacotry implements NavigationConfigFactory {
	
	protected static final Log log = LogFactory.getLog(AbstractNavigationConfigFacotry.class);

	/**
	 * Flag cache
	 */
	private boolean cacheable = true;
	
	private Map<String, NavigationConfig> cache = new HashMap<String, NavigationConfig>();
	
	public boolean isCacheable() {
		return cacheable;
	}
	
	@Value("${application.cacheable}")
	public void setCacheable(boolean cacheable) {
		this.cacheable = cacheable;
	}
	
	public NavigationConfig findNavigation(String path) {
		if(isCacheable()) {
			return getCachedNavigation(path);
		}
		//
		return loadNavigation(path);
	}
	
	private NavigationConfig getCachedNavigation(String path) {
		NavigationConfig navigationConfig = cache.get(path);
		if(navigationConfig==null) {
			synchronized (this) {
				if(navigationConfig==null) {
					navigationConfig = loadNavigation(path);
				}
				//
				if(navigationConfig!=null) {
					cache.put(path, navigationConfig);
				}
			}
		}
		//
		return navigationConfig;
	}

	protected abstract NavigationConfig loadNavigation(String path);
	
}
