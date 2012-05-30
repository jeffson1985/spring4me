package org.osforce.spring4me.web.flow.utils;

import javax.servlet.ServletRequest;

import org.osforce.spring4me.web.Keys;
import org.osforce.spring4me.web.flow.config.FlowConfig;

/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-5-30 - 上午10:04:07
 */
public abstract class FlowConfigUtils {

	public static FlowConfig getFlowConfig(ServletRequest request) {
		return (FlowConfig) request.getAttribute(Keys.REQUEST_KEY_FLOW_CONFIG);
	}
	
	public static void setFlowConfig(ServletRequest request, FlowConfig flowConfig) {
		request.setAttribute(Keys.REQUEST_KEY_FLOW_CONFIG, flowConfig);
	}
	
}
