package org.osforce.spring4me.web.flow.config;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-5-29 - 下午1:54:12
 */
public class StepConfig {
	
	public enum Type{start, step, finish}
	
	private Type type;
	private String page;
	//
	private Map<String, FlowConfig> subflowConfigMap = new HashMap<String, FlowConfig>();
	
	public StepConfig(Type type, String page) {
		this.type = type;
		this.page = page;
	}

	public Type getType() {
		return type;
	}

	public String getPage() {
		return page;
	}
	
	public boolean hasSubFlow() {
		return subflowConfigMap.isEmpty();
	}

	public FlowConfig getSubflow(String name) {
		return subflowConfigMap.get(name);
	}
	
	public void setType(Type type) {
		this.type = type;
	}
	
	public void setPage(String page) {
		this.page = page;
	}
	
	public void addSubFlowConfig(FlowConfig subFlowConfig) {
		this.subflowConfigMap.put(subFlowConfig.getName(), subFlowConfig);
	}

}
