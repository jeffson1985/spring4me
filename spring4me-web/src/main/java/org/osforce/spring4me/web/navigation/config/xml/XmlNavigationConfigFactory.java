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

import org.osforce.spring4me.support.xml.XmlParser;
import org.osforce.spring4me.web.navigation.NavigationNotFoundException;
import org.osforce.spring4me.web.navigation.config.AbstractNavigationConfigFacotry;
import org.osforce.spring4me.web.navigation.config.EventConfig;
import org.osforce.spring4me.web.navigation.config.NavigationConfig;
import org.springframework.core.io.Resource;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 
 * @author Gavin
 * @since 0.4.0
 * @create 2012-5-23 - 上午10:47:20
 */
public class XmlNavigationConfigFactory extends AbstractNavigationConfigFacotry {

	private XmlParser parser = new XmlParser();
	
	private Resource[] configLocations = new Resource[0];
	
	public void setConfigLocation(Resource configLocation) {
		this.configLocations = new Resource[]{configLocation};
	}
	
	public void setConfigLocations(Resource[] configLocations) {
		this.configLocations = configLocations;
	}
	
	@Override
	protected NavigationConfig loadNavigation(String idOrPath) {
		try {
			Element targetEle = findNavigationElement(idOrPath);
			//
			return parseNavigation(targetEle);
		} catch(Exception e) {
			throw new NavigationNotFoundException(String.format("Navigation not found " +
					"for path %s, please check sitemap configuration!", idOrPath), e);
		}
	}
	
	private Element findNavigationElement(String idOrPath) throws Exception {
		for(Resource configLocation : configLocations) {
			if(configLocation.isReadable()) {
				Document xmlDoc = parser.parseAndValidate(configLocation);
				List<Element> navigationEles = DomUtils.getChildElements(xmlDoc.getDocumentElement());
				for(Element navigationEle : navigationEles) {
					if(idOrPath.equals(navigationEle.getAttribute("id"))
							|| idOrPath.equals(navigationEle.getAttribute("path"))) {
						return navigationEle;
					}
				}
			}
		}
		//
		throw new NullPointerException("Navigation element not found!");
	}
	
	private NavigationConfig parseNavigation(Element navigationEle) {
		String id = navigationEle.getAttribute("id");
		String path = navigationEle.getAttribute("path");
		//
		NavigationConfig navigationConfig = new NavigationConfig(id, path);
		//
		List<Element> eventEles = DomUtils.getChildElements(navigationEle);
		for(Element eventEle : eventEles) {
			String on = eventEle.getAttribute("on");
			String to = eventEle.getAttribute("to");
			//
			EventConfig eventConfig = new EventConfig(on, to);
			//
			navigationConfig.addEventConfig(eventConfig);
		}
		//
		return navigationConfig;
	}
	
}
