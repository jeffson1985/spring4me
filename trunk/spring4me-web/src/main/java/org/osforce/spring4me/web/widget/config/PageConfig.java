package org.osforce.spring4me.web.widget.config;

import java.util.List;
import java.util.Map;

import org.osforce.spring4me.commons.collection.CollectionUtil;

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
	private String qualifier;
	private Map<String, List<WidgetConfig>> placeholders = CollectionUtil.newHashMap();

	public PageConfig(String parent, String qualifier) {
		this.parent = parent;
		this.qualifier = qualifier;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}
	
	public String getQualifier() {
		return qualifier;
	}
	
	public void setQualifier(String qualifier) {
		this.qualifier = qualifier;
	}

	public void add(String placeholder, WidgetConfig widgetConfig) {
		List<WidgetConfig> widgetConfigList = placeholders.get(placeholder);
		if(widgetConfigList==null) {
			widgetConfigList = CollectionUtil.newArrayList();
			placeholders.put(placeholder, widgetConfigList);
		}
		widgetConfigList.add(widgetConfig);
	}

	public List<WidgetConfig> getWidgetConfigs(String placeholder) {
		return placeholders.get(placeholder);
	}
	
	public void addPlaceholder(String placeholder, List<WidgetConfig> widgetConfigs) {
		placeholders.put(placeholder, widgetConfigs);
	}
	
	public Map<String, List<WidgetConfig>> getPlaceholders() {
		return placeholders;
	}

}
