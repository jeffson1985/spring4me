package org.osforce.spring4me.web.widget.core;

import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * 
 * @author <a href="mailto:haozhonghu@hotmail.com">gavin</a>
 * @since 0.3.0
 * @create Jul 12, 2011 - 9:24:17 PM
 * <a href="http://www.opensourceforce.org">开源力量</a>
 */
public class HttpWidgetRequest extends HttpServletRequestWrapper {

	private static final String PREFIX = "widget.";
	
	public HttpWidgetRequest(HttpServletRequest request) {
		super(request);
	}
	
	@Override
	public Object getAttribute(String name) {
		return super.getAttribute(PREFIX + name);
	}
	
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Enumeration getAttributeNames() {
		Vector<String> v = new Vector<String>();
		Enumeration<String> e = super.getAttributeNames();
		for(String name = e.nextElement(); e.hasMoreElements();) {
			v.add(PREFIX + name);
		}
		return v.elements();
	}
	
	@Override
	public void setAttribute(String name, Object o) {
		super.setAttribute(PREFIX + name, o);
	}
	
}
