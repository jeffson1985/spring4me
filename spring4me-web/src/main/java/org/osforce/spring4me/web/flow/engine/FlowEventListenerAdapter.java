package org.osforce.spring4me.web.flow.engine;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osforce.spring4me.web.flow.engine.FlowEvent.Type;


/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-5-31 - 上午11:02:48
 */
public class FlowEventListenerAdapter implements FlowEventListener {

	protected static final Log log = LogFactory.getLog(FlowEventListenerAdapter.class);
	
	/**
	 * 
	 */
	public void trigger(FlowEvent event) {
		if(log.isDebugEnabled()) {
			log.debug(String.format("Flow Event %s triggered!", event.getType()));
		}
	}

	/**
	 * 
	 */
	public boolean accept(Type eventType) {
		if(log.isDebugEnabled()) {
			log.debug("Default accept any event type!");
		}
		return true;
	}

}
