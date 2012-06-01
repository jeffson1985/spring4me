package org.osforce.spring4me.web.flow.config;

/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-5-29 - 下午1:20:14
 */
public interface FlowConfigFactory {

	/**
	 * find flow by flow name
	 * @param name
	 * @return
	 */
	FlowConfig findFlow(String name);
	
	/**
	 * find flow by from and to
	 * @param from
	 * @param to
	 * @return
	 */
	FlowConfig findFlow(String from, String to);
	
	/**
	 * 
	 * @param name parent flow name
	 * @param from
	 * @param to
	 * @return a subflow
	 */
	FlowConfig findSubFlow(String name, String from, String to);
	
}
