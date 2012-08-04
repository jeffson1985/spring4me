package org.sarons.spring4me.jdbc.config;


/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-7-30 - 下午9:54:26
 */
public class SqlConfig {
	
	private String id;
	private String type;
	private String sql;

	public SqlConfig(String id, String type, String sql) {
		this.id = id;
		this.type = type;
		this.sql = sql;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getSql() {
		return sql;
	}
	
	public void setSql(String sql) {
		this.sql = sql;
	}
	
}