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

package org.sarons.spring4me.web.page.config.xml;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sarons.spring4me.support.xml.XmlParser;
import org.sarons.spring4me.web.page.PageNotFoundException;
import org.sarons.spring4me.web.page.config.AbstractPageConfigFactory;
import org.sarons.spring4me.web.page.config.GroupConfig;
import org.sarons.spring4me.web.page.config.PageConfig;
import org.sarons.spring4me.web.widget.config.WidgetConfig;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 
 * @author gavin
 * @since 0.4.0
 * @create Feb 12, 2012 - 11:27:37 AM
 */
public class XmlPageConfigFactory extends AbstractPageConfigFactory
        implements ResourceLoaderAware {
	
    private String prefix = "/WEB-INF/pages/";
    private String suffix = ".xml";
    
    private ResourceLoader resourceLoader;
    //
    private XmlParser xmlParser = new XmlParser();
    //
	public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
	
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	
	@Override
    protected PageConfig loadPage(String pagePath) {
		try {
	        String location = getPageLocation(pagePath);
	        //
	        Resource resource = resourceLoader.getResource(location);
	        Document document = xmlParser.parseAndValidate(resource);
	        //
	        PageConfig pageConfig = parsePage(document.getDocumentElement(), pagePath);
	        //
	        return pageConfig;
		} catch (Exception e) {
			throw new PageNotFoundException(e.getMessage(), e);
		}
    }
	
	protected PageConfig parsePage(Element pageEle, String pagePath) {
		String path = pagePath;
		String id = pageEle.getAttribute("id");
    	String parent = pageEle.getAttribute("parent");
    	String layout = pageEle.getAttribute("layout");
    	//
    	PageConfig pageConfig = new PageConfig();
    	pageConfig.setId(id);
    	pageConfig.setPath(path);
    	pageConfig.setParent(parent);
    	pageConfig.setLayout(layout);
    	//
    	List<Element> groupEles = DomUtils.getChildElements(pageEle);
        for(Element groupEle : groupEles) {
        	GroupConfig groupConfig = parseGroup(groupEle, pageConfig);
        	pageConfig.addGroupConfig(groupConfig);
        }
		//
		return pageConfig;
	}
	
	private GroupConfig parseGroup(Element groupEle, PageConfig pageConfig) {
		String gid = groupEle.getAttribute("id");
        String disabled = groupEle.getAttribute("disabled");
        //
        GroupConfig groupConfig = new GroupConfig(pageConfig);
        groupConfig.setId(gid);
        groupConfig.setDisabled("true".equals(disabled));
        //
        List<Element> widgetEles = DomUtils.getChildElements(groupEle);
        for(Element widgetEle : widgetEles) {
            WidgetConfig widgetConfig = parseWidget(widgetEle, groupConfig);
        	groupConfig.addWidgetConfig(widgetConfig);
        }
		return groupConfig;
	}
	
	private WidgetConfig parseWidget(Element widgetEle, GroupConfig groupConfig) {
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
        WidgetConfig widgetConfig = new WidgetConfig(groupConfig);
        widgetConfig.setId(id);
        widgetConfig.setName(name);
        widgetConfig.setPath(path);
        widgetConfig.setCache(cache);
        widgetConfig.setDisabled("true".equals(disabled));
        //
        Element titleEle = DomUtils.getChildElementByTagName(widgetEle, "title");
        if (titleEle != null) {
            String title = titleEle.getTextContent();
            widgetConfig.setTitle(title);
        }
        //
        Element descEle = DomUtils.getChildElementByTagName(widgetEle, "description");
        if (descEle != null) {
            String description = descEle.getTextContent();
            widgetConfig.setDescription(description);
        }
        //
        Map<String, String> events = new HashMap<String, String>();
        Element eventsEle = DomUtils.getChildElementByTagName(widgetEle, "events");
        if (eventsEle != null) {
            List<Element> eventEles = DomUtils.getChildElements(eventsEle);
            for (Element eventEle : eventEles) {
                String on = eventEle.getAttribute("on");
                String to = eventEle.getAttribute("to");
                events.put(on, to);
            }
            widgetConfig.getGroupConfig().getPageConfig().setEvents(events);
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
            widgetConfig.setPreferences(preferences);
        }
        //
        return widgetConfig;
	}
    
    protected String getPageLocation(String pathPath) {
        return prefix + pathPath + suffix;
    }
    
}
