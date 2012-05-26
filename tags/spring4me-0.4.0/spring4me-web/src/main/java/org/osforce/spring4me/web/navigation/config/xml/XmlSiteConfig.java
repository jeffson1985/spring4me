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

package org.osforce.spring4me.web.navigation.config.xml;

import java.util.List;

import org.osforce.spring4me.web.navigation.config.AbstractSiteConfig;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

/**
 * 
 * @author Gavin
 * @since 0.4.0
 * @create 2012-5-23 - 上午11:25:58
 */
public class XmlSiteConfig extends AbstractSiteConfig {

	private static final String ELEMENT_NAVIGATION = "navigation";
	
	public XmlSiteConfig(Element siteEle) {
		initialize(siteEle);
	}

	private void initialize(Element siteEle) {
		List<Element> navigationEles = DomUtils.getChildElementsByTagName(siteEle, ELEMENT_NAVIGATION);
		for(Element navigationEle : navigationEles) {
			XmlNavigationConfig navigationConfig = new XmlNavigationConfig(navigationEle);
			addNavigationConfig(navigationConfig);
		}
	}
	
}
