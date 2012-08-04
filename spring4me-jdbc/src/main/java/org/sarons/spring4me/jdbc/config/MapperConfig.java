package org.sarons.spring4me.jdbc.config;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-7-30 - 下午9:54:16
 */
public class MapperConfig {

	private Class<?> mappedClass;
	
	private Set<String> blobPropertySet = new HashSet<String>();
	
	private Set<String> clobPropertySet = new HashSet<String>();
	
	private Map<String, String> mappedProperties = new LinkedHashMap<String, String>();
	
	private Map<String, String> mappedColumns = new LinkedHashMap<String, String>();
	
	private Map<String, String> mappedSqlTypes = new LinkedHashMap<String, String>();
	
	public MapperConfig(Class<?> mappedClass) {
		this.mappedClass = mappedClass;
	}
	
	public Class<?> getMappedClass() {
		return mappedClass;
	}
	
	public void setMappedClass(Class<?> mappedClass) {
		this.mappedClass = mappedClass;
	}
	
	public boolean isBlobProperty(String property) {
		return blobPropertySet.contains(property);
	}
	
	public boolean isClobProperty(String property) {
		return clobPropertySet.contains(property);
	}
	
	public void addPropertyColumn(String property, String javaType, String column, String sqlType) {
		if("blob".equals(javaType)) {
			blobPropertySet.add(property);
		}
		if("clob".equals(javaType)) {
			clobPropertySet.add(property);
		}
		this.mappedColumns.put(property.toUpperCase(), column);
		this.mappedSqlTypes.put(property.toUpperCase(), sqlType);
		this.mappedProperties.put(column.toUpperCase(), property);
	}
	
	public String getMappedProperty(String column) {
		return this.mappedProperties.get(column.toUpperCase());
	}
	
	public Map<String, String> getMappedProperties() {
		return mappedProperties;
	}
	
	public String getMappedColumn(String property) {
		return this.mappedColumns.get(property.toUpperCase());
	}
	
	public Map<String, String> getMappedColumns() {
		return mappedColumns;
	}
	
	public String getMappedSqlType(String property) {
		return this.mappedSqlTypes.get(property);
	}
	
	public Map<String, String> getMappedSqlTypes() {
		return mappedSqlTypes;
	}
	
}
