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

package org.osforce.spring4me.support.remoting.config;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.parsing.CompositeComponentDefinition;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ClassUtils;
import org.w3c.dom.Element;

/**
 * 
 * @author gavin
 * @since 0.4.0
 * @create Feb 12, 2012 - 12:56:39 PM
 */
public class RemotingImporterBeanDefinitionParser implements BeanDefinitionParser {
	
	private static final String SERVICE_REGISTRY = "classpath:/service.registry";
	
	private static final Log log = LogFactory.getLog(RemotingImporterBeanDefinitionParser.class);
	
	private ResourceLoader resourceLoader = new DefaultResourceLoader();

	public BeanDefinition parse(Element element, ParserContext parserContext) {
		//
		Map<String, Class<?>> serviceDefMap = loadServiceDefinitions();
		//
		registerImporterBeans(element, parserContext, serviceDefMap);
		return null;
	}
	
	private Map<String, Class<?>> loadServiceDefinitions() {
		Map<String, Class<?>> serviceDefMap = new HashMap<String, Class<?>>();
		//
		try {
			Resource registryResource = resourceLoader.getResource(SERVICE_REGISTRY);
			if(registryResource.isReadable()) {
				Properties props = new Properties();
				props.load(registryResource.getInputStream());
				//
				for(Enumeration<?> e = props.keys(); e.hasMoreElements();) {
					String serviceName = (String) e.nextElement();
					String serviceClassName = props.getProperty(serviceName);
					Class<?> serviceClass = ClassUtils.forName(serviceClassName, resourceLoader.getClassLoader());
					//
					serviceDefMap.put(serviceName, serviceClass);
				}
			}
		} catch (Exception e) {
			log.error(e);
		} 
		return serviceDefMap;
	}
	
	private void registerImporterBeans(Element element, ParserContext parserContext, Map<String, Class<?>> serviceDefMap) {
		Object source = parserContext.extractSource(element);
        //
        CompositeComponentDefinition compDefinition = new CompositeComponentDefinition(element.getTagName(), source);
        parserContext.pushContainingComponent(compDefinition);
        //
		//
		for(Iterator<String> iter = serviceDefMap.keySet().iterator(); iter.hasNext();) {
			String serviceName = iter.next();
			Class<?> serviceInterface = serviceDefMap.get(serviceName);
			//
			registerImporterBean(element, parserContext, serviceName, serviceInterface);
		}
	}
	
	private void registerImporterBean(Element element, ParserContext parserContext,
			String serviceName, Class<?> serviceInterface) {
		//
		String parentImporter = element.getAttribute("importer");
		BeanDefinition parentImporterBeanDef = parserContext.getRegistry().getBeanDefinition(parentImporter);
		PropertyValue serviceUrlPV = parentImporterBeanDef.getPropertyValues().getPropertyValue("serviceUrl");
        //
        String serviceUrl = ((TypedStringValue)serviceUrlPV.getValue()).getValue();
        String remoteServiceUrl = getRemoteServiceUrl(serviceUrl, serviceName);
        //
        BeanDefinition proxyFactoryBeanDef = new GenericBeanDefinition();
        proxyFactoryBeanDef.setParentName(parentImporter);
        proxyFactoryBeanDef.getPropertyValues().addPropertyValue("serviceUrl", remoteServiceUrl);
        proxyFactoryBeanDef.getPropertyValues().addPropertyValue("serviceInterface", serviceInterface);
        //
        String beanName = serviceName;
        parserContext.getRegistry().registerBeanDefinition(beanName, proxyFactoryBeanDef);
        //
        parserContext.registerComponent(new BeanComponentDefinition(proxyFactoryBeanDef, beanName));
	}
	
	private String getRemoteServiceUrl(String serviceUrl, String serviceName) {
		if(serviceName.startsWith("/")) {
			return serviceUrl + serviceName;
		}
		return serviceUrl + "/" + serviceName;
	}

}
