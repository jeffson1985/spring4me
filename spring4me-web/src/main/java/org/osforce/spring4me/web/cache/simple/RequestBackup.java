package org.osforce.spring4me.web.cache.simple;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.osforce.spring4me.web.widget.http.HttpWidgetRequest;

/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-5-31 - 下午4:32:02
 */
public class RequestBackup {

	private Map<String, Object> attributeBackup = new HashMap<String, Object>();
	private Map<String, String[]> parameterBackup = new HashMap<String, String[]>();

	public RequestBackup(HttpServletRequest httpRequest) {
		backup(httpRequest);
	}

	public void mergeRequest(HttpServletRequest httpRequest) {
		backup(httpRequest);
	}

	public Map<String, Object> getAttributeBackup() {
		return attributeBackup;
	}

	public Map<String, String[]> getParameterBackup() {
		return parameterBackup;
	}

	@SuppressWarnings("unchecked")
	private void backup(HttpServletRequest httpRequest) {
		// backup parameters
		parameterBackup.putAll(httpRequest.getParameterMap());
		// backup attributes
		for (Enumeration<String> e = httpRequest.getAttributeNames(); e.hasMoreElements();) {
			String name = e.nextElement();
			Object value = httpRequest.getAttribute(name);
			attributeBackup.put(name, value);
		}
		// backup widget model
		if(httpRequest instanceof HttpWidgetRequest) {
			HttpWidgetRequest widgetRequest = (HttpWidgetRequest) httpRequest;
			if (widgetRequest.getWidgetModel() != null) {
				attributeBackup.putAll(widgetRequest.getWidgetModel());
			}
		}
	}

}
