package org.sarons.spring4me.jdbc.sql;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-7-30 - 下午9:55:30
 */
public class ParsedSql {

	private String sql;
	private List<String> placeholders = new ArrayList<String>();

	public ParsedSql() {
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public List<String> getPlaceholders() {
		return placeholders;
	}
	
	public void addPlaceholder(String placeholder) {
		this.placeholders.add(placeholder);
	}

	public void setPlaceholders(List<String> placeholders) {
		this.placeholders = placeholders;
	}

}
