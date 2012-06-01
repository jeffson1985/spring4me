package org.osforce.spring4me.web.flow.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;

/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-5-29 - 下午1:22:10
 */
public abstract class AbstractFlowConfigFactory implements FlowConfigFactory {

	private boolean cacheable = true;
	//
	private Map<String, FlowConfig> cache = new HashMap<String, FlowConfig>();
	
	public boolean isCacheable() {
		return cacheable;
	}
	
	@Value("${application.cacheable}")
	public void setCacheable(boolean cacheable) {
		this.cacheable = cacheable;
	}
	
	public FlowConfig findFlow(String name) {
		if(isCacheable()) {
			return getCachedFlow(name);
		}
		//
		return loadFlow(name);
	}
	
	protected FlowConfig getCachedFlow(String name) {
		FlowConfig flowConfig = cache.get(name);
		if(flowConfig==null) {
			synchronized (this) {
				if(flowConfig==null) {
					flowConfig = loadFlow(name); 
				}
				//
				if(flowConfig!=null) {
					cache.put(name, flowConfig);
				}
			}
		}
		//
		return flowConfig;
	}

	protected abstract FlowConfig loadFlow(String name);
	
}
