package org.sarons.spring4me.jdbc.config;

import java.util.List;

import org.sarons.spring4me.support.xml.XmlParser;
import org.springframework.core.io.Resource;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-7-30 - 下午9:54:21
 */
public abstract class MapperConfigParser {
	
	private static final XmlParser xmlParser = new XmlParser();

	public static MapperConfig parse(Resource resource) throws Exception {
		Document doc = xmlParser.parse(resource);
		return parseDocument(doc.getDocumentElement());
	}
	
	private static MapperConfig parseDocument(Element rootEle) throws ClassNotFoundException {
		String className = rootEle.getAttribute("class");
		Class<?> mappedClass = Class.forName(className);
		//
		MapperConfig mapperConfig = new MapperConfig(mappedClass);
		//
		List<Element> propertyEles = DomUtils.getChildElements(rootEle);
		for(Element propertyEle : propertyEles) {
			String property = propertyEle.getAttribute("name");
			String javaType = propertyEle.getAttribute("type");
			//
			Element columnEle = DomUtils.getChildElementByTagName(propertyEle, "column");
			String column = columnEle.getAttribute("name");
			String sqlType = columnEle.getAttribute("type");
			//
			mapperConfig.addPropertyColumn(property, javaType, column, sqlType);
		}
		//
		return mapperConfig;
	}
	
}
