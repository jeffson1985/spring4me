package org.osforce.spring4me.web.flow.config;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-5-29 - 下午2:03:13
 */
public class FlowConfig {
	
	public static final String KEY = FlowConfig.class.getName();

	private String name;
	//
	private Map<String, StepConfig> stepConfigMap = new LinkedHashMap<String, StepConfig>();
	
	public FlowConfig(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void addStepConfig(StepConfig stepConfig) {
		this.stepConfigMap.put(stepConfig.getPage(), stepConfig);
	}

	public List<StepConfig> getAllStepConfig() {
		return new ArrayList<StepConfig>(stepConfigMap.values());
	}

	public StepConfig getStepConfig(String page) {
		return getStepConfig(page, 0);
	}
	
	public StepConfig getPreStepConfig(String page) {
		return getStepConfig(page, -1);
	}
	
	public StepConfig getNextStepConfig(String page) {
		return getStepConfig(page, 1);
	}
	
	public StepConfig getStepConfig(String page, int offset) {
		StepConfig target = stepConfigMap.get(page);
		if(offset!=0) {
			//
			String[] steps = stepConfigMap.keySet().toArray(new String[stepConfigMap.size()]);
			for(int i=0, len=steps.length; i<len; i++) {
				if(page.equals(steps[i])) {
					target = stepConfigMap.get(steps[i+offset]);
				}
			}
		}
		//
		return target;
	}

}
