package org.osforce.spring4me.web.flow.engine.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osforce.spring4me.web.flow.config.AbstractFlowContext;

/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-5-31 - 上午9:23:45
 */
public class WebFlowContext extends AbstractFlowContext {
	
	private Map<Class<?>, Object> contextObjects = new HashMap<Class<?>, Object>();
	
	public WebFlowContext(HttpServletRequest request, HttpServletResponse response) {
		super(request.getSession().getId(), request.getParameter("flowId"));
		//
		this.contextObjects.put(HttpServletRequest.class, request);
		this.contextObjects.put(HttpServletResponse.class, response);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getContextObject(Class<T> clazz) {
		return (T) contextObjects.get(clazz);
	}

}
