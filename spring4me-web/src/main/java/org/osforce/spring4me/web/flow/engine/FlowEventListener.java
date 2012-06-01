package org.osforce.spring4me.web.flow.engine;

/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-5-31 - 上午8:51:12
 */
public interface FlowEventListener {

	void trigger(FlowEvent event);
	
	boolean accept(FlowEvent.Type eventType);
	
}
