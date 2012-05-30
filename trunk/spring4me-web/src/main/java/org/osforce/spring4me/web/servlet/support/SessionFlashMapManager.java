package org.osforce.spring4me.web.servlet.support;

import javax.servlet.http.HttpServletRequest;

import org.osforce.spring4me.web.widget.config.WidgetConfig;
import org.osforce.spring4me.web.widget.http.HttpWidgetRequest;
import org.osforce.spring4me.web.widget.utils.WidgetConfigUtils;
import org.springframework.web.servlet.FlashMap;

/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-5-30 - 下午5:55:01
 */
public class SessionFlashMapManager extends
		org.springframework.web.servlet.support.SessionFlashMapManager {
	
	@Override
	protected boolean isFlashMapForRequest(FlashMap flashMap, HttpServletRequest request) {
		if(request instanceof HttpWidgetRequest) {
			WidgetConfig widgetConfig = WidgetConfigUtils.getWidgetConfig(request);
			return flashMap.getTargetRequestPath().contains(widgetConfig.getName());
		}
		return super.isFlashMapForRequest(flashMap, request);
	}

}
