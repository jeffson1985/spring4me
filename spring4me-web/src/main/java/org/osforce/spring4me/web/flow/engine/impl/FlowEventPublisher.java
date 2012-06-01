package org.osforce.spring4me.web.flow.engine.impl;

import javax.servlet.http.HttpServletRequest;

import org.osforce.spring4me.web.Keys;
import org.osforce.spring4me.web.flow.engine.FlowContext;
import org.osforce.spring4me.web.flow.engine.FlowEvent;
import org.osforce.spring4me.web.flow.engine.FlowEvent.Type;
import org.osforce.spring4me.web.flow.engine.FlowEventListenerAdapter;

/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-5-31 - 下午2:06:33
 */
public class FlowEventPublisher extends FlowEventListenerAdapter {

	@Override
	public boolean accept(Type eventType) {
		return super.accept(eventType);
	}
	
	@Override
	public void trigger(FlowEvent event) {
		FlowContext flowContext = (FlowContext) event.getSource();
		//
		HttpServletRequest httpRequest = flowContext.getContextObject(HttpServletRequest.class);
		if(httpRequest!=null) {
			httpRequest.setAttribute(Keys.REQUEST_KEY_FLOW_EVENT, event);
		}
	}
	
}
