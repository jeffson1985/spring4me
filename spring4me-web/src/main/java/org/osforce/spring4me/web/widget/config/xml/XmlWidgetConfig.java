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

package org.osforce.spring4me.web.widget.config.xml;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osforce.spring4me.web.page.config.GroupConfig;
import org.osforce.spring4me.web.widget.config.AbstractWidgetConfig;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

/**
 * 
 * @author gavin
 * @since 0.4.0
 * @create Feb 12, 2012 - 11:27:42 AM
 */
public class XmlWidgetConfig extends AbstractWidgetConfig {
	
    public XmlWidgetConfig(Element widgetEle, GroupConfig groupConfig) {
        initialize(widgetEle, groupConfig);
    }
    
    private void initialize(Element widgetEle, GroupConfig groupConfig) {
    	setGroupConfig(groupConfig);
        //
        String id = widgetEle.getAttribute("id");
        String name = widgetEle.getAttribute("name");
        String path = widgetEle.getAttribute("path");
        String cacheStr = widgetEle.getAttribute("cache");
        String disabled = widgetEle.getAttribute("disabled");
        //
        int cache = -1;
        if(StringUtils.hasText(cacheStr)) {
        	cache = Integer.valueOf(cacheStr);
        }
        //
        setId(id);
        setName(name);
        setPath(path);
        setCache(cache);
        setDisabled("true".equals(disabled));
        //
        Element titleEle = DomUtils.getChildElementByTagName(widgetEle, "title");
        if (titleEle != null) {
            String title = titleEle.getTextContent();
            setTitle(title);
        }
        //
        Element descEle = DomUtils.getChildElementByTagName(widgetEle, "description");
        if (descEle != null) {
            String description = descEle.getTextContent();
            setDescription(description);
        }
        //
        Map<String, String> preferences = new HashMap<String, String>();
        Element prefEle = DomUtils.getChildElementByTagName(widgetEle, "preference");
        if (prefEle != null) {
            List<Element> keyValueEles = DomUtils.getChildElements(prefEle);
            for (Element keyValueEle : keyValueEles) {
                String key = keyValueEle.getTagName();
                String value = keyValueEle.getTextContent();
                preferences.put(key, value);
            }
            super.setPreferences(preferences);
        }
    }

}
