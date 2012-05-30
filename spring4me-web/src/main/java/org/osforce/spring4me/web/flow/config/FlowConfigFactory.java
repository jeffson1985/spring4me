package org.osforce.spring4me.web.flow.config;

/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-5-29 - 下午1:20:14
 */
public interface FlowConfigFactory {

	FlowConfig findFlow(String name);
	
	FlowConfig findFlow(String step1, String step2);
	
}
