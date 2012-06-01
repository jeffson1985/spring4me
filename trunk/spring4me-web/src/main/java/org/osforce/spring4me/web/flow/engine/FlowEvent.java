package org.osforce.spring4me.web.flow.engine;

import java.util.EventObject;

/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-5-31 - 上午8:55:37
 */
public class FlowEvent extends EventObject {
	private static final long serialVersionUID = 5256796092393379963L;
	
	public static final String KEY = FlowEvent.class.getName();
	
	public enum Type {FLOW_STARTING, FLOW_FINISHING, SUBFLOW_STARTING, SUBFLOW_FINISHING, FLOW_FORWARD, FLOW_BACK};
	
	private Type type;
	
	public FlowEvent(FlowContext context, Type type) {
		super(context);
		this.type = type;
	}

	public Type getType() {
		return type;
	}
	
}
