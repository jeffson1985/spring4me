package org.sarons.spring4me.web.servlet.support;

import javax.servlet.http.HttpServletRequest;

import org.sarons.spring4me.web.widget.config.WidgetConfig;
import org.sarons.spring4me.web.widget.utils.WidgetConfigUtils;
import org.springframework.web.servlet.FlashMap;

/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-7-30 - 下午9:50:19
 */
public class SessionFlashMapManager extends
		org.springframework.web.servlet.support.SessionFlashMapManager {
	
	@Override
	protected boolean isFlashMapForRequest(FlashMap flashMap,
			HttpServletRequest request) {
		//
		WidgetConfig widgetConfig = WidgetConfigUtils.getWidgetConfig(request);
		if(widgetConfig!=null) {
			return flashMap.getTargetRequestPath().contains(widgetConfig.getName());
		}
		//
		return super.isFlashMapForRequest(flashMap, request);
	}

}
