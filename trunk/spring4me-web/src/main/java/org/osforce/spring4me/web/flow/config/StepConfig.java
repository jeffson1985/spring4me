package org.osforce.spring4me.web.flow.config;

import java.util.LinkedHashSet;
import java.util.Set;

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
	private Set<String> subFlowNames = new LinkedHashSet<String>();
	
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
	
	public void setType(Type type) {
		this.type = type;
	}
	
	public void setPage(String page) {
		this.page = page;
	}
	
	public boolean hasSubFlow() {
		return !subFlowNames.isEmpty();
	}
	
	public Set<String> getSubFlowNames() {
		return subFlowNames;
	}
	
	public void addSubFlow(String name) {
		this.subFlowNames.add(name);
	}
	
}
