package org.osforce.spring4me.web.widget;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author <a href="mailto:haozhonghu@hotmail.com">gavin</a>
 * @since 0.1.0
 * @create May 13, 2011 - 4:38:27 PM
 * <a href="http://www.opensourceforce.org">开源力量</a>
 */
public class WidgetConfig {

	public static final String KEY = WidgetConfig.class.getName();

	private String id;
	private String path;
	private String cssClass;
	private String title;
	private Map<String, String> prefs = new HashMap<String, String>();

	public WidgetConfig(String id, String path, String cssClass) {
		this.id = id;
		this.path = path;
		this.cssClass = cssClass;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
