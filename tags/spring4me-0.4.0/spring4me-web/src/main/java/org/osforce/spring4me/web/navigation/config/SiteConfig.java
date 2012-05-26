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

import java.util.List;

/**
 * <site>
 *	<navigation id="index" path="/index">
 *		<event on="view-profile" to="" />
 *	</navigation>
 *  <navigation id="viewProfile" path="/profile">
 *		<event on="view-profile" to="viewProfile" />
 *	</navigation>
 * </site>
 * 
 * 
 * @author Gavin
 * @since 0.4.0
 * @create 2012-3-24 - 下午2:02:05
 */
public interface SiteConfig {
	
	NavigationConfig getNavigationConfig(String id);
	
	List<NavigationConfig> getAllNavigationConfig();
	
}
