package org.osforce.spring4me.web.widget;

import java.io.File;
import java.util.List;

import org.w3c.dom.Element;

/**
 * 
 * @author <a href="mailto:haozhonghu@hotmail.com">gavin</a>
 * @since 0.1
 * @create May 13, 2011 - 4:38:12 PM
 * <a href="http://www.opensourceforce.org">开源力量</a>
 */
public final class ConfigParser {

	public ConfigParser() {
	}

	public static PageConfig parse(File xml) {
		Element pageEle = XMLUtil.parseToElement(xml);
		String parentP = XMLUtil.getAttribute(pageEle, "parent");
		PageConfig pageConfig = new PageConfig(parentP);
		List<Element> placeholderEles = XMLUtil.getChildElements(pageEle, "placeholder");
		for(Element placeholderEle : placeholderEles) {
			String nameP = XMLUtil.getAttribute(placeholderEle, "name");
			List<Element> widgetEles = XMLUtil.getChildElements(placeholderEle, "widget");
			for(Element widgetEle : widgetEles) {
				String idW = XMLUtil.getAttribute(widgetEle, "id");
				String pathW = XMLUtil.getAttribute(widgetEle, "path");
				String cssClassW = XMLUtil.getAttribute(widgetEle, "cssClass");
				WidgetConfig widgetConfig = new WidgetConfig(idW, pathW, cssClassW);
				//
				Element titleEle = XMLUtil.getChildElement(widgetEle, "title");
				String titleW = XMLUtil.getValue(titleEle);
				widgetConfig.setTitle(titleW);
				//
				Element prefsEle = XMLUtil.getChildElement(widgetEle, "prefs");
				List<Element> prefEles = XMLUtil.getChildElements(prefsEle);
				for(Element prefEle : prefEles) {
					String key = XMLUtil.getName(prefEle);
					String value = XMLUtil.getValue(prefEle);
					widgetConfig.addPref(key, value);
				}
				pageConfig.add(nameP, widgetConfig);
			}
		}
		return pageConfig;
	}

}
