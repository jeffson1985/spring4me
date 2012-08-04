package org.sarons.spring4me.jdbc.sql;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.springframework.ui.velocity.VelocityEngineUtils;

/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-7-30 - 下午9:55:41
 */
public class VelocitySqlTemplateParser implements SqlTemplateParser {
	
	private static final Pattern pattern = Pattern.compile("\\?\\{(.*?)\\}");
	
	private VelocityEngine velocityEngine;
	
	public VelocitySqlTemplateParser() {
		velocityEngine = new VelocityEngine();
		velocityEngine.addProperty(RuntimeConstants.RESOURCE_LOADER, "string");
		velocityEngine.addProperty(RuntimeConstants.INPUT_ENCODING, "UTF-8");
		velocityEngine.addProperty(RuntimeConstants.OUTPUT_ENCODING, "UTF-8");
		velocityEngine.init();
	}
	
	public ParsedSql parse(String templateName, Map<String, ?> model) {
		//
		String sql = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, templateName, model);
		//
		return parsePlaceholders(sql);
	}
	
	private ParsedSql parsePlaceholders(String sql) {
		Matcher m = pattern.matcher(sql);
		//
		ParsedSql parsedSql = new ParsedSql();
		StringBuffer sqlBuffer = new StringBuffer();
		while(m.find()) {
			String placeholder = sql.substring(m.start(1), m.end(1));
			parsedSql.addPlaceholder(placeholder);
			//
			m.appendReplacement(sqlBuffer, "?");
		}
		//
		m.appendTail(sqlBuffer);
		//
		parsedSql.setSql(sqlBuffer.toString());
		//
		return parsedSql;
	}

}
