package org.osforce.spring4me.web.flow.config;

import org.osforce.spring4me.web.flow.engine.FlowContext;

/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-5-31 - 上午9:28:41
 */
public abstract class AbstractFlowContext implements FlowContext {
	
	private String flowId;
	private String from;
	private String to;
	//
	private String sessionKey;
	
	public AbstractFlowContext(String sessionKey, String flowId) {
		this.sessionKey = sessionKey;
		this.flowId = flowId;
	}
	
	public String getSessionKey() {
		return sessionKey;
	}

	public String getFrom() {
		return from;
	}

	public FlowContext setFrom(String from) {
		this.from = from;
		return this;
	}

	public String getTo() {
		return to;
	}

	public FlowContext setTo(String to) {
		this.to = to;
		return this;
	}
	
	public String getFlowId() {
		return flowId;
	}

	public FlowContext setFlowId(String flowId) {
		this.flowId = flowId;
		return this;
	}

}
