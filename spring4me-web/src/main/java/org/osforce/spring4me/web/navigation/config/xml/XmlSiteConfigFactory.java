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

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.osforce.spring4me.web.navigation.config.AbstractSiteConfigFacotry;
import org.osforce.spring4me.web.navigation.config.MultipleSiteConfig;
import org.osforce.spring4me.web.navigation.config.SiteConfig;
import org.springframework.core.io.Resource;
import org.w3c.dom.Document;

/**
 * 
 * @author Gavin
 * @since 0.4.0
 * @create 2012-5-23 - 上午10:47:20
 */
public class XmlSiteConfigFactory extends AbstractSiteConfigFacotry {

	private Resource[] configLocations = new Resource[0];
	
	public void setConfigLocation(Resource configLocation) {
		this.configLocations = new Resource[]{configLocation};
	}
	
	public void setConfigLocations(Resource[] configLocations) {
		this.configLocations = configLocations;
	}
	
	@Override
	protected SiteConfig loadSite() {
		List<SiteConfig> siteConfigList = new ArrayList<SiteConfig>();
		for(Resource configLocation : configLocations) {
			if(configLocation.isReadable()) {
				Document siteDoc = getSiteDocument(configLocation);
				if(siteDoc!=null) {
					XmlSiteConfig siteConfig = new XmlSiteConfig(siteDoc.getDocumentElement());
					siteConfigList.add(siteConfig);
				}
			}
		}
		//
		return new MultipleSiteConfig(siteConfigList);
	}
	
	private Document getSiteDocument(Resource resource) {
		try {
			return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(resource.getFile());
		} catch (Exception e) {
			log.warn("Parse site map config file failed!", e);
		}
		return null;
	}

}
