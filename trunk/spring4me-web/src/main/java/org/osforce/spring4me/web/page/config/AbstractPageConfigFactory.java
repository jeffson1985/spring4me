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

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

/**
 * 
 * @author gavin
 * @since 0.4.0
 * @create Feb 12, 2012 - 11:28:07 AM
 */
public abstract class AbstractPageConfigFactory implements PageConfigFactory {

	private boolean cacheable = true;
	//
	private Map<String, PageConfig> pageCache = new HashMap<String, PageConfig>();

	public boolean isCacheable() {
		return cacheable;
	}

	@Value("${application.cacheable}")
	public void setCacheable(boolean cacheable) {
		this.cacheable = cacheable;
	}

	public PageConfig findPage(String path) {
		if (isCacheable()) {
			return getCachedPage(path);
		}
		//
		PageConfig pageConfig = loadPage(path);
		if (StringUtils.hasText(pageConfig.getParent())) {
			PageConfig parentPageConfig = loadPage(pageConfig.getParent());
			margePageConfig(pageConfig, parentPageConfig);
		}
		//
		return pageConfig;
	}

	protected PageConfig getCachedPage(String pagePath) {
		PageConfig pageConfig = pageCache.get(pagePath);
		if (pageConfig == null) {
			synchronized (this) {
				if (pageConfig == null) {
					pageConfig = loadPage(pagePath);
				}
				//
				if (pageConfig != null) {
					pageCache.put(pagePath, pageConfig);
					pageCache.put(pageConfig.getId(), pageConfig);
				}
			}
		}
		return pageConfig;
	}

	private void margePageConfig(PageConfig pageConfig, PageConfig parentPageConfig) {
		for (GroupConfig groupConfig : parentPageConfig.getAllGroupConfig()) {
			if (pageConfig.getGroupConfig(groupConfig.getId()) == null) {
				pageConfig.addGroupConfig(groupConfig);
			}
		}
	}
	
	protected abstract PageConfig loadPage(String pagePath);
	
}
