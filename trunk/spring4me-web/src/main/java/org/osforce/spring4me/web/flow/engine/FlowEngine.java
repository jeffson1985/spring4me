package org.osforce.spring4me.web.flow.engine;


/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-5-31 - 上午8:48:59
 */
public interface FlowEngine {
	
	/**
	 * 
	 * @param from
	 * @param to
	 * @return a flow id or sub a flow id
	 */
	String execute(FlowContext context);
	
}
