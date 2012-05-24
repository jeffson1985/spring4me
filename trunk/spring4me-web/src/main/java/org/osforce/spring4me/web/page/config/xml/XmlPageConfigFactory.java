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

package org.osforce.spring4me.web.page.config.xml;

import java.util.List;

import org.osforce.spring4me.web.page.PageNotFoundException;
import org.osforce.spring4me.web.page.config.AbstractPageConfigFactory;
import org.osforce.spring4me.web.page.config.GroupConfig;
import org.osforce.spring4me.web.page.config.PageConfig;
import org.springframework.beans.factory.xml.DefaultDocumentLoader;
import org.springframework.beans.factory.xml.DocumentLoader;
import org.springframework.beans.factory.xml.PluggableSchemaResolver;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ClassUtils;
import org.springframework.util.xml.XmlValidationModeDetector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;

/**
 * 
 * @author gavin
 * @since 0.4.0
 * @create Feb 12, 2012 - 11:27:37 AM
 */
public class XmlPageConfigFactory extends AbstractPageConfigFactory
        implements ResourceLoaderAware {
	private static final String SCHEMA_MAPPINGS_LOCATION = "META-INF/spring4me.schemas";
	//
	private DocumentLoader documentLoader = new DefaultDocumentLoader();
	private ErrorHandler errorHandler = new DefaultHandler();
	private XmlValidationModeDetector validationModeDetector = new XmlValidationModeDetector();
	private EntityResolver entityResolver = new PluggableSchemaResolver(ClassUtils.getDefaultClassLoader(), SCHEMA_MAPPINGS_LOCATION);
	//
    private String prefix = "/WEB-INF/pages";
    private String suffix = ".xml";
    
    private ResourceLoader resourceLoader;
    //
	public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
	
	@Override
    protected PageConfig loadPage(String pagePath) {
        String location = getPageLocation(pagePath);
        //
        Document document = getPageDocument(location);
        Element pageEle = document.getDocumentElement();
        XmlPageConfig xmlPageConfig = new XmlPageConfig(pageEle, pagePath);
        //
        List<GroupConfig> xmlGroupConfigCollection = xmlPageConfig.getAllGroupConfig();
        for(GroupConfig groupConfig : xmlGroupConfigCollection) {
        	XmlGroupConfig xmlGroupConfig = (XmlGroupConfig) groupConfig;
        	xmlGroupConfig.getAllWidgetConfig();
        }
        //
        return xmlPageConfig;
    }
    
    protected String getPageLocation(String pathPath) {
        return prefix + pathPath + suffix;
    }
    
    private Document getPageDocument(String location) {
        try {
        	Resource resource = resourceLoader.getResource(location);
            //
            InputSource inputSource = new InputSource(resource.getInputStream());
    		int validationMode = validationModeDetector.detectValidationMode(resource.getInputStream());
    		return documentLoader.loadDocument(inputSource, entityResolver, errorHandler, validationMode, false);
        } catch (Exception e) {
            throw new PageNotFoundException(e.getMessage(), e.getCause());
        } 
    }
	
}
