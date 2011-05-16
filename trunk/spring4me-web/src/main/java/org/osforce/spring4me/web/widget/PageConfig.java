package org.osforce.spring4me.web.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author <a href="mailto:haozhonghu@hotmail.com">gavin</a>
 * @since 0.1.0
 * @create May 13, 2011 - 4:38:19 PM
 * <a href="http://www.opensourceforce.org">开源力量</a>
 */
public class PageConfig {

	public static final String KEY = PageConfig.class.getName();

	private String parent;
	private Map<String, List<WidgetConfig>> placeholders = new HashMap<String, List<WidgetConfig>>();

	public PageConfig(String parent) {
		this.parent = parent;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public void add(String placeholder, WidgetConfig widgetConfig) {
		List<WidgetConfig> widgetConfigList = placeholders.get(placeholder);
		if(widgetConfigList==null) {
			widgetConfigList = new ArrayList<WidgetConfig>();
			placeholders.put(placeholder, widgetConfigList);
		}
		widgetConfigList.add(widgetConfig);
	}

	public List<WidgetConfig> getWidgetConfigs(String placeholder) {
		return placeholders.get(placeholder);
	}

}
