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

package org.sarons.spring4me.web.cache.simple;

import java.util.LinkedHashMap;
import java.util.Map;

import org.sarons.spring4me.web.cache.Cache;
import org.sarons.spring4me.web.widget.config.WidgetConfig;

/**
 * 
 * @author gavin
 * @since 0.4.0
 * @create Feb 12, 2012 - 11:26:35 AM
 */
public class HttpWidgetCache implements Cache {

	private Map<Object, Long> timeCache = new LinkedHashMap<Object, Long>();
	private Map<Object, Object> dataCache = new LinkedHashMap<Object, Object>();
	
	public Object get(Object key) {
		int cache = ((WidgetConfig) key).getCache();
		if(cache==0) {
			return dataCache.get(key);
		}
		if(cache>0 && timeCache.containsKey(key)) {
			long create = timeCache.get(key);
			long now = System.currentTimeMillis();
			return (create + cache * 1000) > now ? dataCache.get(key) : null;
		}
		return null;
	}

	public void put(Object key, Object value) {
		int cache = ((WidgetConfig) key).getCache();
		if(cache>=0) {
			long create = System.currentTimeMillis();
			timeCache.put(key, create);
			dataCache.put(key, value);
		}
	}

}
