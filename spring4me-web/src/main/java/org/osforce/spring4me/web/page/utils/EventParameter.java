package org.osforce.spring4me.web.page.utils;

/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-5-31 - 下午7:18:21
 */
public class EventParameter {
	
	private String event;
	private String pagePath;
	private String widgetPath;
	
	public EventParameter(String eventParameter) {
		String[] eventParams = eventParameter.split("\\|");
		this.pagePath = eventParams[0];
		this.widgetPath = eventParams[1];
		this.event = eventParams[2];
	}
	public String getEvent() {
		return event;
	}
	
	public void setEvent(String event) {
		this.event = event;
	}
	
	public String getPagePath() {
		return pagePath;
	}
	
	public void setPagePath(String pagePath) {
		this.pagePath = pagePath;
	}
	
	public String getWidgetPath() {
		return widgetPath;
	}
	
	public void setWidgetPath(String widgetPath) {
		this.widgetPath = widgetPath;
	}
}
