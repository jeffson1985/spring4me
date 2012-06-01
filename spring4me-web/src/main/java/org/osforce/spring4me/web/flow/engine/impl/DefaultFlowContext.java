package org.osforce.spring4me.web.flow.engine.impl;

import org.osforce.spring4me.web.flow.config.AbstractFlowContext;


/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-5-31 - 上午9:54:06
 */
public class DefaultFlowContext extends AbstractFlowContext {
	
	public DefaultFlowContext(String sessionKey) {
		super(sessionKey, null);
	}
	
	public DefaultFlowContext(String sessionKey, String flowId) {
		super(sessionKey, flowId);
	}

	public <T> T getContextObject(Class<T> clazz) {
		throw new NullPointerException();
	}

}
