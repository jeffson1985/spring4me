package org.osforce.spring4me.web.event;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-5-30 - 下午9:37:25
 */
public abstract class EventReceiver implements EventConstants  {

	public static String receive(HttpServletRequest request, boolean remove) {
		String event = (String) request.getSession().getAttribute(EVENT);
		if(remove) {
			request.getSession().removeAttribute(EVENT);
		}
		return event;
	}
	
}
