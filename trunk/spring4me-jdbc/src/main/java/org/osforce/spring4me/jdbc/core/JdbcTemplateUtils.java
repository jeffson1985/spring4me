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

package org.osforce.spring4me.jdbc.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.osforce.spring4me.jdbc.core.metadata.DefaultTableMetaDataContext;
import org.osforce.spring4me.jdbc.core.metadata.TableMetaDataContext;
import org.osforce.spring4me.jdbc.utils.SqlUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlTypeValue;
import org.springframework.jdbc.core.StatementCreatorUtils;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

/**
 * 
 * @author gavin
 * @since 0.4.0
 * @create Feb 18, 2012 - 11:56:31 AM
 */
public abstract class JdbcTemplateUtils {
	
	public static void insert(JdbcTemplate jdbcTemplate, Object model, final TableMetaDataContext tableMetaDataContext) {
		final String insertString = tableMetaDataContext.createInsertString();
		final int[] insertTypes = tableMetaDataContext.createInsertTypes();
		SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(model);
		final List<Object> values = tableMetaDataContext.matchInParameterValuesWithInsertColumns(sqlParameterSource);
		//
		final KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(
				new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
						PreparedStatement ps = prepareStatementForGeneratedKeys(con, tableMetaDataContext, insertString);
						setParameterValues(ps, values, insertTypes);
						return ps;
					}
				},
				keyHolder);
		//
		setGeneratedValues(tableMetaDataContext, model, keyHolder);
	}
	
	public static void update(JdbcTemplate jdbcTemplate, Object model, TableMetaDataContext tableMetaDataContext) {
		update(jdbcTemplate, model, null, tableMetaDataContext);
	}
	
	public static void update(JdbcTemplate jdbcTemplate, Object model, String[] filterFields, TableMetaDataContext tableMetaDataContext) {
		String[] whereColumnNames = SqlUtils.getWhereColumnNames(tableMetaDataContext.getGeneratedKeyNames(), filterFields);
		//
		String updateString = tableMetaDataContext.createUpdateString(whereColumnNames);
		int[] updateTypes = tableMetaDataContext.createUpdateTypes(whereColumnNames);
		SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(model);
		List<Object> values = tableMetaDataContext.matchInParameterValuesWithUpdateColumns(sqlParameterSource, whereColumnNames);
		jdbcTemplate.update(updateString, values.toArray(), updateTypes);
	}

	public static void delete(JdbcTemplate jdbcTemplate, Object model, TableMetaDataContext tableMetaDataContext) {
		delete(jdbcTemplate, model, null, tableMetaDataContext);
	}
	
	public static void delete(JdbcTemplate jdbcTemplate, Object model, String[] filterFields, TableMetaDataContext tableMetaDataContext) {
		String[] whereColumnNames = SqlUtils.getWhereColumnNames(tableMetaDataContext.getGeneratedKeyNames(), filterFields);
		//
		String deleteString = tableMetaDataContext.createDeleteString(whereColumnNames);
		int[] deleteTypes = tableMetaDataContext.createDeleteTypes(whereColumnNames);
		SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(model);
		List<Object> values = tableMetaDataContext.matchInParameterValuesWithDeleteColumns(sqlParameterSource, whereColumnNames);
		jdbcTemplate.update(deleteString, values.toArray(), deleteTypes);
	}
	
	public static <T> List<T> select(JdbcTemplate jdbcTemplate, Object model, RowMapper<T> rowMapper, TableMetaDataContext tableMetaDataContext) {
		return select(jdbcTemplate, model, null, rowMapper, tableMetaDataContext);
	}
	
	public static <T> List<T> select(JdbcTemplate jdbcTemplate, Object model, String[] filterFields, RowMapper<T> rowMapper, TableMetaDataContext tableMetaDataContext) {
		String[] whereColumnNames = SqlUtils.getWhereColumnNames(tableMetaDataContext.getGeneratedKeyNames(), filterFields);
		//
		String selectString = tableMetaDataContext.createSelectString(whereColumnNames);
		int[] selectTypes = tableMetaDataContext.createDeleteTypes(whereColumnNames);
		SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(model);
		List<Object> values = tableMetaDataContext.matchInParameterValuesWithDeleteColumns(sqlParameterSource, whereColumnNames);
		return jdbcTemplate.query(selectString, values.toArray(), selectTypes, rowMapper);
	}
	
	public static TableMetaDataContext getTableMetaDataContext(JdbcTemplate jdbcTemplate, String tableName, String schemaName, String[] generatedKeyNames) {
		TableMetaDataContext tableMetaDataContext = new DefaultTableMetaDataContext();
		tableMetaDataContext.setTableName(tableName);
		tableMetaDataContext.setSchemaName(schemaName);
		tableMetaDataContext.setGeneratedKeyNames(generatedKeyNames);
		tableMetaDataContext.setNativeJdbcExtractor(jdbcTemplate.getNativeJdbcExtractor());
		tableMetaDataContext.processMetaData(jdbcTemplate.getDataSource(), null);
		//
		return tableMetaDataContext;
	}
	
	private static PreparedStatement prepareStatementForGeneratedKeys(Connection connection, 
			TableMetaDataContext tableMetaDataContext, String insertString) throws SQLException {
		if (tableMetaDataContext.getGeneratedKeyNames().length < 1) {
			throw new InvalidDataAccessApiUsageException("Generated Key Name(s) not specificed. " +
					"Using the generated keys features requires specifying the name(s) of the generated column(s)");
		}
		PreparedStatement ps;
		if (tableMetaDataContext.isGeneratedKeysColumnNameArraySupported()) {
			ps = connection.prepareStatement(insertString, tableMetaDataContext.getGeneratedKeyNames());
		}
		else {
			ps = connection.prepareStatement(insertString, Statement.RETURN_GENERATED_KEYS);
		}
		return ps;
	}
	
	private static void setParameterValues(PreparedStatement preparedStatement, List<Object> values, int[] columnTypes)
			throws SQLException {
		int colIndex = 0;
		for (Object value : values) {
			colIndex++;
			if (columnTypes == null || colIndex > columnTypes.length) {
				StatementCreatorUtils.setParameterValue(preparedStatement, colIndex, SqlTypeValue.TYPE_UNKNOWN, value);
			}
			else {
				StatementCreatorUtils.setParameterValue(preparedStatement, colIndex, columnTypes[colIndex - 1], value);
			}
		}
	}
	
	private static void setGeneratedValues(TableMetaDataContext tableMetaDataContext, Object model, KeyHolder keyHolder) {
		BeanWrapper beanWrapper = new BeanWrapperImpl(model);
		String[] generatedKeyNames = tableMetaDataContext.getGeneratedKeyNames();
		for(String generatedKeyName : generatedKeyNames) {
			String name = SqlUtils.name(generatedKeyName);
			Object value = keyHolder.getKeys().get(generatedKeyName);
			beanWrapper.setPropertyValue(name, value);
		}
	}
}
