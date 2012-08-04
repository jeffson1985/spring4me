package org.sarons.spring4me.jdbc.config;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-7-30 - 下午9:53:59
 */
public class DaoConfig {
	
	private Class<?> daoClass;
	
	private String mapper;
	
	private MapperConfig mapperConfig;

	private Map<String, SqlConfig> sqlConfigMap = new HashMap<String, SqlConfig>();
	
	public DaoConfig(Class<?> daoClass) {
		this.daoClass = daoClass;
	}
	
	public Class<?> getDaoClass() {
		return daoClass;
	}
	
	public void setDaoClass(Class<?> daoClass) {
		this.daoClass = daoClass;
	}
	
	public String getMapper() {
		return mapper;
	}
	
	public void setMapper(String mapper) {
		this.mapper = mapper;
	}
	
	public MapperConfig getMapperConfig() {
		return mapperConfig;
	}
	
	public void setMapperConfig(MapperConfig mapperConfig) {
		this.mapperConfig = mapperConfig;
	}
	
	public Map<String, SqlConfig> getSqlConfigMap() {
		return sqlConfigMap;
	}
	
	public SqlConfig getSqlConfig(String name) {
		return this.sqlConfigMap.get(name);
	}
	
	public void addSqlConfig(SqlConfig sqlConfig) {
		this.sqlConfigMap.put(sqlConfig.getId(), sqlConfig);
	}
	
}
