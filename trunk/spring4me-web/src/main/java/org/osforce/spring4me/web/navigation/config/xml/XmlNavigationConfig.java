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

import org.osforce.spring4me.web.navigation.config.AbstractNavigationConfig;
import org.springframework.util.Assert;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

/**
 * 
 * @author Gavin
 * @since 0.4.0
 * @create 2012-5-23 - 上午11:13:46
 */
public class XmlNavigationConfig extends AbstractNavigationConfig {
	
	private static final String ATTRIBUTE_ID = "id";
	private static final String ATTRIBUTE_PATH = "path";
	//
	private static final String ELEMENT_EVENT = "event";
	
	public XmlNavigationConfig(Element navigationEle) {
		initialize(navigationEle);
	}

	private void initialize(Element navigationEle) {
		String id = navigationEle.getAttribute(ATTRIBUTE_ID);
		String path = navigationEle.getAttribute(ATTRIBUTE_PATH);
		//
		Assert.hasText(id, "Navigation element's attribute id is required!");
		Assert.hasText(path, "Navigation element's attribute path is required!");
		//
		setId(id);
		setPath(path);
		//
		List<Element> eventEles = DomUtils.getChildElementsByTagName(navigationEle, ELEMENT_EVENT);
		for(Element eventEle : eventEles) {
			XmlEventConfig eventConfig = new XmlEventConfig(eventEle);
			addEventConfig(eventConfig);
		}
	}
	
}
