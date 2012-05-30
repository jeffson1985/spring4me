/*
 * Copyright 2011-2012 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.osforce.spring4me.jdbc.dao;

import java.util.Collections;
import java.util.List;

import org.osforce.spring4me.dao.BaseDao;
import org.osforce.spring4me.jdbc.core.JdbcTemplateUtils;
import org.osforce.spring4me.jdbc.core.metadata.DefaultTableMetaDataContext;
import org.osforce.spring4me.jdbc.core.metadata.TableMetaDataContext;
import org.osforce.spring4me.jdbc.utils.ModelUtils;
import org.osforce.spring4me.jdbc.utils.SqlUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * 
 * @author gavin
 * @since 0.4.0
 * @create Feb 18, 2012 - 12:02:33 PM
 */
public abstract class AbstractBaseDao<T> extends JdbcDaoSupport 
	implements BaseDao<T> {

	private Class<T> mappedClass;
	private RowMapper<T> rowMapper;
	private TableMetaDataContext tableMetaDataContext;
	//
	private String schemaName;
	private String tableNamePrefix = "osf_";
	private String[] generatedKeyNames = new String[]{"id"};
	
	
	public AbstractBaseDao(Class<T> mappedClass) {
		this.mappedClass = mappedClass;
		this.rowMapper = new BeanPropertyRowMapper<T>(mappedClass);
	}
	
	@Override
	protected void initDao() throws Exception {
		this.tableMetaDataContext = new DefaultTableMetaDataContext();
		this.tableMetaDataContext.setTableName(getTableName());
		this.tableMetaDataContext.setSchemaName(getSchemaName());
		this.tableMetaDataContext.setGeneratedKeyNames(getGeneratedKeyNames());
		this.tableMetaDataContext.setNativeJdbcExtractor(getJdbcTemplate().getNativeJdbcExtractor());
		this.tableMetaDataContext.processMetaData(getDataSource(), getDeclaredColumns());
	}
	
	public Class<T> getMappedClass() {
		return mappedClass;
	}
	
	public RowMapper<T> getRowMapper() {
		return rowMapper;
	}
	
	public TableMetaDataContext getTableMetaDataContext() {
		return tableMetaDataContext;
	}
	
	public T load(Long id) {
		String idName = SqlUtils.name(getGeneratedKeyNames()[0]);
		//
		BeanWrapper beanWrapper = new BeanWrapperImpl(mappedClass);
		beanWrapper.setPropertyValue(idName, id);
		//
		Object model = beanWrapper.getWrappedInstance();
		List<T> result = JdbcTemplateUtils.select(getJdbcTemplate(), model, rowMapper, tableMetaDataContext);
		if(result.size()==1) {
			return (T) result.get(0);
		}
		//
		throw new IncorrectResultSizeDataAccessException(1, result.size());
	}

	public void persist(T model) {
		JdbcTemplateUtils.insert(getJdbcTemplate(), model, tableMetaDataContext);
	}

	public void merge(T model) {
		JdbcTemplateUtils.update(getJdbcTemplate(), model, tableMetaDataContext);
	}

	public void purge(T model) {
		JdbcTemplateUtils.delete(getJdbcTemplate(), model, tableMetaDataContext);
	}
	
	public void purge(Long id) {
		
	}
	
	public List<T> fetchList(T model) {
		String[] filterFields = ModelUtils.getFilterFields(model);
		return JdbcTemplateUtils.select(getJdbcTemplate(), model, filterFields, rowMapper, tableMetaDataContext); 
	}
	
	public T fetchOne(T model) {
		List<T> result = fetchList(model);
		if(result.size()==1) {
			return (T) result.get(0);
		}
		//
		throw new IncorrectResultSizeDataAccessException(1, result.size());
	}
	
	public String getTableName() {
		return getTableNamePrefix() + SqlUtils.underscoreName(mappedClass.getSimpleName());
	}
	
	protected String getTableNamePrefix() {
		return tableNamePrefix;
	}
	
	public void setTableNamePrefix(String tableNamePrefix) {
		this.tableNamePrefix = tableNamePrefix;
	}
	
	public String getSchemaName() {
		return schemaName;
	}
	
	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}
	
	public String[] getGeneratedKeyNames() {
		return generatedKeyNames;
	}
	
	public List<String> getDeclaredColumns() {
		return Collections.emptyList();
	}
	
}
