package org.sarons.spring4me.support.remoting.metadata;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-7-30 - 下午10:00:35
 */
public class RemoteServiceMetadata {
	
	private String name;
	
	private String serviceName;
	
	private Class<?> serviceInterface;
	
	private Map<String, RemoteServiceMethodMetadata> serviceMethodsMetadata = new HashMap<String, RemoteServiceMethodMetadata>();
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public Class<?> getServiceInterface() {
		return serviceInterface;
	}

	public void setServiceInterface(Class<?> serviceInterface) {
		this.serviceInterface = serviceInterface;
	}
	
	public RemoteServiceMethodMetadata getServiceMethodMetadata(String methodName) {
		return serviceMethodsMetadata.get(methodName);
	}
	
	public void addServiceMethodMetadata(RemoteServiceMethodMetadata methodMetadata) {
		if(!this.serviceMethodsMetadata.containsKey(methodMetadata.getMethod())) {
			this.serviceMethodsMetadata.put(methodMetadata.getMethod(), methodMetadata);
		}
	}
	
	public Map<String, RemoteServiceMethodMetadata> getServiceMethodsMetadata() {
		return serviceMethodsMetadata;
	}
	
	public void setServiceMethodsMetadata(Map<String, RemoteServiceMethodMetadata> serviceMethodsMetadata) {
		this.serviceMethodsMetadata = serviceMethodsMetadata;
	}
	
}
