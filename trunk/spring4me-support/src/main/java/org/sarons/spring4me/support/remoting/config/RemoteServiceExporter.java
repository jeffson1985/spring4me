package org.sarons.spring4me.support.remoting.config;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sarons.spring4me.support.chain.Handler;
import org.sarons.spring4me.support.chain.HandlerChain;
import org.sarons.spring4me.support.chain.HandlerContext;
import org.sarons.spring4me.support.chain.impl.DefaultHandlerChain;
import org.sarons.spring4me.support.remoting.metadata.DefaultRemoteServiceMetadataManager;
import org.sarons.spring4me.support.remoting.metadata.RemoteServiceMetadata;
import org.sarons.spring4me.support.remoting.metadata.RemoteServiceMetadataManager;
import org.sarons.spring4me.support.remoting.metadata.RemoteServiceMethodMetadata;
import org.sarons.spring4me.support.remoting.metadata.RemoteServiceNameGenerator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.CannotLoadBeanClassException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.remoting.rmi.RmiServiceExporter;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;


/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-7-30 - 下午10:00:20
 */
public class RemoteServiceExporter implements BeanDefinitionRegistryPostProcessor,
	BeanFactoryAware {
	
	private static final Log log = LogFactory.getLog(RemoteServiceExporter.class);

	private BeanFactory beanFactory;
	
	private String exporterProvider;
	
	private Set<String> serviceInterfacePackages;

	private RemoteServiceNameGenerator serviceNameGenerator;
	
	private RemoteServiceMetadataManager serviceMetadataManager = new DefaultRemoteServiceMetadataManager();
	
	private List<RemoteServiceMetadata> serviceMetadataList = new ArrayList<RemoteServiceMetadata>();
	
	private HandlerChain<Map<String, BeanDefinition>> handlerChain = new DefaultHandlerChain<Map<String, BeanDefinition>>();
	
	public void setExporterProvider(String exporterProvider) {
		this.exporterProvider = exporterProvider;
	}
	
	public void setServiceInterfacePackages(Set<String> serviceInterfacePackages) {
		this.serviceInterfacePackages = serviceInterfacePackages;
	}
	
	public void setServiceNameGenerator(RemoteServiceNameGenerator serviceNameGenerator) {
		this.serviceNameGenerator = serviceNameGenerator;
	}
	
	public void setServiceMetadataManager(RemoteServiceMetadataManager serviceMetadataManager) {
		this.serviceMetadataManager = serviceMetadataManager;
	}
	
	public void setHandlers(Handler<Map<String, BeanDefinition>>[] handlers) {
		for(Handler<Map<String, BeanDefinition>> handler : handlers) {
			handlerChain.registerHandler(handler);
		}
	}
	
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}
	
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		//
		serviceMetadataManager.storeLocal(serviceMetadataList);
	}
	
	@SuppressWarnings("deprecation")
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		Map<String, BeanDefinition> beanDefinitionMap = filterBeanDefinitions(beanFactory, registry);
		for(String beanName : beanDefinitionMap.keySet()) {
			BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
			try {
				Class<?> beanType = ClassUtils.forName(beanDefinition.getBeanClassName());
				//
				registerExporterBeanDefinition(registry, beanName, beanType);
			} catch (ClassNotFoundException e) {
				throw new CannotLoadBeanClassException("", beanName, beanDefinition.getBeanClassName(), e);
			} catch (LinkageError e) {
				throw new CannotLoadBeanClassException("", beanName, beanDefinition.getBeanClassName(), e);
			}
		}
	}
	
	/**
	 * 使用责任链模式筛选需要支持远程访问的 Bean
	 * @param registry
	 * @return
	 */
	protected Map<String, BeanDefinition> filterBeanDefinitions(BeanFactory beanFactory, BeanDefinitionRegistry registry) {
		HandlerContext context = new HandlerContext();
		context.set(BeanDefinitionRegistry.class.getName(), registry);
		context.set(BeanFactory.class.getName(), beanFactory);
		//
		Map<String, BeanDefinition> beanDefinitionMap = new LinkedHashMap<String, BeanDefinition>();
		handlerChain.execute(context, beanDefinitionMap);
		//
		return beanDefinitionMap;
	}
	
	/**
	 * 注册远程服务 Bean 
	 * @param registry
	 * @param beanName
	 * @param beanType
	 * @throws BeansException
	 * @see org.springframework.remoting.rmi.RmiServiceExporter
	 * @see org.springframework.remoting.caucho.HessianServiceExporter
	 */
	@SuppressWarnings("deprecation")
	private void registerExporterBeanDefinition(BeanDefinitionRegistry registry, String beanName, Class<?> beanType) throws BeansException {
		Assert.notNull(exporterProvider);
		Assert.notNull(serviceInterfacePackages);
		//
		RuntimeBeanReference serviceBeanReference = new RuntimeBeanReference(beanName);
		BeanDefinition exporterBeanDefinition = registry.getBeanDefinition(exporterProvider);
		try {
			//
			Class<?> exporterClass = ClassUtils.forName(exporterBeanDefinition.getBeanClassName());
			//
			Class<?>[] interfaceClasses = beanType.getInterfaces();
			for(Class<?> interfaceClass : interfaceClasses) {
				if(isServiceInterface(interfaceClass)) {
					exporterBeanDefinition = new GenericBeanDefinition();
					exporterBeanDefinition.setParentName(exporterProvider);
					exporterBeanDefinition.getPropertyValues().addPropertyValue("service", serviceBeanReference);
					exporterBeanDefinition.getPropertyValues().addPropertyValue("serviceInterface", interfaceClass);
			        //
			        String serviceName = getServiceName(exporterClass, beanType, beanName);
			        //
			        if(ClassUtils.isAssignable(RmiServiceExporter.class, exporterClass)) {
						exporterBeanDefinition.getPropertyValues().add("serviceName", StringUtils.capitalize(serviceName));
	                }
			        //
			        registerRemoteServiceMetadata(beanName, serviceName,  beanType, interfaceClass);
			        // 注册bean
			        registry.registerBeanDefinition(serviceName, exporterBeanDefinition);
			        //
			        if(log.isInfoEnabled()) {
			        	log.debug("Register remote service " + serviceName);
			        }
				}
			}
			//
		} catch (ClassNotFoundException e) {
			throw new CannotLoadBeanClassException("", beanName, exporterBeanDefinition.getBeanClassName(), e);
		} catch (LinkageError e) {
			throw new CannotLoadBeanClassException("", beanName, exporterBeanDefinition.getBeanClassName(), e);
		}
	}
	
	private boolean isServiceInterface(Class<?> serviceInterface) {
		String className = serviceInterface.getPackage().getName();
		for(String serviceInterfacePackage : serviceInterfacePackages) {
			if(className.startsWith(serviceInterfacePackage)) {
				return true;
			}
		}
		//
		return false;
	}

	private String getServiceName(Class<?> exporterClass, Class<?> serviceClass, String beanName) {
		String serviceName = null;
    	if (ClassUtils.isAssignable(RmiServiceExporter.class, exporterClass)) {
            serviceName = beanName + "Exporter";
        } else {
            serviceName = serviceNameGenerator.generate(serviceClass);
        }
    	//
    	return serviceName;
	}
	
	private void registerRemoteServiceMetadata(String beanName, String serviceName, 
			Class<?> beanType, Class<?> serviceInterface) {
		RemoteServiceMetadata metadata = new RemoteServiceMetadata();
		metadata.setName(beanName);
		metadata.setServiceName(serviceName);
		metadata.setServiceInterface(serviceInterface);
		//
		Method[] methods = beanType.getDeclaredMethods();
		for(Method method : methods) {
			RemoteServiceMethodMetadata methodMetadata = new RemoteServiceMethodMetadata(method);
			metadata.addServiceMethodMetadata(methodMetadata);
		}
		//
		this.serviceMetadataList.add(metadata);
	}
	
}
