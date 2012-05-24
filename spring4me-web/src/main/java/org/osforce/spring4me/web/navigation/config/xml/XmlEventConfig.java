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

import org.osforce.spring4me.web.navigation.config.AbstractEventConfig;
import org.springframework.util.Assert;
import org.w3c.dom.Element;

/**
 * 
 * @author Gavin
 * @since 0.4.0
 * @create 2012-5-23 - 上午11:05:36
 */
public class XmlEventConfig extends AbstractEventConfig {

	private static final String ATTRIBUTE_ON = "on";
	private static final String ATTRIBUTE_TO = "to";
	
	public XmlEventConfig(Element eventEle) {
		initialize(eventEle);
	}

	private void initialize(Element eventEle) {
		String on = eventEle.getAttribute(ATTRIBUTE_ON);
		String to = eventEle.getAttribute(ATTRIBUTE_TO);
		//
		Assert.hasText(on, "Event element's attribute on is required!");
		Assert.hasText(to, "Event element's attribute to is required!");
		//
		setOn(on);
		setTo(to);
	}
	
}
