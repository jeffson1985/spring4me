package org.sarons.spring4me.support.remoting.config;

import java.util.List;

import org.sarons.spring4me.support.remoting.metadata.RemoteServiceMetadata;
import org.sarons.spring4me.support.remoting.metadata.RemoteServiceMetadataManager;
import org.sarons.spring4me.support.remoting.restful.RestfulServiceImporter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.CannotLoadBeanClassException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;


/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-7-30 - 下午10:00:25
 */
public class RemoteServiceImporter implements BeanDefinitionRegistryPostProcessor {
	
	private String importerProvider;
	
	private RemoteServiceMetadataManager metadataManager;
	
	public void setImporterProvider(String importerProvider) {
		this.importerProvider = importerProvider;
	}
	
	public void setMetadataManager(RemoteServiceMetadataManager metadataManager) {
		this.metadataManager = metadataManager;
	}
	
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
	}

	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		Assert.notNull(importerProvider);
		//
		List<RemoteServiceMetadata> serviceMetadataList = metadataManager.loadRemote();
		for(RemoteServiceMetadata metadata : serviceMetadataList) {
			registerImporterBeanDefinition(registry, metadata);
		}
	}
	
	@SuppressWarnings("deprecation")
	private void registerImporterBeanDefinition(BeanDefinitionRegistry registry, RemoteServiceMetadata serviceMetadata) throws BeansException {
		BeanDefinition importerBeanDefinition = registry.getBeanDefinition(importerProvider);
		try {	
			//
			Object serviceUrlValue = importerBeanDefinition
					.getPropertyValues().getPropertyValue("serviceUrl").getValue();
			String serviceUrl = null;
			if(serviceUrlValue instanceof String) {
				serviceUrl = (String) serviceUrlValue;
			}
			if(serviceUrlValue instanceof TypedStringValue) {
				serviceUrl = ((TypedStringValue)serviceUrlValue).getValue();
			}
			//
			Class<?> importerClass = ClassUtils.forName(importerBeanDefinition.getBeanClassName());
			if(ClassUtils.isAssignable(RestfulServiceImporter.class, importerClass)) {
				serviceUrl = serviceUrl + serviceMetadata.getServiceName();
			}
			//
			importerBeanDefinition = new GenericBeanDefinition();
			importerBeanDefinition.setParentName(importerProvider);
			importerBeanDefinition.getPropertyValues().addPropertyValue("serviceUrl", serviceUrl);
			importerBeanDefinition.getPropertyValues().addPropertyValue("serviceInterface", serviceMetadata.getServiceInterface());
			// 
			registry.registerBeanDefinition(serviceMetadata.getName(), importerBeanDefinition);
			//
		} catch (ClassNotFoundException e) {
			throw new CannotLoadBeanClassException("", serviceMetadata.getName(), importerBeanDefinition.getBeanClassName(), e);
		} catch (LinkageError e) {
			throw new CannotLoadBeanClassException("", serviceMetadata.getName(), importerBeanDefinition.getBeanClassName(), e);
		}
	}

}
