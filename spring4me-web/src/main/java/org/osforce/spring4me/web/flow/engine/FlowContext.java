package org.osforce.spring4me.web.flow.engine;

/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-5-31 - 上午9:12:54
 */
public interface FlowContext {
	
	FlowContext setFrom(String from);
	
	FlowContext setTo(String to);
	
	<T> T getContextObject(Class<T> clazz);
	
}
