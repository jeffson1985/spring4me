package org.sarons.spring4me.jdbc.config;

import java.util.List;

import org.sarons.spring4me.support.xml.XmlParser;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-7-30 - 下午9:54:04
 */
public abstract class DaoConfigParser {

	private static final XmlParser parser = new XmlParser();
	
	public static DaoConfig parse(Resource resource) throws Exception {
		Document doc = parser.parse(resource);
		return parseDocument(doc.getDocumentElement());
	}
	
	private static DaoConfig parseDocument(Element rootEle) throws ClassNotFoundException {
		String className = rootEle.getAttribute("class");
		Class<?> daoClass = Class.forName(className);
		String mapper = rootEle.getAttribute("mapper");
		//
		DaoConfig daoConfig = new DaoConfig(daoClass);
		daoConfig.setMapper(mapper);
		//
		List<Element> sqlEles = DomUtils.getChildElements(rootEle);
		for(Element sqlEle : sqlEles) {
			String id = sqlEle.getAttribute("id");
			String type = sqlEle.getNodeName();
			String sql = cleanSqlString(DomUtils.getTextValue(sqlEle));
			//
			SqlConfig sqlConfig = new SqlConfig(id, type, sql);
			daoConfig.addSqlConfig(sqlConfig);
		}
		
		return daoConfig;
	}
	
	private static String cleanSqlString(String sqlString) {
		sqlString = StringUtils.trimWhitespace(sqlString);
		sqlString = sqlString.replace("\n", " ");
		sqlString = sqlString.replace("\t", "");
		return sqlString.toLowerCase();
	}
	
}
