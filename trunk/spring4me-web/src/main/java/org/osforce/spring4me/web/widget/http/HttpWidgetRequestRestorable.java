package org.osforce.spring4me.web.widget.http;

import java.util.Map;

/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-5-30 - 上午9:07:07
 */
public interface HttpWidgetRequestRestorable extends HttpWidgetRequest {

	void restoreWidgetRequest(Map<String, String[]> parameters, Map<String, ?> attributes);
	
}
