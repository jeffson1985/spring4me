package org.sarons.spring4me.jdbc.mapper;

import java.beans.PropertyDescriptor;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.sarons.spring4me.jdbc.config.MapperConfig;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;


/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-7-30 - 下午9:54:41
 * @param <T>
 */
public class ConfigurableBeanPropertyRowMapper<T> implements RowMapper<T> {
	
	private MapperConfig mapperConfig;
	
	public ConfigurableBeanPropertyRowMapper(MapperConfig mapperConfig) {
		this.mapperConfig = mapperConfig;
	}
	
	public MapperConfig getMapperConfig() {
		return mapperConfig;
	}
	
	public void setMapperConfig(MapperConfig mapperConfig) {
		this.mapperConfig = mapperConfig;
	}

	public T mapRow(ResultSet rs, int rowNum) throws SQLException {
		T mappedObject = instantiateMappedClass(mapperConfig.getMappedClass());
		BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(mappedObject);
		//
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		//
		for (int index = 1; index <= columnCount; index++) {
			String column = JdbcUtils.lookupColumnName(rsmd, index);
			String property = mapperConfig.getMappedProperty(column);
			if(property!=null) {
				PropertyDescriptor pd = bw.getPropertyDescriptor(property);
				if (pd != null) {
					Object value = getColumnValue(rs, index, pd);
					bw.setPropertyValue(pd.getName(), value);
				}
			}
			//
		}
		//
		return mappedObject;
	}
	
	@SuppressWarnings("unchecked")
	protected T instantiateMappedClass(Class<?> mappedClass) {
		return (T) BeanUtils.instantiate(mappedClass);
	}
	
	protected Object getColumnValue(ResultSet rs, int index, PropertyDescriptor pd) throws SQLException {
		return JdbcUtils.getResultSetValue(rs, index, pd.getPropertyType());
	}

}
