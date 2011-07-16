package org.osforce.spring4me.web.widget.config.impl;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.osforce.spring4me.commons.xml.XMLUtil;
import org.osforce.spring4me.web.widget.config.ConfigParser;
import org.osforce.spring4me.web.widget.config.PageConfig;
import org.osforce.spring4me.web.widget.config.WidgetConfig;
import org.w3c.dom.Element;

/**
 * 
 * @author <a href="mailto:haozhonghu@hotmail.com">gavin</a>
 * @since 0.1.0
 * @create May 13, 2011 - 4:38:12 PM
 * <a href="http://www.opensourceforce.org">开源力量</a>
 */
public class XmlConfigParser implements ConfigParser {

	public XmlConfigParser() {
	}
	
	public PageConfig parse(File xmlPage, Map<String, String> paramMap) {
		Element pageEle = findPageEle(xmlPage, paramMap);
		return parseToPageConfig(pageEle);
	}
	
	protected Element findPageEle(File xml, Map<String, String> paramMap) {
		Element pageEle = XMLUtil.parseToElement(xml);
		if(StringUtils.equals(XMLUtil.getName(pageEle), "pages")) {
			String path = paramMap.get("path");
			String qualifier = paramMap.get("qualifier");
			List<Element> pageEles = XMLUtil.getChildElements(pageEle, "page");
			//
			for(Element tmpEle : pageEles) {
				if(XMLUtil.hasAttributeValue(tmpEle, "path", path) && 
						XMLUtil.hasAttributeValue(tmpEle, "qualifier", qualifier)) {
					return tmpEle;
				}
			}
			//
			for(Element tmpEle : pageEles) {
				if(XMLUtil.hasAttributeValue(tmpEle, "qualifier", qualifier)) {
					return tmpEle;
				}
			}
			// default page element
			for(Element tmpEle : pageEles) {
				if(XMLUtil.hasAttributeValue(tmpEle, "path", path) && 
						!XMLUtil.hasAttribute(tmpEle, "qualifier")) {
					pageEle = tmpEle;
				}
			}
		}
		return pageEle;
	}
	
	protected PageConfig parseToPageConfig(Element pageEle) {
		String parentP = XMLUtil.getAttribute(pageEle, "parent");
		String qualifierP = XMLUtil.getAttribute(pageEle, "qualifier");
		PageConfig pageConfig = new PageConfig(parentP, qualifierP);
		List<Element> placeholderEles = XMLUtil.getChildElements(pageEle, "placeholder");
		for(Element placeholderEle : placeholderEles) {
			String nameP = XMLUtil.getAttribute(placeholderEle, "name");
			List<Element> widgetEles = XMLUtil.getChildElements(placeholderEle, "widget");
			for(Element widgetEle : widgetEles) {
				String nameW = XMLUtil.getAttribute(widgetEle, "name");
				String pathW = XMLUtil.getAttribute(widgetEle, "path");
				String cssClassW = XMLUtil.getAttribute(widgetEle, "cssClass");
				String cacheW = XMLUtil.getAttribute(widgetEle, "cache");
				WidgetConfig widgetConfig = new WidgetConfig(nameW, pathW, cssClassW, cacheW);
				//
				Element titleEle = XMLUtil.getChildElement(widgetEle, "title");
				if(titleEle!=null) {
					String titleW = XMLUtil.getValue(titleEle);
					widgetConfig.setTitle(titleW);
				}
				//
				Element prefsEle = XMLUtil.getChildElement(widgetEle, "prefs");
				if(prefsEle!=null) {
				List<Element> prefEles = XMLUtil.getChildElements(prefsEle);
					for(Element prefEle : prefEles) {
						String key = XMLUtil.getName(prefEle);
						String value = XMLUtil.getValue(prefEle);
						widgetConfig.addPref(key, value);
					}
				}
				pageConfig.add(nameP, widgetConfig);
			}
		}
		return pageConfig;
	}
	
}
