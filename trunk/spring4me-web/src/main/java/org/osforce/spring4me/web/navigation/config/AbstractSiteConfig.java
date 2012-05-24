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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author Gavin
 * @since 0.4.0
 * @create 2012-5-23 - 上午10:53:04
 */
public class AbstractSiteConfig implements SiteConfig {
	
	protected static final Log log = LogFactory.getLog(AbstractSiteConfig.class);
	
	private Map<String, NavigationConfig> navigationConfigMap = new HashMap<String, NavigationConfig>();

	public NavigationConfig getNavigationConfig(String id) {
		return navigationConfigMap.get(id);
	}

	public List<NavigationConfig> getAllNavigationConfig() {
		return new ArrayList<NavigationConfig>(navigationConfigMap.values());
	}
	
	protected void addNavigationConfig(NavigationConfig navigationConfig) {
		//
		if(log.isDebugEnabled()) {
			log.debug("Found navigation config " + navigationConfig);
		}
		//
		this.navigationConfigMap.put(navigationConfig.getId(), navigationConfig);
	}

}
