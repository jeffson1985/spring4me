package org.osforce.spring4me.web.widget.http.impl;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.osforce.spring4me.web.widget.http.HttpWidgetRequest;
import org.osforce.spring4me.web.widget.http.HttpWidgetRequestRestorable;
import org.springframework.util.StringUtils;

/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-5-30 - 上午9:09:58
 */
public class DefaultHttpWidgetRequestRestorable extends DefaultHttpWidgetRequest
	implements HttpWidgetRequestRestorable {
	
	private Map<String, String[]> widgetParameters = new HashMap<String, String[]>();
	//
	private Map<String, Object> widgetAttributes = new HashMap<String, Object>();
	
	public DefaultHttpWidgetRequestRestorable(HttpWidgetRequest widgetRequest) {
		super(widgetRequest);
	}

	@Override
	public String getParameter(String name) {
		String paramValue = super.getParameter(name);
		if(!StringUtils.hasText(paramValue)) {
			String[] paramValues = this.widgetParameters.get(name);
			//
			if(paramValues!=null && paramValues.length==1) {
				paramValue = paramValues[0];
			}
		}
		//
		return paramValue;
	}
	
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Enumeration getParameterNames() {
		Vector<String> parameterNames = new Vector<String>();
		for(Enumeration<String> e=super.getParameterNames(); e.hasMoreElements();) {
			parameterNames.add(e.nextElement());
		}
		//
		return parameterNames.elements();
	}
	
	
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map getParameterMap() {
		Map parameterMap = new HashMap(getParameterMap());
		parameterMap.putAll(this.widgetParameters);
		return parameterMap;
	}
	
	@Override
	public String[] getParameterValues(String name) {
		String[] paramValues = super.getParameterValues(name);
		if(paramValues==null) {
			paramValues = this.widgetParameters.get(name);
		}
		//
		return paramValues;
	}
	
	@Override
	public Object getAttribute(String name) {
		Object attrValue = super.getAttribute(name);
		if(attrValue==null) {
			attrValue = this.widgetAttributes.get(name);
		}
		//
		return attrValue;
	}
	
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Enumeration getAttributeNames() {
		Vector<String> attributeNames = new Vector<String>();
		for(Enumeration<String> e=super.getAttributeNames(); e.hasMoreElements();) {
			attributeNames.add(e.nextElement());
		}
		//
		return attributeNames.elements();
	}

	@SuppressWarnings("unchecked")
	public Map<String, ?> getWidgetAttributes() {
		for(Enumeration<String> e=getAttributeNames(); e.hasMoreElements();) {
			String attrName = e.nextElement();
			Object attrValue = getAttribute(attrName);
			//
			if(!widgetAttributes.containsKey(attrName)) {
				widgetAttributes.put(attrName, attrValue);
			}
		}
		return widgetAttributes;
	}

	public void restoreWidgetRequest(Map<String, String[]> parameters,
			Map<String, ?> attributes) {
		this.widgetParameters.putAll(parameters);
		this.widgetAttributes.putAll(attributes);
	}
	
}
