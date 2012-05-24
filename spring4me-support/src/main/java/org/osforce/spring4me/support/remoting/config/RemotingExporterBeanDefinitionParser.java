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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.HierarchicalBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.parsing.CompositeComponentDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.remoting.rmi.RmiServiceExporter;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.ClassUtils;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/**
 * 
 * @author gavin
 * @since 0.4.0
 * @create Feb 12, 2012 - 12:56:00 PM
 */
public class RemotingExporterBeanDefinitionParser implements BeanDefinitionParser {
	private Log log = LogFactory.getLog(RemotingExporterBeanDefinitionParser.class);
	
	private PathMatcher pathMatcher = new AntPathMatcher();
	private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
	private MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(this.resourcePatternResolver);
	
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		Set<String> serviceClassSet = findServiceClasses(element);
		//
		Map<String, BeanDefinition> beanDefMap = walkBeanDefinitionRegistry(parserContext.getRegistry(), serviceClassSet);
		//
		registerExporterBeans(element, parserContext, beanDefMap);
		//
		storeExporterRegistry(element, beanDefMap);
		//
		return null;
	}
	
	private void storeExporterRegistry(Element element, Map<String, BeanDefinition> beanDefMap) {
		Properties props = new Properties();
		try {
			for (Iterator<Entry<String, BeanDefinition>> iter = beanDefMap.entrySet().iterator(); iter.hasNext();) {
	            Entry<String, BeanDefinition> beanDefEntry = iter.next();
	            String beanName = (String) beanDefEntry.getKey();
	            BeanDefinition beanDef = (BeanDefinition) beanDefEntry.getValue();
	            //
	            Class<?> serviceClass = ClassUtils.forName(beanDef.getBeanClassName(), getClass().getClassLoader());
	            Class<?> serviceInterface = findServiceInterface(element, serviceClass);
	            props.setProperty(StringUtils.capitalize(beanName), serviceInterface.getName());
	        }
			//
			String fileName = System.getProperty("java.io.tmpdir") + File.separator + "service.registry";
			OutputStream out = new FileOutputStream(fileName);
			props.store(out, "auto generate by remote serive exporter!");
		} catch (Exception e) {
			log.error(e);
		}
	}

	private Set<String> findServiceClasses(Element element) {
		Set<String> serviceClassSet = new HashSet<String>();
		String[] basePackages = getPackagePatterns(element);
		//
		try {
			for(String basePackage : basePackages) {
				String packagePattern = ClassUtils.convertClassNameToResourcePath(basePackage) + "/**/*.class";
				Resource[] resources = resourcePatternResolver.getResources(packagePattern);
				for(Resource resource : resources) {
					if(resource.isReadable()) {
						MetadataReader metadataReader = this.metadataReaderFactory.getMetadataReader(resource);
						if(metadataReader.getAnnotationMetadata().hasAnnotation(Service.class.getName())) {
							serviceClassSet.add(metadataReader.getClassMetadata().getClassName());
						}
					}
				}
			}
		} catch (IOException e) {
			throw new BeanDefinitionStoreException("I/O failure during classpath scanning", e);
		}
		return serviceClassSet;
	}
	
	private Class<?> findServiceInterface(Element element, Class<?> serviceClass) {
		Class<?>[] interfaceClasses = ClassUtils.getAllInterfacesForClass(serviceClass);
		if(interfaceClasses.length==1) {
			return interfaceClasses[0];
		}
		//
		String[] basePackages = getPackagePatterns(element);
		//
		for(Class<?> interfaceClass : interfaceClasses) {
			for(String basePackage : basePackages) {
				if(interfaceClass.getName().startsWith(basePackage)) {
					return interfaceClass;
				}
			}
		}
		//
		return null;
	}
	
	private Map<String, BeanDefinition> walkBeanDefinitionRegistry(BeanDefinitionRegistry registry, Set<String> classSet) {
        Map<String, BeanDefinition> beanDefMap = new HashMap<String, BeanDefinition>();
        //
        String[] beanNames = registry.getBeanDefinitionNames();
        for (int i = 0; i < beanNames.length; i++) {
            String beanName = beanNames[i];
            BeanDefinition beanDef = registry.getBeanDefinition(beanName);
            if(classSet.contains(beanDef.getBeanClassName())) {
            	beanDefMap.put(beanName, beanDef);
            }
        }
        //
        if(registry instanceof HierarchicalBeanFactory) {
        	HierarchicalBeanFactory beanFactory = (HierarchicalBeanFactory) registry;
        	BeanFactory parentBeanFactory = beanFactory.getParentBeanFactory();
        	if(parentBeanFactory!=null && parentBeanFactory instanceof BeanDefinitionRegistry) {
        		Map<String, BeanDefinition> parentBeanDefMap = walkBeanDefinitionRegistry(
        				(BeanDefinitionRegistry)parentBeanFactory, classSet);
        		beanDefMap.putAll(parentBeanDefMap);
        	}
        }
        //
        return beanDefMap;
    }
	
	private void registerExporterBeans(Element element, ParserContext parserContext, Map<String, BeanDefinition> beanDefMap) {
        Object source = parserContext.extractSource(element);
        //
        CompositeComponentDefinition compDefinition = new CompositeComponentDefinition(element.getTagName(), source);
        parserContext.pushContainingComponent(compDefinition);
        //
        for (Iterator<Entry<String, BeanDefinition>> iter = beanDefMap.entrySet().iterator(); iter.hasNext();) {
            Entry<String, BeanDefinition> beanDefEntry = iter.next();
            String beanName = (String) beanDefEntry.getKey();
            BeanDefinition beanDef = (BeanDefinition) beanDefEntry.getValue();
            //
            registerExporterBean(element, parserContext, beanName, beanDef);
        }
    }

    private void registerExporterBean(Element element, ParserContext parserContext,
                                      String serviceName, BeanDefinition serviceBeanDef) {
        String parentExporter = element.getAttribute("exporter");
        BeanDefinition parentExporterBeanDef = parserContext.getRegistry().getBeanDefinition(parentExporter);
        //
        RuntimeBeanReference serviceBeanReference = new RuntimeBeanReference(serviceName);
        //
        try {
            Class<?> serviceClass = ClassUtils.forName(serviceBeanDef.getBeanClassName(), getClass().getClassLoader());
            Class<?> exporterClass = ClassUtils.forName(parentExporterBeanDef.getBeanClassName(), getClass().getClassLoader());
            //
            Class<?>[] serviceInterfaceClasses = ClassUtils.getAllInterfacesForClass(serviceClass);
            List<Class<?>> filteredServiceInterfaceList = filterServiceInterfaceClasses(element, serviceInterfaceClasses);
            //
            for (int i = 0; i < filteredServiceInterfaceList.size(); i++) {
                Class<?> serviceInterface = filteredServiceInterfaceList.get(i);
                //
                BeanDefinition exporterBeanDef = new GenericBeanDefinition();
                exporterBeanDef.setParentName(parentExporter);
                exporterBeanDef.getPropertyValues().addPropertyValue("service", serviceBeanReference);
                exporterBeanDef.getPropertyValues().addPropertyValue("serviceInterface", serviceInterface);
                //
                String remoteServiceName = getRemotingServiceName(exporterClass, serviceName);
                //
                if(ClassUtils.isAssignable(RmiServiceExporter.class, exporterClass)) {
                	exporterBeanDef.getPropertyValues().add("serviceName", StringUtils.capitalize(serviceName));
                }
                //
                parserContext.getRegistry().registerBeanDefinition(remoteServiceName, exporterBeanDef);
                //
                parserContext.registerComponent(new BeanComponentDefinition(exporterBeanDef, remoteServiceName));
            }
        } catch (Exception e) {
            log.error("Register Service Exporter Bean Error!", e);
        }
    }
    
    private String getRemotingServiceName(Class<?> exporterClass, String serviceName) {
    	String remoteServiceName = null;
    	if (ClassUtils.isAssignable(RmiServiceExporter.class, exporterClass)) {
            remoteServiceName = serviceName + "Exporter";
        } else {
            remoteServiceName = "/" + StringUtils.capitalize(serviceName);
        }
    	//
    	return remoteServiceName;
	}

	private List<Class<?>> filterServiceInterfaceClasses(Element element, Class<?>[] serviceInterfaceClasses) {
        List<Class<?>> filteredServiceInterfaceList = new ArrayList<Class<?>>();
        //
        for (int i = 0; i < serviceInterfaceClasses.length; i++) {
            Class<?> serviceInterfaceClass = serviceInterfaceClasses[i];
            //
            String[] packagePatterns = getPackagePatterns(element);
            for (int j = 0; j < packagePatterns.length; j++) {
                String packagePattern = ClassUtils.convertClassNameToResourcePath(packagePatterns[j]) + "/**";
                String targetPath = ClassUtils.convertClassNameToResourcePath(serviceInterfaceClass.getName());
                if (pathMatcher.matchStart(packagePattern, targetPath)) {
                    filteredServiceInterfaceList.add(serviceInterfaceClass);
                }
            }
        }
        return filteredServiceInterfaceList;
    }
    
    private String[] getPackagePatterns(Element element) {
        return StringUtils.commaDelimitedListToStringArray(element.getAttribute("base-package"));
    }
    
}
