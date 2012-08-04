package org.sarons.spring4me.jdbc.sql;

import java.util.Map;

/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-7-30 - 下午9:55:36
 */
public interface SqlTemplateParser {

	ParsedSql parse(String templateName, Map<String, ?> model);
	
}
