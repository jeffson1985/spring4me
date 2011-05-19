package org.osforce.spring4me.web.widget;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author <a href="mailto:haozhonghu@hotmail.com">gavin</a>
 * @since 0.1.0
 * @create May 13, 2011 - 4:38:27 PM
 * <a href="http://www.opensourceforce.org">开源力量</a>
 */
public class WidgetConfig {
	public static final String KEY = WidgetConfig.class.getName();
	public static final String NAME = StringUtils.uncapitalize(WidgetConfig.class.getSimpleName());

	private String id;
	private String name;
	private String path;
	private String cssClass;
	private String title;
	private Map<String, String> prefs = new HashMap<String, String>();

	public WidgetConfig(String name, String path, String cssClass) {
		this.name = name;
		this.path = path;
		this.cssClass = cssClass;
	}

	public String getId() {
		if(id==null) {
			id = UUID.randomUUID().toString();
		}
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getCssClass() {
		return cssClass;
	}

	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Map<String, String> getPrefs() {
		return prefs;
	}

	public void addPref(String key, String value) {
		this.prefs.put(key, value);
	}

	public void setPrefs(Map<String, String> prefs) {
		this.prefs = prefs;
	}

}
